package com.sendbird.sendbirdtalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.sendbird.sendbirdtalk.databinding.ActivityLoginBinding
import com.sendbird.sendbirdtalk.prefrence.PREFERENCE_KEY_USER_ID
import com.sendbird.sendbirdtalk.prefrence.sendbirdTalkPreference
import com.sendbird.uikit.SendbirdUIKit
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.etEmail.addTextChangedListener(
            afterTextChanged = {
                viewModel.edit(it.toString())
                checkValidEmail(it.toString())
            }
        )

        binding.btLogin.setOnClickListener {
            lifecycleScope.launch {
                sendbirdTalkPreference.edit { preference ->
                    preference[PREFERENCE_KEY_USER_ID] = viewModel.userId.value
                }

                SendbirdUIKit.connect { user, e ->
                    if (e != null) {
                        Toast.makeText(this@LoginActivity, "$e", Toast.LENGTH_SHORT).show()
                        Log.e("LoginActivity", "SendbirdUIKit.connect() failed with error: $e")
                        return@connect
                    }

                    if (user != null) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        }

        binding.btQrLogin.setOnClickListener {
            //TODO: QR 로그인 지원
        }

        binding.cbAutoLogin.setOnCheckedChangeListener { buttonView, isChecked ->
            //TODO: 자동 로그인 프리퍼런스에 값 입력
        }
    }

    private fun checkValidEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        binding.btLogin.isEnabled = email.matches(emailPattern.toRegex())
    }

    fun checkValidPassword(password: String) {
    }
}