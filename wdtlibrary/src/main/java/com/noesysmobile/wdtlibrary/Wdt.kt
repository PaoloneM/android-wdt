package com.noesysmobile.wdtlibrary

import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_WDT_EXPIRED
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.*
import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_REGISTER_CLIENT
import android.content.Intent
import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_RESET
import com.noesysmobile.wdtlibrary.WdtConstants.Companion.MSG_UNREGISTER_CLIENT
import java.util.*


class Wdt(context: Context, timeout: Int, callback: WdtCallback)  {

    private var mIsBound: Boolean
    private var mService: Messenger? = null
    private val mTimeout: Int
    private val mCallback: WdtCallback
    private val mContext: Context
    private val mId: String
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private val mMessenger = Messenger(IncomingHandler())

    init {
        mId = UUID.randomUUID().toString()
        mTimeout = timeout
        mCallback = callback
        mContext = context
        mIsBound = false
        doBindService()
    }

    /**
     * Handler of incoming messages from service.
     */
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_WDT_EXPIRED -> mCallback.onWdtExpired()
                else -> super.handleMessage(msg)
            }
        }
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = Messenger(service)

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                sendMessage(MSG_REGISTER_CLIENT, mTimeout)
            } catch (e: RemoteException) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }

        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null
        }
    }

    fun doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because there is no reason to be able to let other
        // applications replace our component.
        mContext.bindService(
            Intent(
                mContext,
                WdtService::class.java
            ), mConnection, Context.BIND_AUTO_CREATE
        )
        mIsBound = true
    }

    fun release() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    sendMessage(MSG_UNREGISTER_CLIENT)
                } catch (e: RemoteException) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }

            }

            // Detach our existing connection.
            mContext.unbindService(mConnection)
            mIsBound = false
        }
    }

    fun reset() {
        // Reset WDT counter
        sendMessage(MSG_RESET)
    }

    private fun obtainMessage(what: Int): Message {
        val message = Message.obtain()
        message.obj = mId
        message.what = what
        if (what == MSG_REGISTER_CLIENT || what == MSG_UNREGISTER_CLIENT)
          message.replyTo = mMessenger
        return message
    }

    private fun sendMessage(what: Int){
        mService!!.send(this.obtainMessage(what))
    }

    private fun sendMessage(what: Int, arg1: Int){
        val msg = obtainMessage(what)
        msg.arg1 = arg1
        mService!!.send(msg)
    }

}