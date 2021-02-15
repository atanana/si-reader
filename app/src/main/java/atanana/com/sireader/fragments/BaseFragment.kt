package atanana.com.sireader.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import atanana.com.sireader.viewmodels.*
import dagger.android.support.AndroidSupportInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    abstract val transactionTag: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        val viewModelClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.first()
        @Suppress("UNCHECKED_CAST")
        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass as Class<VM>]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveBus.observe(viewLifecycleOwner, Observer { action ->
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