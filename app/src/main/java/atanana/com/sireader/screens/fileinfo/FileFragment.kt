package atanana.com.sireader.screens.fileinfo

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.R
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.packspager.PacksPagerFragment
import atanana.com.sireader.viewmodels.Action
import atanana.com.sireader.viewmodels.OpenPackMessage
import atanana.com.sireader.viewmodels.observe
import kotlinx.android.synthetic.main.fragment_file.*


private const val ARG_FILE_ID = "file_id"

class FileFragment : BaseFragment<FileViewModel>() {
    private var fileId: Int? = null

    private val packsAdapter = FileInfoAdapter { packId ->
        viewModel.onPackClick(packId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            fileId = getInt(ARG_FILE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.file.observe(this) { state ->
            packsAdapter.packs = state.packs
            packsAdapter.info = state.file
        }

        file_info.layoutManager = LinearLayoutManager(activity)
        file_info.adapter = packsAdapter
    }

    override fun onResume() {
        super.onResume()
        fileId?.let {
            viewModel.loadFileInfo(it)
        }
    }

    override fun processMessage(message: Action) {
        when (message) {
            is OpenPackMessage -> openPack(message.packId)
        }
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
