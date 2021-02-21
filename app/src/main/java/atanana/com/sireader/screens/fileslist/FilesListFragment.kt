package atanana.com.sireader.screens.fileslist

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import atanana.com.sireader.R
import atanana.com.sireader.databinding.FragmentFilesListBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.fileinfo.FileFragment
import atanana.com.sireader.viewmodels.*
import atanana.com.sireader.views.gone
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * A placeholder fragment containing a simple view.
 */
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(this) { state ->
            setViewState(state)
        }

        binding.fab.setOnClickListener {
            viewModel.fabClicked(requireActivity(), requestPermission)
        }

        binding.filesList.layoutManager = LinearLayoutManager(activity)
        binding.filesList.adapter = filesAdapter
    }

    override fun processMessage(message: Action) {
        when (message) {
            is OpenFileMessage -> openFile(message.fileId)
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
                    filesAdapter.files = emptyList()
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
                    filesAdapter.files = state.files
                }
            }
        }
    }

    private fun openFile(fileId: Int) {
        parentFragmentManager.openFragment(FileFragment.newInstance(fileId))
    }

    override val isToolbarVisible = true

    override val transactionTag: String
        get() = TAG

    companion object {
        const val TAG = "FilesListFragment"
    }
}
