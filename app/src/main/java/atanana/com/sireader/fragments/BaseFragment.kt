package atanana.com.sireader.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.View
import android.widget.Toast
import atanana.com.sireader.viewmodels.*

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    abstract val transactionTag: String

    protected abstract val viewModel: VM

    private var actionMode: ActionMode? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveBus.observe(this, Observer { action ->
            when (action) {
                is ToastMessage -> processTextMessage(action)
                is ActivityForResultMessage -> startActivityForResult(action.intent, action.requestCode)
                is TitleMessage -> processTitleMessage(action)
                is StartActionModeMessage -> actionMode = (activity as? AppCompatActivity)?.startSupportActionMode(action.callback)
                is StopActionModeMessage -> actionMode?.finish()
                is ActionModeTitleMessage -> processActionModeTitleMessage(action)
                else -> processMessage(action!!)
            }
        })
    }

    protected open fun processMessage(message: Action) {}

    //todo refactor these methods
    private fun processTitleMessage(message: TitleMessage) {
        val text = when (message) {
            is StringTitleMessage -> message.text
            is ResourceTitleMessage -> getString(message.titleId)
        }
        activity?.title = text
    }

    private fun processTextMessage(message: ToastMessage) {
        val text = when (message) {
            is StringToastMessage -> message.text
            is ResourceToastMessage -> getString(message.textId)
        }
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    private fun processActionModeTitleMessage(message: ActionModeTitleMessage) {
        val text = when (message) {
            is StringActionModeTitleMessage -> message.text
            is ResourceActionModeTitleMessage -> getString(message.titleId)
        }
        actionMode?.title = text
    }
}