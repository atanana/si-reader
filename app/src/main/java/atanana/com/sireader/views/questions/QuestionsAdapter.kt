package atanana.com.sireader.views.questions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R
import atanana.com.sireader.viewmodels.QuestionViewModel
import atanana.com.sireader.views.gone

class QuestionsAdapter(
        private val onQuestionClick: (questionId: Int) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {
    var questions = emptyList<QuestionViewModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val questionText = item.findViewById<TextView>(R.id.question_text)
        private val answer = item.findViewById<TextView>(R.id.answer)

        fun bind(viewModel: QuestionViewModel) {
            questionText.text = viewModel.question.question
            answer.gone(viewModel.isClosed)
            answer.text = viewModel.question.answer

            itemView.setOnClickListener {
                if (viewModel.isClosed) {
                    onQuestionClick(viewModel.question.id)
                }
            }
        }
    }
}