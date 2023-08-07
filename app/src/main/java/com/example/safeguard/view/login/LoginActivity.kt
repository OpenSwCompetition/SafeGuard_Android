package com.example.safeguard.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.safeguard.R
import com.example.safeguard.databinding.ActivityLoginBinding
import com.example.safeguard.util.binding.BindingActivity
import com.example.safeguard.view.MainActivity
import com.example.safeguard.view.dialog.FinishDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = FinishDialog(this@LoginActivity)
            dialog.initDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this, callback)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            Intent(this@LoginActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
        binding.signUpButton.setOnClickListener {
            val phoneNumber = binding.phoneNumberText.text.toString()
            sendVerificationCode("+82${phoneNumber.substring(1)}")
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                } else {
                }
            }
    }

    private fun isValidNumber(number: String) = when(number.length){
        10 -> true
        else -> false
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun sendVerificationCode(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // 자동 인증되었을 때의 동작 (이미 전화번호 인증을 받은 경우)
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.d("testtt", e.toString())
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d("testtt", verificationId)
                    this@LoginActivity.verificationId = verificationId
                }
            })
    }
}