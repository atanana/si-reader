package atanana.com.sireader.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import atanana.com.sireader.viewmodels.*

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    abstract val transactionTag: String

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveBus.observe(this, Observer { action ->
            when (action) {
                is TextMessage -> processTextMessage(action)
                is ActivityForResultMessage -> startActivityForResult(action.intent, action.requestCode)
                is TitleMessage -> processTitleMessage(action)
                else -> processMessage(action!!)
            }
        })
    }

    protected open fun processMessage(message: Action) {}

    private fun processTitleMessage(message: TitleMessage) {
        val text = when (message) {
            is StringTitleMessage -> message.text
            is ResourceTitleMessage -> getString(message.titleId)
        }
        activity?.title = text
    }

    private fun processTextMessage(message: TextMessage) {
        val text = when (message) {
            is StringTextMessage -> message.text
            is ResourceTextMessage -> getString(message.textId)
        }
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}