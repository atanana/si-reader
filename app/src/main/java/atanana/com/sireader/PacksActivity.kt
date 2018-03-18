package atanana.com.sireader

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import atanana.com.sireader.viewmodels.*
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_packs.*
import javax.inject.Inject


class PacksActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: PacksViewModelFactory

    lateinit var viewModel: PacksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packs)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PacksViewModel::class.java)

        fab.setOnClickListener {
            viewModel.fabClicked()
        }

        viewModel.liveBus.observe(this, Observer<Action> {
            it!!
            when (it) {
                is TextMessage -> processTextMessage(it)
                is ActivityForResultMessage -> startActivityForResult(it.intent, it.requestCode)
            }
        })
    }

    private fun processTextMessage(message: TextMessage) {
        val text = when (message) {
            is StringTextMessage -> message.text
            is ResourceTextMessage -> getString(message.textId)
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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
