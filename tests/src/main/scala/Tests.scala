package com.entscheidungsbaum.nativesound.tests

import com.entscheidungsbaum.nativesound._
import junit.framework.Assert._
import _root_.android.test.AndroidTestCase
import _root_.android.test.ActivityInstrumentationTestCase2

class AndroidTests extends AndroidTestCase {
  def testPackageIsCorrect() {
    assertEquals("com.entscheidungsbaum.nativesound", getContext.getPackageName)
  }
}

class ActivityTests extends ActivityInstrumentationTestCase2(classOf[com.entscheidungsbaum.nativesound.SoundMainActivity]) {
   def testHelloWorldIsShown() {
      val activity = getActivity
      val textview = activity.findView(TR.textview)
      assertEquals(textview.getText, "hello, world!")
    }
}
