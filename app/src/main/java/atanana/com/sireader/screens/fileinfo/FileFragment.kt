package atanana.com.sireader.screens.fileinfo

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.databinding.FragmentFileBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.createViewModel
import atanana.com.sireader.viewmodels.Action
import atanana.com.sireader.viewmodels.OpenPackMessage
import atanana.com.sireader.views.optionalText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class FileFragment : BaseFragment<FileViewModel>(R.layout.fragment_file) {

    @Inject
    lateinit var viewModelFactory: FileViewModel.Factory

    private val args: FileFragmentArgs by navArgs()

    private val packsAdapter = FileInfoAdapter { packId ->
        viewModel.onPackClick(packId)
    }

    private val binding by viewBinding(FragmentFileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = createViewModel {
            viewModelFactory.create(args.fileId)
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
        val direction = FileFragmentDirections.openPack(args.fileId, packId)
        findNavController().navigate(direction)
    }
}
