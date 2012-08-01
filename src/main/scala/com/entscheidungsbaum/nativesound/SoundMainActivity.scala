package com.entscheidungsbaum.nativesound

import _root_.android.app.Activity
import android.os.{Environment, Bundle}
import android.widget.Button
import android.view.View
import android.media.MediaRecorder
import java.io.{IOException, File}
import android.content.{Intent, ContentValues}
import android.provider.MediaStore

class SoundMainActivity extends Activity {

  import SoundMainActivity._

  /**
   * first create some members initialized upfront
   */

  private var mRecordButton: Button = _

  private var mStopRecordButton: Button = _
  private var mRecord: MediaRecorder = _

  private val mSampleFile: File = {
    val mSampleFileDir = Environment.getExternalStorageDirectory
    Logger.info(mSampleFileDir.getPath)
    try {

      File.createTempFile(SAMPLE_PREFIX, SAMPLE_EXTENSION, mSampleFileDir)
    } catch {
      case e: IOException =>
        Logger.info("sdcard access error"+e)
        System.exit(1)
        null
      case e1: Exception =>
        LogException.error(e1.getCause)
        System.exit(1)
        null
    }

  }

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    Logger.info("starting up")

    mRecord = new MediaRecorder()

    setContentView(R.layout.main)
    //    mRecordButton.setKeepScreenOn(true)
    //    mRecordButton.setOnClickListener(new View.OnClickListener() {
    //      override def onClick(v:View) {
    //        startRecord();
    //      }
    //    })
    //findView(TR.textview).setText("hello, world!")
    mRecordButton = findViewById(R.id.main_record).asInstanceOf[Button]
    mStopRecordButton = findViewById(R.id.main_stop_record).asInstanceOf[Button]
    mStopRecordButton setEnabled false


    mRecordButton setOnClickListener new View.OnClickListener() {
      def onClick(v: View) {
        startRecord()
        mRecordButton setEnabled false
        mStopRecordButton setEnabled true
      }
    }

    mStopRecordButton setOnClickListener new View.OnClickListener() {
      def onClick(v: View) {

        stopRecording()
        mRecordButton setEnabled true
        mStopRecordButton setEnabled false
      }
    }


    //setListener

  }

  //  def setListener = {
  //    mRecordButton.setOnClickListener(new OnClickListener {
  //      def onClick(v: View) {
  //        startRecord();
  //      }
  //    })
  //  }

  //
  //  def setListener = {
  //
  //    mRecordButton.setOnClickListener(new View.OnClickListener() {
  //      def onClick(v: View) {
  //        val recordIntent = new Intent(SoundMainActivity.this, classOf[RecordActivity])
  //        startActivity(recordIntent)
  //        finish();
  //      }
  //    })
  //  }

  /**
   * not in use so far
   *
   * @return boolean whether the device meet all the requirements
   */
  def checkDevice() = true

  object MediaRecorder extends MediaRecorder

  def startRecord() = {
    mRecord = new MediaRecorder()
    mRecord setAudioSource MediaRecorder.AudioSource.MIC
    mRecord setOutputFormat MediaRecorder.OutputFormat.THREE_GPP
    mRecord setAudioEncoder MediaRecorder.AudioEncoder.AMR_NB
    mRecord setOutputFile mSampleFile.getAbsolutePath
    try {
      mRecord.prepare()
      mRecord.start()
    } catch {
      case ex: Exception => Logger.info(ex.getStackTraceString)
    }
  }

  protected def stopRecording() {
    mRecord.stop()
    mRecord.release()
  }

  protected def addToDB() {
    val values = new ContentValues(3)
    val current = System.currentTimeMillis

    values.put(MediaStore.MediaColumns.TITLE, "test_audio")
    values.put(MediaStore.MediaColumns.DATE_ADDED, Predef.float2Float(current / 1000))
    values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
    values.put(MediaStore.MediaColumns.DATA, mSampleFile.getAbsolutePath)
    val contentResolver = getContentResolver()

    val base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val newUri = contentResolver.insert(base, values)

    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri))
  }
}

/**
 * get a log object
 */

object Logger {
  def info(mLogMessage: String) {
    val TAG = "SoundRecordingDemo"

    android.util.Log.i(TAG, mLogMessage)
  }
}

object LogException {
  def error(mException : Throwable) {
  android.util.Log.getStackTraceString(mException)
  }
}

/**
 * a companion object
 */
object SoundMainActivity {
  private final val SAMPLE_PREFIX = "recording"
  private final val SAMPLE_EXTENSION = ".mp3"
}

