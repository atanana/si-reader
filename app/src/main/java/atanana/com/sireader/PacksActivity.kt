package atanana.com.sireader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_packs.*
import android.webkit.MimeTypeMap
import android.widget.Toast


class PacksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packs)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            val file = Environment.getExternalStorageDirectory()
            val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc")
            intent.setDataAndType(Uri.fromFile(file), type)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.no_file_managers_installed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_packs, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
