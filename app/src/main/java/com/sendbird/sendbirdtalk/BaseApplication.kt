package com.sendbird.sendbirdtalk

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.sendbirdtalk.prefrence.PREFERENCE_KEY_NICKNAME
import com.sendbird.sendbirdtalk.prefrence.PREFERENCE_KEY_PROFILE_URL
import com.sendbird.sendbirdtalk.prefrence.PREFERENCE_KEY_USER_ID
import com.sendbird.sendbirdtalk.prefrence.sendbirdTalkPreference
import com.sendbird.uikit.SendbirdUIKit
import com.sendbird.uikit.adapter.SendbirdUIKitAdapter
import com.sendbird.uikit.consts.ReplyType
import com.sendbird.uikit.consts.ThreadReplySelectType
import com.sendbird.uikit.consts.TypingIndicatorType
import com.sendbird.uikit.interfaces.UserInfo
import com.sendbird.uikit.model.configurations.UIKitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val APP_ID = "FEA2129A-EA73-4EB9-9E0B-EC738E7EB768"

class BaseApplication : Application() {
    private var _userId: String = ""
    private var _nickname: String = ""
    private var _profileUrl: String = ""

    companion object {
        internal val initState = MutableLiveData(InitState.NONE)
    }

    override fun onCreate() {
        super.onCreate()
        collectDataStore()
        initUIKit()
        setupConfigurations()
    }

    private fun initUIKit() {
        SendbirdUIKit.init(
            object : SendbirdUIKitAdapter {
                override fun getAppId(): String {
                    val appId = APP_ID
                    Log.d("BaseApplication", "appId: $appId")
                    return appId
                }

                /**
                 * If return null, login to guest mode that restricted to use some features.
                 */
                override fun getAccessToken(): String {
                    return ""
                }

                override fun getUserInfo(): UserInfo {
                    return object : UserInfo {
                        override fun getUserId(): String = _userId

                        override fun getNickname(): String = _nickname

                        override fun getProfileUrl(): String = _profileUrl
                    }
                }

                override fun getInitResultHandler(): InitResultHandler {
                    return object : InitResultHandler {
                        override fun onMigrationStarted() {
                            initState.value = InitState.MIGRATING
                        }

                        override fun onInitFailed(e: SendbirdException) {
                            initState.value = InitState.FAILED
                        }

                        override fun onInitSucceed() {
                            initState.value = InitState.SUCCEED
                        }
                    }
                }
            },
            this,
        )
    }
    
    private fun collectDataStore() {
        CoroutineScope(Dispatchers.Main).launch {
            sendbirdTalkPreference.data.map {
                _userId = it[PREFERENCE_KEY_USER_ID] ?: ""
            }.collect()

            sendbirdTalkPreference.data.map {
                _nickname = it[PREFERENCE_KEY_NICKNAME] ?: ""
            }.collect()

            sendbirdTalkPreference.data.map {
                _profileUrl = it[PREFERENCE_KEY_PROFILE_URL] ?: ""
            }.collect()
        }
    }

    fun initStateChanges(): LiveData<InitState> {
        return initState
    }

    /**
     * In a sample app, different contextual settings are used in a single app.
     * These are only used in the sample, because if the app kills and resurrects due to low memory, the last used sample settings should be preserved.
     */
    private fun setupConfigurations() {
        // set whether to use user profile
        UIKitConfig.common.enableUsingDefaultUserProfile = true
        // set whether to use typing indicators in channel list
        UIKitConfig.groupChannelListConfig.enableTypingIndicator = true
        // set whether to use read/delivery receipt in channel list
        UIKitConfig.groupChannelListConfig.enableMessageReceiptStatus = true
        // set whether to use user mention
        UIKitConfig.groupChannelConfig.enableMention = true
        // set reply type
        UIKitConfig.groupChannelConfig.replyType = ReplyType.THREAD
        UIKitConfig.groupChannelConfig.threadReplySelectType = ThreadReplySelectType.THREAD
        // set whether to use voice message
        UIKitConfig.groupChannelConfig.enableVoiceMessage = true
        // set typing indicator types
        UIKitConfig.groupChannelConfig.typingIndicatorTypes =
            setOf(TypingIndicatorType.BUBBLE, TypingIndicatorType.TEXT)
        // set whether to use feedback
        UIKitConfig.groupChannelConfig.enableFeedback = true
    }
}
