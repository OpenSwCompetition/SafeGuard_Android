package com.example.safeguard.ui.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.safeguard.R
import com.example.safeguard.databinding.ActivitySignUpBinding
import com.example.safeguard.util.binding.BindingActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SignUpActivity : BindingActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var phoneNumber: String
    private var isCodeSend: Boolean = false
    private var isCertified: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.sendButton.setOnClickListener {
            phoneNumber = binding.phoneNumber.text.toString()
            if(isValidNumber(phoneNumber)){
                sendVerificationCode("+82${phoneNumber.substring(1)}")
            }else{
                Toast.makeText(this, "올바른 휴대폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.certificationButton.setOnClickListener {
            if(isCodeSend){
                verifyCode(binding.certificationNumber.text.toString())
            }else{
                Toast.makeText(this, "인증번호를 전송해주세요", Toast.LENGTH_SHORT).show()
            }
        }

//        binding.completeButton.setOnClickListener {
//
//        }

        binding.birth.setOnClickListener {
            initBirth()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    isCertified = true
                    Toast.makeText(this, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initBirth(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dateString = "${year}년 ${month+1}월 ${day}일"
            binding.birth.apply {
                text = dateString
                isSelected = true
//                age = calculateAge(year, month+1, day)
            }
        }
        DatePickerDialog(this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).
        show()
    }

    private fun isValidNumber(number: String) = when(number.length){
        11 -> true
        else -> false
    }

    private fun isValidPassword(password: String, passwordCheck: String): Boolean = password==passwordCheck

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
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.d("testtt", e.toString())
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    isCodeSend = true
                    Toast.makeText(this@SignUpActivity, "인증번호를 전송했습니다", Toast.LENGTH_SHORT).show()
                    this@SignUpActivity.verificationId = verificationId
                }
            })
    }
}