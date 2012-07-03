package com.entscheidungsbaum.nativesound

import _root_.android.app.Activity
import _root_.android.os.Bundle
import android.widget.Button
import android.view.View
import android.content.Intent

class SoundMainActivity extends Activity {

  var mRecordButton: Button = _

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

    setContentView(R.layout.main)
    //    mRecordButton.setKeepScreenOn(true)
    //    mRecordButton.setOnClickListener(new View.OnClickListener() {
    //      override def onClick(v:View) {
    //        startRecord();
    //      }
    //    })
    //findView(TR.textview).setText("hello, world!")
    mRecordButton = findViewById(R.id.main_record).asInstanceOf[Button]
    setListener

  }


  def setListener = {

    mRecordButton.setOnClickListener(new View.OnClickListener() {
      def onClick(v: View) {
        val recordIntent = new Intent(SoundMainActivity.this, classOf[RecordActivity])
        startActivity(recordIntent)
        finish();
      }
    })
  }

  /**
   * not in use so far
   *
   * @return boolean whether the device meet all the requirements
   */
  def checkDevice(): Boolean = true


  def startRecord() = {

  }

}

/**
 * get a log object
 */

object Log {
  def info(mLogMessage: String) {
    android.util.Log.i("androidNativeSound", mLogMessage)
  }
}