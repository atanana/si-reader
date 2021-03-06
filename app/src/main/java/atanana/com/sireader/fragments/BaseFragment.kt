package atanana.com.sireader.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import atanana.com.sireader.viewmodels.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes resId: Int) : Fragment(resId) {

    protected lateinit var viewModel: VM

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
                is Navigate -> {
                    findNavController().navigate(action.directions)
                }
                else -> processMessage(action)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    protected open fun processMessage(message: Action) {}
}