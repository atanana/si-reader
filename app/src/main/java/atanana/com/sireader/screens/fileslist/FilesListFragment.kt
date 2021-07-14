package atanana.com.sireader.screens.fileslist

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import atanana.com.sireader.R
import atanana.com.sireader.databinding.FragmentFilesListBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.createViewModel
import atanana.com.sireader.viewmodels.Action
import atanana.com.sireader.viewmodels.OpenFilePicker
import atanana.com.sireader.viewmodels.ReadStoragePermissionExplanation
import atanana.com.sireader.views.gone
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class FilesListFragment : BaseFragment<FilesListViewModel>(R.layout.fragment_files_list) {

    private val fileClickListener = object : FilesListAdapter.FileClickListener {
        override fun onClick(fileId: Int) {
            viewModel.onFileClick(fileId)
        }

        override fun onLongClick(fileId: Int) {
            viewModel.onLongFileClick(fileId)
        }
    }

    private val filesAdapter = FilesListAdapter(fileClickListener)

    private val binding by viewBinding(FragmentFilesListBinding::bind)

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        viewModel.onPermissionResult(isGranted)
    }

    private val requestFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        viewModel.processFile(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            viewModel.fabClicked(requireActivity(), requestPermission)
        }

        binding.filesList.setHasFixedSize(true)
        binding.filesList.layoutManager = LinearLayoutManager(activity)
        binding.filesList.adapter = filesAdapter

        viewModel.state.onEach { state -> setViewState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    override fun processMessage(message: Action) {
        when (message) {
            ReadStoragePermissionExplanation -> showReadStorageExplanation()
            is OpenFilePicker -> requestFile.launch(message.types.toTypedArray())
            else -> Unit
        }
    }

    private fun showReadStorageExplanation() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.read_storage_explanation)
            .setPositiveButton(R.string.allow_permission) { _, _ -> viewModel.requestReadStorage(requestPermission) }
            .setNegativeButton(R.string.deny_permission) { _, _ -> }
            .create()
            .show()
    }

    private fun setViewState(state: FilesListViewState) {
        with(binding) {
            when (state) {
                NoFiles -> {
                    noFilesLabel.gone(false)
                    loadingFiles.gone(true)
                    filesList.gone(true)
                    filesAdapter.submitList(emptyList())
                }
                Loading -> {
                    noFilesLabel.gone(true)
                    loadingFiles.gone(false)
                    filesList.gone(true)
                }
                is Files -> {
                    noFilesLabel.gone(true)
                    loadingFiles.gone(true)
                    filesList.gone(false)
                    filesAdapter.submitList(state.files)
                }
            }
        }
    }
}
