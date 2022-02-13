package jp.cafe_boscobel.ushio.zaizen.kitchentimer2
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


//  Title#   :    タイマーのタイトル（料理名）
// TimerTime#:    タイマー設定時間
// Count_Down_ButtonA# :  ビープ音終了ボタン
// Count_Down_ButtonB# :  タイマー停止
// Count_Down_ButtonC#:  タイマー再開
// Count_Down_ButtonD#:  タイマー途中終了
// Count_Label# :     タイマー残り時間

// timerswitch   :   0:timer is not assigned, other number: timer number, -1:ending during countdown, -2: ending finishing count down
// timerstopsw :  1: Timer to pause count down, 0: Timer to count down

data class timedata(var hour: Int, var minites: Int, var second: Int)

class MainActivity : AppCompatActivity()  {

    val REQUESTCODE = 1
    var timerswitch = arrayOf<Int>(0, 0, 0, 0, 0, 0)
    var timername = arrayOfNulls<String>(6)
    var timertime = arrayOfNulls<Int>(6)
    var timeremaining = arrayOf<Int>(0, 0, 0, 0, 0, 0)
    var timerstopsw = arrayOf<Int>(0, 0, 0, 0, 0, 0)

    private var mtime:timedata? = null

    private lateinit var mainHandler : Handler

    private lateinit var soundPool: SoundPool
    private var warningSound = arrayOf<Int> (0,0,0,0,0,0)

    lateinit var ButtonA:Array<Button>
    lateinit var ButtonB: Array<Button>
    lateinit var ButtonC: Array<Button>
    lateinit var ButtonD: Array<Button>

    lateinit var TitleN: Array<TextView>
    lateinit var TimerTimeN: Array<TextView>
    lateinit var Count_LabelN: Array<TextView>

    var availabeN: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButtonA = arrayOf(Count_Down_ButtonA1, Count_Down_ButtonA2, Count_Down_ButtonA3, Count_Down_ButtonA4, Count_Down_ButtonA5, Count_Down_ButtonA6)
        ButtonB = arrayOf(Count_Down_ButtonB1, Count_Down_ButtonB2, Count_Down_ButtonB3, Count_Down_ButtonB4, Count_Down_ButtonB5, Count_Down_ButtonB6)
        ButtonC = arrayOf(Count_Down_ButtonC1, Count_Down_ButtonC2, Count_Down_ButtonC3, Count_Down_ButtonC4, Count_Down_ButtonC5, Count_Down_ButtonC6)
        ButtonD = arrayOf(Count_Down_ButtonD1, Count_Down_ButtonD2, Count_Down_ButtonD3, Count_Down_ButtonD4, Count_Down_ButtonD5, Count_Down_ButtonD6)

        TitleN = arrayOf(Title1, Title2, Title3, Title4, Title5, Title6)
        TimerTimeN = arrayOf(TimerTime1, TimerTime2, TimerTime3, TimerTime4, TimerTime5, TimerTime6)
        Count_LabelN = arrayOf(Count_Label1, Count_Label2, Count_Label3, Count_Label4, Count_Label5, Count_Label6)

        Log.d("uztest", "phase 00")

        mainHandler = Handler(Looper.getMainLooper())

