package atanana.com.sireader.screens.pack

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.R
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.viewmodels.observe
import atanana.com.sireader.views.optionalText
import kotlinx.android.synthetic.main.fragment_pack.*

private const val ARG_PACK_ID = "pack_id"

class PackFragment : BaseFragment<PackViewModel>() {
    private var packId: Int? = null

    private val questionsAdapter = QuestionsAdapter { questionId ->
        viewModel.onQuestionClick(questionId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            packId = getInt(ARG_PACK_ID)
        }

        packId?.let {
            viewModel.loadPack(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pack, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pack.observe(this, { state ->
            updatePackInfo(state.pack)
            updateQuestions(state.questions)
        })

        questions_list.layoutManager = LinearLayoutManager(activity)
        questions_list.adapter = questionsAdapter
    }

    private fun updatePackInfo(pack: PackEntity) {
        pack_title.text = pack.topic
        pack_author.optionalText(pack.author)
        pack_notes.optionalText(pack.notes)
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