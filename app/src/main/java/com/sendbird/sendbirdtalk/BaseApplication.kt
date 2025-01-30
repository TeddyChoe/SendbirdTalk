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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val APP_ID = "FEA2129A-EA73-4EB9-9E0B-EC738E7EB768"

class BaseApplication : Application() {
    companion object {
        internal val initState = MutableLiveData(InitState.NONE)
    }

    override fun onCreate() {
        super.onCreate()
        initUIKit()
        setupConfigurations()
    }

    private fun initUIKit() {
        SendbirdUIKit.init(object : SendbirdUIKitAdapter {
            override fun getAppId(): String {
                val appId = APP_ID
                Log.d("BaseApplication", "appId: $appId")
                return appId
            }

            override fun getAccessToken(): String {
                return ""
            }

            override fun getUserInfo(): UserInfo {
                return object : UserInfo {
                    override fun getUserId(): String =
                        runBlocking {
                            sendbirdTalkPreference.data.first()[PREFERENCE_KEY_USER_ID] ?: ""
                        }


                    override fun getNickname(): String =
                        runBlocking {
                            sendbirdTalkPreference.data.first()[PREFERENCE_KEY_NICKNAME] ?: ""
                        }


                    override fun getProfileUrl(): String =
                        runBlocking {
                            sendbirdTalkPreference.data.first()[PREFERENCE_KEY_PROFILE_URL] ?: ""
                        }
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
        }, this)
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