        Log.d("uztest", "phase 0")

        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, MenuSelectionActivity::class.java)
            startActivityForResult(intent, REQUESTCODE)
        }

        Log.d("uztest", "phase1")

        Timer().scheduleAtFixedRate(TimerCallback(), 0, 1000)

        Log.d("uztest", "ButtonN clearance start")
        for (i in 0..5){
            ButtonA[i].visibility = View.INVISIBLE
            ButtonB[i].visibility = View.INVISIBLE
            ButtonC[i].visibility = View.INVISIBLE
            ButtonD[i].visibility = View.INVISIBLE
        }

        Log.d("uztest", "ButtonN cleared")
    }

    inner class TimerCallback : TimerTask() {
        var i:Int = 0
        var idx:Int = 0

        override fun run() {
            mainHandler.post(Runnable {

                for (idx in 0..5) {

                    if (timerswitch[idx] == 1) {
                        if (timerstopsw[idx] == 0) {
                            timeremaining[idx] -= 1
                        }

                        TitleN[idx].text = timername[idx].toString()
                        var timet: timedata = timetransfer(timertime[idx]!!)
                        TimerTimeN[idx].text = "${timet.hour}:${timet.minites}:${timet.second}"
                        timet = timetransfer(timeremaining[idx])
                        Count_LabelN[idx].text = "${timet.hour}:${timet.minites}:${timet.second}"
                        ButtonA[idx].visibility = View.INVISIBLE

                        TitleN[idx].visibility = View.VISIBLE
                        TimerTimeN[idx].visibility = View.VISIBLE
                        Count_LabelN[idx].visibility = View.VISIBLE

                        ButtonB[idx].text = "停止"
                        ButtonB[idx].visibility = View.VISIBLE
                        ButtonC[idx].text = "再開"
                        ButtonC[idx].visibility = View.VISIBLE
                        ButtonD[idx].text = "終了"
                        ButtonD[idx].visibility = View.VISIBLE

                        ButtonB[idx].setOnClickListener {
                            timerstopsw[idx] = 1
                        }

                        ButtonC[idx].setOnClickListener {
                            timerstopsw[idx] = 0
                        }

                        ButtonD[idx].setOnClickListener {
                            timerswitch[idx] = -1
                        }

                        var timem: timedata = timetransfer(timeremaining[idx])
                        Count_LabelN[idx].text = "${timem.hour}:${timem.minites}:${timem.second}"

                        if (timeremaining[idx] == 0) Endingoperation(idx)
                    } else if (timerswitch[idx] == -1) {
                        ButtonB[idx].visibility = View.INVISIBLE
                        ButtonC[idx].visibility = View.INVISIBLE
                        ButtonD[idx].visibility = View.INVISIBLE
                        TitleN[idx].visibility = View.INVISIBLE
                        TimerTimeN[idx].visibility = View.INVISIBLE
                        Count_LabelN[idx].visibility = View.INVISIBLE
                        timerswitch[idx] = 0
                    } else if (timerswitch[idx] == -2) {
                        timerswitch[idx] = 0
                    }
                }
            }
            )
        }

        fun Endingoperation(idx: Int){
            timerswitch[idx] = -2
            ButtonB[idx].visibility = View.INVISIBLE
            ButtonC[idx].visibility = View.INVISIBLE
            ButtonD[idx].visibility = View.INVISIBLE

            ButtonA[idx].visibility = View.VISIBLE

            ButtonA[idx].text = "終了"
            ButtonA[idx].isEnabled = true
            Count_LabelN[idx].text = "0"

            val oldColors: ColorStateList = TitleN[idx].getTextColors()
            TitleN[idx].setTextColor(Color.rgb(255, 0, 0))

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

            warningSound[idx] = soundPool.load(this@MainActivity, R.raw.warningsound, 1)

            ButtonA[idx].setOnClickListener {
                soundPool.stop(warningSound[idx])
                ButtonA[idx].text = " "
                ButtonA[idx].isEnabled = false
                ButtonA[idx].visibility = View.INVISIBLE
                Count_LabelN[idx].text = " "
                TitleN[idx].setTextColor(oldColors)
                timerswitch[idx] = 0
            }

            Thread.sleep(500)
            soundPool.play(warningSound[idx], 1.0f, 1.0f, 0, -1, 1.0f)
        }

        }

    fun timetransfer(timesecond: Int):timedata{
        var hh:Int = 0
        var mm:Int = 0
        var ss:Int = 0
        ss = timesecond % 60
        mm = timesecond / 60
        hh = timesecond / 60 / 60
        mtime = timedata(hh, mm, ss)
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

                availabeN = timerswitch.indexOf(0)

                if (availabeN != -1) {
                    timerswitch[availabeN] = 1
                    timername[availabeN] = recievemenu.toString()
                    timertime[availabeN] = recievetime.toInt()

                    timeremaining[availabeN] = timertime[availabeN]!!
                }
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