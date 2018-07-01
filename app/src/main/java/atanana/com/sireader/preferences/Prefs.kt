package atanana.com.sireader.preferences

import android.content.Context
import android.content.SharedPreferences

private const val PREFERENCES_NAME = "si-reader"
private const val KEY_LAST_READ_FILE = "KEY_LAST_READ_FILE"

class Prefs(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private fun write(operation: SharedPreferences.Editor.() -> Unit) {
        val editor = preferences.edit()
        editor.operation()
        editor.apply()
    }

    private fun write(key: String, value: Int) {
        write { putInt(key, value) }
    }

    var lastReadFile: Int
        get() = preferences.getInt(KEY_LAST_READ_FILE, -1)
        set(value) {
            write(KEY_LAST_READ_FILE, value)
        }
}