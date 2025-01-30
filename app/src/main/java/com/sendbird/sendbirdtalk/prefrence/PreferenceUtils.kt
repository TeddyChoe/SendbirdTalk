package com.sendbird.sendbirdtalk.prefrence

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

const val PREFERENCE_KEY_BOT_ID = "PREFERENCE_KEY_BOT_ID"
const val PREFERENCE_KEY_THEME_MODE = "PREFERENCE_KEY_THEME_MODE"
const val PREFERENCE_KEY_DO_NOT_DISTURB = "PREFERENCE_KEY_DO_NOT_DISTURB"
const val PREFERENCE_KEY_LATEST_USED_SAMPLE = "PREFERENCE_KEY_LATEST_USED_SAMPLE"
const val PREFERENCE_KEY_NOTIFICATION_USE_FEED_CHANNEL_ONLY = "PREFERENCE_KEY_NOTIFICATION_USE_FEED_CHANNEL_ONLY"
const val PREFERENCE_KEY_REGION = "PREFERENCE_KEY_REGION"

val Context.sendbirdTalkPreference by preferencesDataStore(
    name = "sendbird_talk_preferences"
)

val SENDBIRD_PREFERENCES_NAME = stringPreferencesKey("sendbird_preferences_name")
val PREFERENCE_KEY_APP_ID = stringPreferencesKey("preference_key_app_id")
val PREFERENCE_KEY_USER_ID = stringPreferencesKey("preference_key_user_id")
val PREFERENCE_KEY_NICKNAME = stringPreferencesKey("preference_key_nickname")
val PREFERENCE_KEY_PROFILE_URL = stringPreferencesKey("preference_key_profile_url")

