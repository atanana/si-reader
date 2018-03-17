package atanana.com.sireader.files

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import javax.inject.Inject

const val OPEN_FILE_REQUEST_CODE = 100

class OpenFileHandler @Inject constructor(private val context: Context) {
    fun openFileIntent(): Intent? {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        val file = Environment.getExternalStorageDirectory()
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc")
        intent.setDataAndType(Uri.fromFile(file), type)
        return if (intent.resolveActivity(context.packageManager) != null) {
            intent
        } else {
            null
        }
    }
}