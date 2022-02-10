package jp.cafe_boscobel.ushio.zaizen.kitchentimer2
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

//  Title1   :    タイマーのタイトル（料理名）
// TimerTime1:    タイマー設定時間
// Count_Down_Button1 :  ビープ音終了ボタン
// Count_Down_Button2 :  タイマー停止
// Count_Down_Button3:  タイマー再開
// Count_Down_Button4:  タイマー途中終了
// Count_Label1 :     タイマー残り時間

// timerswitch   :   0:timer is not assigned, other number: timer number, -1:ending during countdown, -2: ending finishing count down
// timerstopsw :  1: Timer to pause count down, 0: Timer to count down

data class timedata(var hour:Int, var minites:Int, var second:Int)

class MainActivity : AppCompatActivity()  {

    val REQUESTCODE = 1
    var timerswitch = arrayOf<Int>(0,0,0,0,0,0)
    var timername = arrayOfNulls<String>(6)
    var timertime = arrayOfNulls<Int>(6)
    var timeremaining = arrayOf<Int>(0,0,0,0,0,0)
    var timerstopsw = arrayOf<Int>(0,0,0,0,0,0)

    private var mtime:timedata? = null

    private lateinit var mainHandler : Handler

    private lateinit var soundPool: SoundPool
    private var warningSound = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainHandler = Handler(Looper.getMainLooper())

        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, MenuSelectionActivity::class.java)
            startActivityForResult(intent, REQUESTCODE)
        }

        Timer().scheduleAtFixedRate(TimerCallback3(), 0, 1000)

        Count_Down_Button1.visibility = View.INVISIBLE
        Count_Down_Button2.visibility = View.INVISIBLE
        Count_Down_Button3.visibility = View.INVISIBLE
        Count_Down_Button4.visibility = View.INVISIBLE
    }

    inner class TimerCallback3 : TimerTask() {
        var i:Int = 0

        override fun run() {
            mainHandler.post(Runnable {
                if (timerswitch[0] == 1) {
                    if (timerstopsw[0]==0) {timeremaining[0] -= 1}

                    Title1.text = timername[0].toString()
                    var timet : timedata = timetransfer(timertime[0]!!)
                    TimerTime1.text = "${timet.hour}:${timet.minites}:${timet.second}"
                    timet = timetransfer(timeremaining[0])
                    Count_Label1.text = "${timet.hour}:${timet.minites}:${timet.second}"
                    Count_Down_Button1.visibility = View.INVISIBLE

                    Title1.visibility=View.VISIBLE
                    TimerTime1.visibility=View.VISIBLE
                    Count_Label1.visibility=View.VISIBLE

                    Count_Down_Button2.text="停止"
                    Count_Down_Button2.visibility=View.VISIBLE
                    Count_Down_Button3.text="再開"
                    Count_Down_Button3.visibility=View.VISIBLE
                    Count_Down_Button4.text="終了"
                    Count_Down_Button4.visibility=View.VISIBLE

                    Count_Down_Button2.setOnClickListener{
                        timerstopsw[0] = 1
                    }

                    Count_Down_Button3.setOnClickListener{
                        timerstopsw[0] = 0
                    }

                    Count_Down_Button4.setOnClickListener{
                        timerswitch[0] = -1
                    }

                    var timem : timedata = timetransfer(timeremaining[0])
                    Count_Label1.text = "${timem.hour}:${timem.minites}:${timem.second}"

                    if (timeremaining[0] == 0) Endingoperation()
                }
                else if (timerswitch[0] == -1) {
                    Count_Down_Button2.visibility = View.INVISIBLE
                    Count_Down_Button3.visibility = View.INVISIBLE
                    Count_Down_Button4.visibility = View.INVISIBLE
                    Title1.visibility = View.INVISIBLE
                    TimerTime1.visibility = View.INVISIBLE
                    Count_Label1.visibility = View.INVISIBLE
                    timerswitch[0] = 0
                }
                else if (timerswitch[0] == -2) {
                    timerswitch[0] = 0
                }
            }
                )
        }

        fun Endingoperation(){
            timerswitch[0] = -2
            Count_Down_Button2.visibility = View.INVISIBLE
            Count_Down_Button3.visibility = View.INVISIBLE
            Count_Down_Button4.visibility = View.INVISIBLE

            Count_Down_Button1.visibility = View.VISIBLE

            Count_Down_Button1.text = "終了"
            Count_Down_Button1.isEnabled = true
            Count_Label1.text = "0"

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

            Count_Down_Button1.setOnClickListener {
                soundPool.stop(warningSound)
                Count_Down_Button1.text = " "
                Count_Down_Button1.isEnabled = false
                Count_Down_Button1.visibility = View.INVISIBLE
                Count_Label1.text = " "
                timerswitch[0] = 0
            }

            Thread.sleep(500)
            soundPool.play(warningSound, 1.0f, 1.0f, 0, -1, 1.0f)
        }

        }

    fun timetransfer(timesecond:Int):timedata{
        var hh:Int = 0
        var mm:Int = 0
        var ss:Int = 0
        ss = timesecond % 60
        mm = timesecond / 60
        hh = timesecond / 60 / 60
        mtime = timedata(hh,mm,ss)
        return mtime!! }


    // 戻るボタンの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        if (item.itemId == android.R.id.home) {
            onPause()
            finish()
        } else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("uztest", "result returned")
        if (requestCode == REQUESTCODE) {
            if (RESULT_OK == resultCode) {
                var recievemenu: String = data!!.getStringExtra("menu")!!
                var recievetime: String = data!!.getStringExtra("time")!!

                timerswitch[0] = 1
                timername[0] = recievemenu.toString()
                timertime[0] = recievetime.toInt()

                timeremaining[0] = timertime[0]!!

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