package com.noesysmobile.wdtlibrary

import android.content.Intent
import android.app.Service
import android.os.*
import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_REGISTER_CLIENT
import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_RESET
import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_UNREGISTER_CLIENT
import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_WDT_EXPIRED
import java.util.*
import kotlin.collections.HashMap
import android.R.string.cancel
import android.util.Log


class WdtService : Service() {


    /** Keeps track of all current registered clients.  */
    internal var mClients = HashMap<String, Messenger>()
    /** Keeps track of all current registered clients' timeouts.  */
    internal var mClientsTimeouts = HashMap<String, Int>()
   /** Keeps track of all current registered clients' timeouts.  */
    internal var mCounters = HashMap<String, Int>()


    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    internal val mMessenger = Messenger(IncomingHandler())

    /**
     * Handler of incoming messages from clients.
     */
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_REGISTER_CLIENT -> addClient(msg.replyTo, msg.obj as String, msg.arg1)
                MSG_UNREGISTER_CLIENT -> removeClient(msg.obj as String)
                MSG_RESET -> resetCounter(msg.obj as String)
                else -> super.handleMessage(msg)
            }
        }
    }

    private fun addClient(replyTo: Messenger, id: String, timeout: Int) {
        Log.d("WdtService", "addClient")
        mClients[id] = replyTo
        mClientsTimeouts[id] = timeout
        mCounters[id] = 0
    }

    private fun removeClient(id: String) {
        Log.d("WdtService", "removeClient")
        mClients.remove(id)
        mClientsTimeouts.remove(id)
        mCounters.remove(id)
    }

    private fun resetCounter(id: String) {
        Log.d("WdtService", "resetCounter")
        mCounters[id] = 0
    }

    /**
     * When timer end, increment all counters, check if timeout is gone and eventually notify client and reset the counter
     */
    private fun tick(){
        Log.d("WdtService", "tick")
        mCounters.forEach { (key, value) -> mCounters[key] = value + 1 }
        mClientsTimeouts.forEach{(key, value) -> checkTimeout(key, value) }
    }

    private fun checkTimeout(key: String, value: Int) {
        Log.d("WdtService", "checkTimeout")
        if (mCounters[key]!! >= value){
            // Checked counter exceeds timeout, signal and reset it
            sendTimeoutMessage(key)
            resetCounter(key)
        }

    }

    private fun sendTimeoutMessage(key: String) {
        Log.d("WdtService", "sendTimeoutMessage")
        val msg = Message.obtain()
        msg.what = MSG_WDT_EXPIRED
        try {
            mClients[key]!!.send(msg)
        } catch (e: Exception) {
        }
        mCounters[key] = 0
    }

    override fun onCreate() {
        Log.d("WdtService", "onCreate")
        Timer("WDT", false).scheduleAtFixedRate(TickTask(), 1000, 1000)
    }

    override fun onDestroy() {
        Log.d("WdtService", "onDestroy")
        Timer("WDT").cancel()
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder {
        Log.d("WdtService", "onBind")
        return mMessenger.binder
    }

    internal inner class TickTask : TimerTask() {
        override fun run() {
            Log.d("WdtService", "run")
            tick()
        }
    }

}