package atanana.com.sireader.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.View
import android.widget.Toast
import atanana.com.sireader.viewmodels.*
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    abstract val transactionTag: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    private var actionMode: ActionMode? = null

    protected abstract val viewModelClass: Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[viewModelClass]
    }

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