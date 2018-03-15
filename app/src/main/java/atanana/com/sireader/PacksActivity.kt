package atanana.com.sireader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_packs.*
import javax.inject.Inject


class PacksActivity : AppCompatActivity() {
    @Inject
    lateinit var openFileHandler: OpenFileHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packs)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = openFileHandler.openFileIntent()
            if (intent != null) {
                startActivityForResult(intent, OPEN_FILE_REQUEST_CODE)
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
