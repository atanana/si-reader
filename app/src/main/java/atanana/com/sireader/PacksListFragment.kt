package atanana.com.sireader

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.viewmodels.PacksListViewModel
import atanana.com.sireader.viewmodels.PacksListViewModelFactory
import atanana.com.sireader.views.gone
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_packs.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class PacksListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: PacksListViewModelFactory

    lateinit var viewModel: PacksListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PacksListViewModel::class.java)

        viewModel.files.observe(this, Observer { files ->
            files!!
            no_packs_label.gone(files.isNotEmpty())
            packs_list.gone(files.isEmpty())
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_packs, container, false)
    }
}
