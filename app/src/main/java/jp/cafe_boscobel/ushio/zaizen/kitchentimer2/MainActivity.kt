package jp.cafe_boscobel.ushio.zaizen.kitchentimer2
import android.app.Application
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    companion object {
        var COUNT_DOWN_MILLISECOND: Long = 0
        var INTERVAL_MILLISECOND: Long = 1000
    }

    private lateinit var soundPool: SoundPool
    private var warningSound = 0
    val REQUESTCODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fab.setOnClickListener { view ->
            Log.d("uztest", "fab click listener")

            val intent = Intent(this@MainActivity, MenuSelectionActivity::class.java)
            startActivityForResult(intent, REQUESTCODE)
        }

        var menutime: String = "1"
        var COUNT_DOWN_MILLISECOND: Long = menutime.toLong() * 60000
        var INTERVAL_MILLISECOND: Long = 1000


        val df = SimpleDateFormat("HH:mm:ss")

        val titleText = findViewById<TextView>(R.id.title)
        val timertimer = findViewById<TextView>(R.id.TimerTime)
        val button = findViewById<Button>(R.id.count_down_button)
        val countTextView = findViewById<TextView>(R.id.count_label)

        timertimer.text = ""
        titleText.text = "Test Menu" //menuName.toString()
        timertimer.text = df.format(COUNT_DOWN_MILLISECOND - 9 * 60 * 60 * 1000)

//        button.visibility = View.INVISIBLE
        countTextView.text = df.format(COUNT_DOWN_MILLISECOND)

        val timer = object : CountDownTimer(COUNT_DOWN_MILLISECOND, INTERVAL_MILLISECOND) {
            override fun onTick(millisUntilFinished: Long) {
                // 1秒ごとにテキストを更新
                countTextView.text = df.format(millisUntilFinished - 9 * 60 * 60 * 1000)
            }

            override fun onFinish() {
/*                val test2=intent2TimeManager.getIntExtra("test1",0)
                Log.d("uztest","receiving done")
                Log.d("uztest","received text is"+test2.toString())

 */

                button.visibility = View.VISIBLE

                button.text = "終了"
                button.isEnabled = true
                countTextView.text = "0"


                val audioAttributes = AudioAttributes.Builder()
                        // USAGE_MEDIA
                        // USAGE_GAME
                        .setUsage(AudioAttributes.USAGE_GAME)
                        // CONTENT_TYPE_MUSIC
                        // CONTENT_TYPE_SPEECH, etc.
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()

                soundPool = SoundPool.Builder()
                        .setAudioAttributes(audioAttributes)
                        // ストリーム数に応じて
                        .setMaxStreams(2)
                        .build()

                warningSound = soundPool.load(this@MainActivity, R.raw.warningsound, 1)



                button.setOnClickListener {
                    soundPool.stop(warningSound)
                    finish()
                }

                Thread.sleep(500)
                soundPool.play(warningSound, 1.0f, 1.0f, 0, -1, 1.0f)

            }
        }


        button.setOnClickListener {
            timer.cancel()
            finish()
        }

        //　戻るボタン（←）の表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        timer.start()
        button.text = "終了"
        button.isEnabled = true

    }


    // 戻るボタンの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        if(item.itemId == android.R.id.home){
            onPause()
            finish()
        }
        else{
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("uztest","result returned")
        if (requestCode == REQUESTCODE){if (RESULT_OK == resultCode) {
            var recievemenu: String = data!!.getStringExtra("menu")!!
            var recievetime: String = data!!.getStringExtra("time")!!

            Log.d("uztest", "received menu is " + recievemenu)
            Log.d("uztest", "recieved time is " + recievetime)
        }
        }
        }


    override fun onStart(){
        super.onStart()
        Log.d("uztest", "onStart")
    }

    override fun onResume () {
        super.onResume()
        Log.d("uztest", "onResume")
    }

    override fun onPause(){
        super.onPause()
        Log.d("uztest", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("uztest", "onStop")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d("uztest", "onDestroy")
    }


}