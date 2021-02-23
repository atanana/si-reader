package atanana.com.sireader.screens.pack

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import atanana.com.sireader.R
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.databinding.FragmentPackBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.views.optionalText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val ARG_PACK_ID = "pack_id"

@AndroidEntryPoint
class PackFragment : BaseFragment<PackViewModel>(R.layout.fragment_pack) {
    private var packId: Int? = null

    private val questionsAdapter = QuestionsAdapter { questionId ->
        viewModel.onQuestionClick(questionId)
    }

    private val binding by viewBinding(FragmentPackBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            packId = getInt(ARG_PACK_ID)
        }

        packId?.let {
            viewModel.loadPack(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.questionsList.layoutManager = LinearLayoutManager(activity)
        binding.questionsList.adapter = questionsAdapter

        viewModel.pack.onEach { state ->
            state ?: return@onEach
            updatePackInfo(state.pack)
            updateQuestions(state.questions)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updatePackInfo(pack: PackEntity) {
        binding.packTitle.text = pack.indexedTitle
        binding.packAuthor.optionalText(pack.author)
        binding.packNotes.optionalText(pack.notes)
    }

    private fun updateQuestions(questions: List<QuestionItem>) {
        questionsAdapter.questions = questions
    }

    override val transactionTag: String
        get() = TAG

    companion object {
        const val TAG = "PackFragment"

        @JvmStatic
        fun newInstance(packId: Int) =
            PackFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PACK_ID, packId)
                }
            }
    }
}