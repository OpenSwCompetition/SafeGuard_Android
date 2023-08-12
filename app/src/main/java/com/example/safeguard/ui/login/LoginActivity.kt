package com.example.safeguard.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.safeguard.R
import com.example.safeguard.databinding.ActivityLoginBinding
import com.example.safeguard.util.binding.BindingActivity
import com.example.safeguard.ui.MainActivity
import com.example.safeguard.ui.dialog.FinishDialog
import com.example.safeguard.ui.signup.SignUpActivity

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = FinishDialog(this@LoginActivity)
            dialog.initDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this, callback)


        binding.loginButton.setOnClickListener {
            Intent(this@LoginActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.signUpButton.setOnClickListener {
            Intent(this@LoginActivity, SignUpActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}