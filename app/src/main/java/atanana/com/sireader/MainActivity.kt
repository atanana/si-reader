package atanana.com.sireader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import atanana.com.sireader.databinding.ActivityMainBinding
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.fileslist.FilesListFragment
import atanana.com.sireader.views.gone
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainActivity : HasAndroidInjector, AppCompatActivity() {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.openFragment(FilesListFragment(), false)
        }
    }

    fun setToolbarVisibility(isVisible: Boolean) {
        binding.toolbar.gone(!isVisible)
    }
}
