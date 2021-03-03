package com.thepianodentist.toastnotificationapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.thepianodentist.toastnotificationapp.api.Retriever
import com.thepianodentist.toastnotificationapp.data.PostUserRequestBody
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val retriever = Retriever()
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bundle = intent.extras
        if (bundle != null) {
            //text_view_notification.text = bundle.getString("text")
        }
        if (!checkGooglePlayServices()){
            Log.e(TAG, "FUDGE!!!")
        }
        Log.e(TAG, "in oncreate")

    }
    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            //text_view_notification.text = intent.extras?.getString("message")
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        // 1
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        // 2
        return if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Error")
            // ask user to update google play services and manage the error.
            false
        } else {
            // 3
            Log.i(TAG, "Google play services updated")
            true
        }
    }

    fun getToken(v: View){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                // 2
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // 3
                val msg = task.result?:"cant find"

                // 4
                Log.i(TAG, msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
            })
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "in onstart")
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("MyData"))
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                // 2
                Log.e(TAG, "in token oncomplete")
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // 3
                token = task.result?:"cant find"

                // 4
                Log.i(TAG, token)
                Toast.makeText(baseContext, token, Toast.LENGTH_LONG).show()
                val errorHandler = CoroutineExceptionHandler{_, exception ->
                    Log.e(TAG, "fudge", exception)
//                AlertDialog.Builder(this).setTitle("Error")
//                    .setMessage(exception.message)
//                    .setPositiveButton("ok"){_, _ ->}
//                    .show()
                }
                CoroutineScope(Job() + Dispatchers.IO).launch(errorHandler){
                    retriever.postUser(PostUserRequestBody(token))
                }
            })
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    fun btnClick(v: View){
//        val errorHandler = CoroutineExceptionHandler{_, exception ->
//            Log.e(TAG, "fudge", exception)
//        }
//        CoroutineScope(Job() + Dispatchers.IO).launch(errorHandler){
//            retriever.postUser(PostUserRequestBody("aaaabbbb"))
//        }
    }

    // TODO: Add a method for receiving notifications

    // TODO: Add a function to check for Google Play Services

    // TODO: Create a message receiver constant

    companion object {
        private const val TAG = "MainActivity"
    }
}