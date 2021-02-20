package atanana.com.sireader.screens.fileinfo

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import atanana.com.sireader.R
import atanana.com.sireader.databinding.FragmentFileBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.packspager.PacksPagerFragment
import atanana.com.sireader.viewmodels.Action
import atanana.com.sireader.viewmodels.OpenPackMessage
import atanana.com.sireader.viewmodels.observe
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


private const val ARG_FILE_ID = "file_id"

class FileFragment : BaseFragment<FileViewModel>(R.layout.fragment_file) {
    private var fileId: Int? = null

    private val packsAdapter = FileInfoAdapter { packId ->
        viewModel.onPackClick(packId)
    }

    private val binding by viewBinding(FragmentFileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            fileId = getInt(ARG_FILE_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.file.observe(this) { state ->
            packsAdapter.packs = state.packs
            packsAdapter.info = state.file
        }

        binding.fileInfo.layoutManager = LinearLayoutManager(activity)
        binding.fileInfo.adapter = packsAdapter
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
            else -> Unit
        }
    }

    private fun openPack(packId: Int) {
        parentFragmentManager.openFragment(PacksPagerFragment.newInstance(fileId!!, packId))
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
