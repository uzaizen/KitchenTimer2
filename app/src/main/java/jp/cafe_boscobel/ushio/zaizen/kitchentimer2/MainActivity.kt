package jp.cafe_boscobel.ushio.zaizen.kitchentimer2
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity()  {

    val REQUESTCODE = 1

private lateinit var mainHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainHandler = Handler(Looper.getMainLooper())

        fab.setOnClickListener { view ->
            Log.d("uztest", "fab click listener")

            val intent = Intent(this@MainActivity, MenuSelectionActivity::class.java)
            startActivityForResult(intent, REQUESTCODE)
        }

        Timer().scheduleAtFixedRate(TimerCallback3(), 0, 1000)
    }

    inner class TimerCallback3 : TimerTask() {
        var i:Int = 0
        override fun run() {
            mainHandler.post(Runnable {
                Log.d("uztest", "timer called ${i}")
                i++
                count_label1.text = i.toString()
                //画面処理
            })
        }
    }



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

                Log.d("uztest", "received menu is " + recievemenu)
                Log.d("uztest", "recieved time is " + recievetime)
            }
        }
    }

/*
    var i:Int =0
    override fun run(){
        Log.d("uztest","i=${i}" )
        i++
    }

 */

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