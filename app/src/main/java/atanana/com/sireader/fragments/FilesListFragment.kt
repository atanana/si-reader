package atanana.com.sireader.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.R
import atanana.com.sireader.viewmodels.*
import atanana.com.sireader.views.files.FilesListAdapter
import atanana.com.sireader.views.gone
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_files_list.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class FilesListFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: FilesListViewModel

    private val filesAdapter = FilesListAdapter { fileId ->
        viewModel.onFileClick(fileId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(this, { state ->
            setViewState(state)
        })

        viewModel.liveBus.observe(this, Observer { action ->
            when (action) {
                is OpenFile -> openFile(action.fileId)
            }
        })

        files_list.layoutManager = LinearLayoutManager(activity)
        files_list.adapter = filesAdapter
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

    override val transactionTag: String
        get() = TAG

    companion object {
        const val TAG = "FilesListFragment"
    }
}
