package atanana.com.sireader.screens.pack

import android.graphics.Typeface
import android.support.annotation.StringRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R
import atanana.com.sireader.views.gone

class QuestionsAdapter(
        private val onQuestionClick: (questionId: Int) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {
    var questions = emptyList<QuestionItem>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(DiffCallback(field, value), false)
            field = value
            diffResult.dispatchUpdatesTo(this)
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
        private val alsoAnswer = item.findViewById<TextView>(R.id.also_answer)
        private val notAnswer = item.findViewById<TextView>(R.id.not_answer)
        private val comment = item.findViewById<TextView>(R.id.comment)
        private val reference = item.findViewById<TextView>(R.id.reference)
        private val answerLayout = item.findViewById<View>(R.id.answer_layout)

        fun bind(item: QuestionItem) {
            questionText.text = createQuestionText(item)
            answerLayout.gone(item.isClosed)
            answer.safePrepend(item.question.answer, R.string.prefix_answer)
            alsoAnswer.safePrepend(item.question.alsoAnswer, R.string.prefix_also_answer)
            notAnswer.safePrepend(item.question.notAnswer, R.string.prefix_not_answer)
            comment.safePrepend(item.question.comment, R.string.prefix_comment)
            reference.safePrepend(item.question.reference, R.string.prefix_reference)

            itemView.setOnClickListener {
                if (item.isClosed) {
                    onQuestionClick(item.question.id)
                }
            }
        }
    }

    private fun createQuestionText(item: QuestionItem): Spannable =
            SpannableStringBuilder().apply {
                val price = item.price + ". "
                append(price)
                setSpan(StyleSpan(Typeface.BOLD), 0, price.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                append(item.question.question)
            }

    private fun TextView.safePrepend(value: String?, @StringRes prefixId: Int) {
        gone(value == null)
        value?.let {
            text = context.resources.getString(prefixId, it)
        }
    }
}

private class DiffCallback(
        private val oldQuestions: List<QuestionItem>,
        private val newQuestion: List<QuestionItem>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldQuestions[oldItemPosition].question.id == newQuestion[newItemPosition].question.id
    }

    override fun getOldListSize(): Int = oldQuestions.size

    override fun getNewListSize(): Int = newQuestion.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldQuestions[oldItemPosition] == newQuestion[newItemPosition]
    }
}