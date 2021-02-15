package atanana.com.sireader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import atanana.com.sireader.fragments.hasFragment
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.fileslist.FilesListFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : HasAndroidInjector, AppCompatActivity() {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (!supportFragmentManager.hasFragment()) {
            supportFragmentManager.openFragment(FilesListFragment(), false)
        }
    }
}
