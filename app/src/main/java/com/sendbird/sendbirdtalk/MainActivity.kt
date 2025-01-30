package com.sendbird.sendbirdtalk

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.sendbird.sendbirdtalk.prefrence.SENDBIRD_PREFERENCES_NAME
import com.sendbird.sendbirdtalk.prefrence.sendbirdTalkPreference
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = "teddy"
        getSharedPreferences("sendbird", MODE_PRIVATE).edit().putString("user_id", userId).apply()

        lifecycleScope.launch {
            startChatActivity()
        }
    }

    private suspend fun startChatActivity() {
        if (isSaveUserInfo()) {
//            startActivity(Intent(this, ChatActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private suspend fun isSaveUserInfo(): Boolean {
        // preference에 저장된 user_id가 있는지 확인
        val userId =
            sendbirdTalkPreference.edit { preference ->
                val currentCounterValue = preference[SENDBIRD_PREFERENCES_NAME] ?: 0
                preference[SENDBIRD_PREFERENCES_NAME] = "teddy"
            }
        return false
    }
}
