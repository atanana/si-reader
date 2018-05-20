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
                is ToastMessage -> {
                    Toast.makeText(activity, action.text(resources), Toast.LENGTH_SHORT).show()
                }
                is ActivityForResultMessage -> {
                    startActivityForResult(action.intent, action.requestCode)
                }
                is TitleMessage -> {
                    activity?.title = action.text(resources)
                }
                is StartActionModeMessage -> {
                    actionMode = (activity as? AppCompatActivity)?.startSupportActionMode(action.callback)
                }
                is StopActionModeMessage -> {
                    actionMode?.finish()
                }
                is ActionModeTitleMessage -> {
                    actionMode?.title = action.text(resources)
                }
                else -> processMessage(action!!)
            }
        })
    }

    protected open fun processMessage(message: Action) {}
}