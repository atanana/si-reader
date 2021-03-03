package atanana.com.sireader.screens.fileinfo

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.databinding.FragmentFileBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.createViewModel
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.packspager.PacksPagerFragment
import atanana.com.sireader.viewmodels.Action
import atanana.com.sireader.viewmodels.OpenPackMessage
import atanana.com.sireader.views.optionalText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


private const val ARG_FILE_ID = "file_id"

@AndroidEntryPoint
class FileFragment : BaseFragment<FileViewModel>(R.layout.fragment_file) {

    @Inject
    lateinit var viewModelFactory: FileViewModel.Factory

    private var fileId: Int? = null

    private val packsAdapter = FileInfoAdapter { packId ->
        viewModel.onPackClick(packId)
    }

    private val binding by viewBinding(FragmentFileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fileId = arguments?.getInt(ARG_FILE_ID)
        viewModel = createViewModel {
            viewModelFactory.create(fileId!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fileInfo.layoutManager = LinearLayoutManager(activity)
        binding.fileInfo.adapter = packsAdapter

        viewModel.file.onEach { state ->
            packsAdapter.packs = state.packs
            bindFileInfo(state.file)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun bindFileInfo(file: QuestionFileEntity) {
        binding.header.fileTitle.text = file.title
        binding.header.fileName.text = file.filename
        binding.header.fileNotes.optionalText(file.notes)
        binding.header.fileEditors.optionalText(file.editor)
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
