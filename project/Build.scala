import sbt._

import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq(
    name := "NativeSound",
    version := "0.1",
    versionCode := 0,
    scalaVersion := "2.9.1",
    platformName in Android := "android-10"
  )

  val proguardSettings = Seq(
    useProguard in Android := true
  )

  lazy val fullAndroidSettings =
    General.settings ++
      AndroidProject.androidSettings ++
      TypedResources.settings ++
      proguardSettings ++
      AndroidManifestGenerator.settings ++
      AndroidMarketPublish.settings ++ Seq(
      keyalias in Android := "change-me",
      libraryDependencies ++= Seq("org.specs2" %% "specs2" % "1.9" % "test")
    )
}

object AndroidBuild extends Build {
  lazy val main = Project(
    "NativeSound",
    file("."),
    settings = General.fullAndroidSettings ++ AndroidNdk.settings ++ TypedResources.settings
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
