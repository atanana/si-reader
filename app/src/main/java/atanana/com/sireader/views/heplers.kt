package atanana.com.sireader.views

import android.view.View

fun View.gone(isGone: Boolean) {
    visibility = if (isGone) View.GONE else View.VISIBLE
}