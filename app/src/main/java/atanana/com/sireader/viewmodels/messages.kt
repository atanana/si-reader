package atanana.com.sireader.viewmodels

import android.content.Intent
import android.support.annotation.StringRes

sealed class Action

sealed class TextMessage : Action()

data class StringTextMessage(val text: String) : TextMessage()

data class ResourceTextMessage(@StringRes val textId: Int) : TextMessage()

data class ActivityForResultMessage(val intent: Intent, val requestCode: Int) : Action()

data class OpenPack(val packId: Int) : Action()