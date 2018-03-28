package atanana.com.sireader.files

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import javax.inject.Inject

const val OPEN_FILE_REQUEST_CODE = 100

class OpenFileHandler @Inject constructor(private val context: Context) {
    private val docType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc")

    fun openFileIntent(): Intent? {
        val intent = createIntent()
        return if (intent.resolveActivity(context.packageManager) != null) {
            intent
        } else {
            null
        }
    }

    private fun createIntent(): Intent {
        return Intent()
                .setAction(Intent.ACTION_GET_CONTENT)
                .setType(docType)
    }

    fun getUri(data: Intent): Uri {
        return data.data
    }
}