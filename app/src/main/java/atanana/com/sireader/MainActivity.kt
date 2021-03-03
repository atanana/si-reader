package atanana.com.sireader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import atanana.com.sireader.databinding.ActivityMainBinding
import atanana.com.sireader.fragments.openFragment
import atanana.com.sireader.screens.fileslist.FilesListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.openFragment(FilesListFragment(), false)
        }
    }
}
