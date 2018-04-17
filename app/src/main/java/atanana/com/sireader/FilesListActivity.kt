package atanana.com.sireader

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import atanana.com.sireader.fragments.FilesListFragment
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.viewmodels.*
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_files_list.*
import javax.inject.Inject


class FilesListActivity : HasSupportFragmentInjector, AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: FilesListActivityViewModel

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files_list)
        setSupportActionBar(toolbar)

        supportFragmentManager.openFragment(FilesListFragment(), false)

        viewModel = getViewModel(viewModelFactory)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_files, menu)
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
