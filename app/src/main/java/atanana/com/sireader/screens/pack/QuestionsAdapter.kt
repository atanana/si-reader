package atanana.com.sireader.screens.pack

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import atanana.com.sireader.R
import atanana.com.sireader.databinding.ItemQuestionBinding
import atanana.com.sireader.views.gone

class QuestionsAdapter(
    private val onQuestionClick: (questionId: Int) -> Unit
) : ListAdapter<QuestionItem, QuestionsAdapter.ViewHolder>(Callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuestionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionItem) {
            binding.questionText.text = createQuestionText(item)
            binding.answerLayout.gone(item.isClosed)
            binding.answer.safePrepend(item.question.answer, R.string.prefix_answer)
            binding.alsoAnswer.safePrepend(item.question.alsoAnswer, R.string.prefix_also_answer)
            binding.notAnswer.safePrepend(item.question.notAnswer, R.string.prefix_not_answer)
            binding.comment.safePrepend(item.question.comment, R.string.prefix_comment)
            binding.reference.safePrepend(item.question.reference, R.string.prefix_reference)

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

private object Callback : DiffUtil.ItemCallback<QuestionItem>() {

    override fun areItemsTheSame(oldItem: QuestionItem, newItem: QuestionItem): Boolean =
        oldItem.question.id == newItem.question.id

    override fun areContentsTheSame(oldItem: QuestionItem, newItem: QuestionItem): Boolean =
        oldItem == newItem
}