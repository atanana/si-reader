package atanana.com.sireader.views

import android.view.View
import android.widget.TextView

fun View.gone(isGone: Boolean) {
    visibility = if (isGone) View.GONE else View.VISIBLE
}

fun TextView.optionalText(text: String?) {
    gone(text.isNullOrBlank())
    this.text = text
}