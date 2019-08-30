package com.noesysmobile.wdtlibtest

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.noesysmobile.wdtlibrary.Wdt
import com.noesysmobile.wdtlibrary.WdtCallback

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), WdtCallback {


    var wdt: Wdt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            wdt!!.reset()
        }

    }

    override fun onResume() {
        Log.d("MainActivity", "onResume")
        super.onResume()
        wdt = Wdt(this, 10, this)
    }

    override fun onPause() {
        Log.d("MainActivity", "onPause")
        wdt!!.release()
        wdt = null
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
    }

}
