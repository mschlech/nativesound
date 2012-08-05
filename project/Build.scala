import java.io.FileWriter
import scala.xml.{Node => XmlNode}
import scala.xml._
import sbt._


import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq(
    name := "NativeSound",
    version := "0.1",
    versionCode := 0,
    scalaVersion := "2.9.1",
    platformName in Android := "android-15",
    resolvers += "ActionBarSherlock snapshots" at "http://r.jakewharton.com/maven/snapshot/"
  )

  val proguardSettings = Seq(
    useProguard in Android := true
  )

  val pgOptions = Seq("-keep class android.support.v4.app.** { *; }",
    "-keep interface android.support.v4.app.** { *; }",
    "-keep class com.actionbarsherlock.** { *; }",
    "-keep interface com.actionbarsherlock.** { *; }",
    "-keepattributes *Annotation*").mkString(" ")

  lazy val fullAndroidSettings =
    General.settings ++
      AndroidProject.androidSettings ++
      TypedResources.settings ++
      proguardSettings ++
      AndroidManifestGenerator.settings ++
      AndroidMarketPublish.settings ++ Seq(
      proguardOption in Android := pgOptions,
      keyalias in Android := "change-me",
      libraryDependencies ++= Seq("org.specs2" %% "specs2" % "1.9" % "test",
        "com.actionbarsherlock" % "library" % "4.0.2" artifacts (Artifact("library", "apklib", "apklib")))
    )
}

object AndroidBuild extends Build with AndroidScalaLibs {

  def buildWithoutProguard = Command.command("build-without-proguard") {
    state =>
      val extracted = Project.extract(state)
      import extracted._

      val transformed = session.mergeSettings :+ (useProguard in Android := false)
      val newStructure = Load.reapply(transformed, structure)
      val newState = Project.setProject(session, newStructure, state)
      Project.evaluateTask(buildAndRun, newState)
      state
  }

  def addScalaLibToManifestTask = (manifestPath in Android) map {
    manPath =>
      addScalaLibToManifest(manPath.head)
  }

  def removeScalaLibFromManifestTask = (manifestPath in Android) map {
    manPath =>
      removeScalaLibFromManifest(manPath.head)
  }

  val buildAndRun = TaskKey[Unit]("build-and-run", "Builds and runs the app on a device")
  val removeScalaLib = TaskKey[Unit]("remove-scala-uses", "Removes Scala uses-library from AndroidManifest.xml")
  val addScalaLib = TaskKey[Unit]("add-scala-uses", "Adds Scala uses-library to AndroidManifest.xml")

  lazy val main = Project(
    "NativeSound",
    file("."),
    settings = General.fullAndroidSettings ++ AndroidNdk.settings ++ TypedResources.settings ++ Seq(
      useProguard in Android := true
    )
  )

  lazy val dev = Project(
    "dev",
    file("."),
    settings = General.fullAndroidSettings ++ Seq(
      name := "nativesound-dev",
      removeScalaLib <<= removeScalaLibFromManifestTask,
      addScalaLib <<= addScalaLibToManifestTask,
      buildAndRun <<= removeScalaLibFromManifestTask,
      buildAndRun <<= buildAndRun dependsOn (startDevice in Android),
      buildAndRun <<= buildAndRun dependsOn (packageDebug in Android),
      buildAndRun <<= buildAndRun dependsOn addScalaLib,
      useProguard in Android := false
    )
  )

  lazy val tests = Project(
    "tests",
    file("tests"),
    settings = General.settings ++
      AndroidTest.androidSettings ++
      General.proguardSettings ++ Seq(
      name := "NativeSoundTests"
    )
  ) dependsOn main
}

trait AndroidScalaLibs {
  def pp = new PrettyPrinter(150, 5)

  def addScalaLibToManifest(manifest: File) {
    val xml = XML.load(manifest.absolutePath)
    val appNode = (xml \ "application").head
    val appNodeWithScala = addChildren(appNode, scalaNodes)
    val newRootChildren = xml.child.filterNot(appNode.equals) ++ appNodeWithScala
    val newXml = Elem(xml.prefix, xml.label, xml.attributes, xml.scope, newRootChildren: _*)
    writeToFile(manifest.absolutePath, pp.format(newXml))
  }

  def removeScalaLibFromManifest(manifest: File) {
    val xml = XML.load(manifest.absolutePath)
    val appNode = (xml \ "application").head
    val newAppChildren = appNode.child.filterNot(scalaNodes.contains)
    val appNodeWithoutScala = Elem(appNode.prefix, appNode.label, appNode.attributes, appNode.scope, newAppChildren: _*)
    val newRootChildren = xml.child.filterNot(appNode.equals) ++ appNodeWithoutScala
    val newXml = Elem(xml.prefix, xml.label, xml.attributes, xml.scope, newRootChildren: _*)
    writeToFile(manifest.absolutePath, pp.format(newXml))
  }

  def addChildren(n: XmlNode, newChildren: Seq[XmlNode]) = n match {
    case Elem(prefix, label, attribs, scope, child@_*) =>
      Elem(prefix, label, attribs, scope, child ++ newChildren: _*)
    case _ => sys.error("Can only add children to elements!")
  }

  lazy val scalaNodes = {
    Seq(<uses-library android:name="scala_actors-2.9.1"/>,
        <uses-library android:name="scala_collection-2.9.1"/>,
        <uses-library android:name="scala_immutable-2.9.1"/>,
        <uses-library android:name="scala_library-2.9.1"/>,
        <uses-library android:name="scala_mutable-2.9.1"/>)
  }

  def writeToFile(path: String, data: String) {
    val f = new FileWriter(path)
    f.write(data)
    f.close()
  }

}
