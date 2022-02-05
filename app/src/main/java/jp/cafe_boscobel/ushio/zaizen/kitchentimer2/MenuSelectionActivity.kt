package jp.cafe_boscobel.ushio.zaizen.kitchentimer2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity

class MenuSelectionActivity : AppCompatActivity() {
    private var _menuList: MutableList<MutableMap<String, Any>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_selection)

        Log.d("uztest", "menu selection is called")

        _menuList = createChoriList()
        val lvMenu = findViewById<ListView>(R.id.lvMenu)

        Log.d("uztets", "menu selection is called 2")

        val from = arrayOf("name", "time")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter = SimpleAdapter(
            this@MenuSelectionActivity,
            _menuList,
            android.R.layout.simple_expandable_list_item_2,
            from,
            to
        )
        lvMenu.adapter = adapter

        lvMenu.onItemClickListener = ListItemClickListener()

    }

    private fun createChoriList(): MutableList<MutableMap<String, Any>> {
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
        var menu = mutableMapOf<String, Any>("name" to "ハンバーグ燻製", "time" to 20)
        menuList.add(menu)
        menu = mutableMapOf("name" to "サーモン燻製", "time" to 20)
        menuList.add(menu)
//        menu = mutableMapOf("name" to "チキンむね肉燻製", "time" to 40)
//        menuList.add(menu)
        menu = mutableMapOf("name" to "タルト生地１", "time" to 30)
        menuList.add(menu)
        menu = mutableMapOf("name" to "タルト生地２", "time" to 8)
        menuList.add(menu)
        menu = mutableMapOf("name" to "キッシュ", "time" to 43)
        menuList.add(menu)
        menu = mutableMapOf("name" to "ゆで卵", "time" to 14)
        menuList.add(menu)
        menu = mutableMapOf("name" to "10分", "time" to 10)
        menuList.add(menu)
        menu = mutableMapOf("name" to "5分", "time" to 5)
        menuList.add(menu)
        menu = mutableMapOf("name" to "1分", "time" to 1)
        menuList.add(menu)

        return menuList
    }

    private fun createSeikaList(): MutableList<MutableMap<String, Any>> {
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
        var menu = mutableMapOf<String, Any>("name" to "チーズケーキ１", "time" to 8)
        menuList.add(menu)
        menu = mutableMapOf("name" to "チーズケーキ２", "time" to 45)
        menuList.add(menu)
        menu = mutableMapOf("name" to "ブランデーケーキ", "time" to 20)
        menuList.add(menu)
        menu = mutableMapOf("name" to "チョコブラウニー", "time" to 17)
        menuList.add(menu)
        menu = mutableMapOf("name" to "シフォンケーキ", "time" to 30)
        menuList.add(menu)
        menu = mutableMapOf("name" to "ジェラート", "time" to 30)
        menuList.add(menu)
        menu = mutableMapOf("name" to "クリームチーズ１", "time" to 15)
        menuList.add(menu)
        menu = mutableMapOf("name" to "クリームチーズ２", "time" to 45)
        menuList.add(menu)

        return menuList
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        when (item.itemId) {
            R.id.menuListOptionChori ->
                _menuList = createChoriList()
            R.id.menuListOptionSeika ->
                _menuList = createSeikaList()
            else ->
                returnVal = super.onOptionsItemSelected(item)
        }
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        val from = arrayOf("name", "time")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter = SimpleAdapter(
            this@MenuSelectionActivity,
            _menuList,
            android.R.layout.simple_expandable_list_item_2,
            from,
            to
        )
        lvMenu.adapter = adapter
        return returnVal
    }

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            val item = parent.getItemAtPosition(position) as MutableMap<String, Any>
            val menuName = item["name"] as String
            var menuTime = item["time"] as Int * 60  //minutes to second

            menuTime = 10 // For Debug

            val intent = Intent()
            intent.putExtra("menu", menuName)
            intent.putExtra("time", menuTime.toString())
            setResult(RESULT_OK, intent)
            finish()

        }
    }

}


