package com.noesysmobile.wdtlibtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.noesysmobile.wdtlibrary.Wdt
import com.noesysmobile.wdtlibrary.WdtCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView.OnItemClickListener




class MainActivity : AppCompatActivity(), WdtCallback {


    private var wdt: ArrayList<Wdt>? = ArrayList()

    private var adapter: WdtListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        adapter = WdtListAdapter(this, R.layout.list_row, wdt!!)
        list_view.adapter = adapter
        list_view.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            wdt!![position].reset()
        }

        fab.setOnClickListener { view ->
            wdt!!.add(Wdt(this, Random().nextInt(10) + 1, this))
            adapter!!.notifyDataSetChanged()
        }

    }

    override fun onResume() {
        Log.d("MainActivity", "onResume")
        super.onResume()
        wdt!!.add(Wdt(this, 10, this))
        wdt!!.add(Wdt(this, 5, this))
        adapter!!.notifyDataSetChanged()
    }

    override fun onPause() {
        Log.d("MainActivity", "onPause")
        wdt!![0]!!.release()
        wdt!![1]!!.release()
        wdt!!.clear()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onWdtExpired() {
        Log.d("MainActivity", "WDT Expired")
        adapter!!.notifyDataSetChanged()
    }

}
