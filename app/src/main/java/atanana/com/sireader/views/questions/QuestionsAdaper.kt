package atanana.com.sireader.views.questions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionEntity

class QuestionsAdaper(
        private val answerClick: (questionId: Int) -> Unit
) : RecyclerView.Adapter<QuestionsAdaper.ViewHolder>() {
    var questions = emptyList<QuestionEntity>()
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

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val questionText = item.findViewById<TextView>(R.id.question_text)

        fun bind(question: QuestionEntity) {
            questionText.text = question.question
        }
    }
}