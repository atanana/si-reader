package atanana.com.sireader.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.lifecycleScope
import atanana.com.sireader.MainActivity
import atanana.com.sireader.viewmodels.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes resId: Int, vmClass: KClass<VM>) : Fragment(resId) {
    abstract val transactionTag: String

    protected val viewModel: VM by createViewModelLazy(vmClass, { viewModelStore }, null)

    private var actionMode: ActionMode? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.bus.onEach { action ->
            when (action) {
                is ToastMessage -> {
                    Toast.makeText(activity, action.text(resources), Toast.LENGTH_SHORT).show()
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
                else -> processMessage(action)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        (context as? MainActivity)?.setToolbarVisibility(isToolbarVisible)
    }

    protected open fun processMessage(message: Action) {}

    open val isToolbarVisible: Boolean = false
}