package atanana.com.sireader.screens.fileslist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import atanana.com.sireader.R
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.fileinfo.FileFragment
import atanana.com.sireader.viewmodels.*
import atanana.com.sireader.views.gone
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_files_list.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class FilesListFragment : BaseFragment<FilesListViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var rxPermissions: RxPermissions

    override lateinit var viewModel: FilesListViewModel

    private val fileClickListener = object : FilesListAdapter.FileClickListener {
        override fun onClick(fileId: Int) {
            viewModel.onFileClick(fileId)
        }

        override fun onLongClick(fileId: Int) {
            viewModel.onLongFileClick(fileId)
        }
    }

    private val filesAdapter = FilesListAdapter(fileClickListener)

    private var isSelectionMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(viewModelFactory)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(this, { state ->
            setViewState(state)
        })

        fab.setOnClickListener {
            viewModel.fabClicked(rxPermissions)
        }

        files_list.layoutManager = LinearLayoutManager(activity)
        files_list.adapter = filesAdapter
    }

    override fun processMessage(message: Action) {
        when (message) {
            is OpenFileMessage -> openFile(message.fileId)
            is SelectionModeChangeMessage -> onSelectionModeChange(message.value)
        }
    }

    private fun onSelectionModeChange(value: Boolean) {
        isSelectionMode = value
        activity?.invalidateOptionsMenu()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    private fun setViewState(state: FilesListViewState) {
        when (state) {
            NoFiles -> {
                no_files_label.gone(false)
                loading_files.gone(true)
                files_list.gone(true)
                filesAdapter.files = emptyList()
            }
            Loading -> {
                no_files_label.gone(true)
                loading_files.gone(false)
                files_list.gone(true)
            }
            is Files -> {
                no_files_label.gone(true)
                loading_files.gone(true)
                files_list.gone(false)
                filesAdapter.files = state.files
            }
        }
    }

    private fun openFile(fileId: Int) {
        fragmentManager?.openFragment(FileFragment.newInstance(fileId))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_files, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.action_delete)?.isVisible = isSelectionMode
    }

    override val transactionTag: String
        get() = TAG

    companion object {
        const val TAG = "FilesListFragment"
    }
}
