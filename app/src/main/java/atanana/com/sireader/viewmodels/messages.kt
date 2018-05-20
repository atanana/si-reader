package atanana.com.sireader.viewmodels

import android.content.Intent
import android.support.annotation.StringRes
import android.support.v7.view.ActionMode

sealed class Action

sealed class ToastMessage : Action()

data class StringToastMessage(val text: String) : ToastMessage()

data class ResourceToastMessage(@StringRes val textId: Int) : ToastMessage()

data class ActivityForResultMessage(val intent: Intent, val requestCode: Int) : Action()

data class OpenFileMessage(val fileId: Int) : Action()

data class OpenPackMessage(val packId: Int) : Action()

sealed class TitleMessage : Action()

data class StringTitleMessage(val text: String) : TitleMessage()

data class ResourceTitleMessage(@StringRes val titleId: Int) : TitleMessage()

sealed class ActionModeTitleMessage : Action()

data class StringActionModeTitleMessage(val text: String) : ActionModeTitleMessage()

data class ResourceActionModeTitleMessage(@StringRes val titleId: Int) : ActionModeTitleMessage()

data class StartActionModeMessage(val callback: ActionMode.Callback) : Action()

object StopActionModeMessage : Action()