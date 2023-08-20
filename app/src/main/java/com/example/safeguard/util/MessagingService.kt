package com.example.safeguard.util

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("testtt", "FCM token created: $token")
    }

    fun getToken(): String?{
        var token: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if(!task.isSuccessful){
                Log.w("testtt", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
            Log.d("testtt", "FCM token: $token")
        })
        return token
    }
}