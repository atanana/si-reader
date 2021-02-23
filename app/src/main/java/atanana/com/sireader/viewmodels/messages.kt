package atanana.com.sireader.viewmodels

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.appcompat.view.ActionMode
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
        else -> throw SiReaderException("Action $this has no text!")
    }
}

sealed class ToastMessage : Action()

data class ResourceToastMessage(@StringRes override val value: Int) : ToastMessage(), ResourceTextMessage

data class OpenFileMessage(val fileId: Int) : Action()

data class OpenPackMessage(val packId: Int) : Action()

sealed class ActionModeTitleMessage : Action()

data class StringActionModeTitleMessage(override val value: String) : ActionModeTitleMessage(), StringTextMessage

data class StartActionModeMessage(val callback: ActionMode.Callback) : Action()

object StopActionModeMessage : Action()

object ReadStoragePermissionExplanation : Action()

data class OpenFilePicker(val types: List<String>) : Action()