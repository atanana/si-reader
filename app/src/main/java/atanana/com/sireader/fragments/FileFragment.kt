package atanana.com.sireader.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.R
import atanana.com.sireader.viewmodels.*
import atanana.com.sireader.views.files.FileInfoAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_file.*
import javax.inject.Inject


private const val ARG_FILE_ID = "file_id"

class FileFragment : BaseFragment() {
    private var fileId: Int? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: FileViewModel

    private val packsAdapter = FileInfoAdapter { packId ->
        viewModel.onPackClick(packId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(viewModelFactory)

        arguments?.apply {
            fileId = getInt(ARG_FILE_ID)
        }

        fileId?.let {
            viewModel.loadFileInfo(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.file.observe(this, { state ->
            packsAdapter.packs = state.packs
            packsAdapter.info = state.file
        })

        viewModel.liveBus.observe(this, Observer { action ->
            when (action) {
                is OpenPack -> openPack(action.packId)
            }
        })

        file_info.layoutManager = LinearLayoutManager(activity)
        file_info.adapter = packsAdapter
    }

    private fun openPack(packId: Int) {
        fragmentManager?.openFragment(PacksPagerFragment.newInstance(fileId!!, packId))
    }

    override val transactionTag: String
        get() = TAG

    companion object {
        const val TAG = "FileFragment"

        @JvmStatic
        fun newInstance(fileId: Int) =
                FileFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_FILE_ID, fileId)
                    }
                }
    }
}
