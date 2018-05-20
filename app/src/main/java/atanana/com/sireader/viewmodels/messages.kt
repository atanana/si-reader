package atanana.com.sireader.viewmodels

import android.content.Intent
import android.content.res.Resources
import android.support.annotation.StringRes
import android.support.v7.view.ActionMode
import atanana.com.sireader.SiReaderException

sealed class Action

interface StringTextMessage {
    val value: String
}

interface ResourceTextMessage {
    @get:StringRes
    val value: Int
}

fun Action.text(resources: Resources): String {
    return when (this) {
        is StringTextMessage -> value
        is ResourceTextMessage -> resources.getString(value)
        else -> throw SiReaderException("Action ${this} has no text!")
    }
}

sealed class ToastMessage : Action()

data class StringToastMessage(override val value: String) : ToastMessage(), StringTextMessage

data class ResourceToastMessage(@StringRes override val value: Int) : ToastMessage(), ResourceTextMessage

data class ActivityForResultMessage(val intent: Intent, val requestCode: Int) : Action()

data class OpenFileMessage(val fileId: Int) : Action()

data class OpenPackMessage(val packId: Int) : Action()

sealed class TitleMessage : Action()

data class StringTitleMessage(override val value: String) : TitleMessage(), StringTextMessage

data class ResourceTitleMessage(@StringRes override val value: Int) : TitleMessage(), ResourceTextMessage

sealed class ActionModeTitleMessage : Action()

data class StringActionModeTitleMessage(override val value: String) : ActionModeTitleMessage(), StringTextMessage

data class ResourceActionModeTitleMessage(@StringRes override val value: Int) : ActionModeTitleMessage(), ResourceTextMessage

data class StartActionModeMessage(val callback: ActionMode.Callback) : Action()

object StopActionModeMessage : Action()