package atanana.com.sireader

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.viewmodels.OpenPack
import atanana.com.sireader.viewmodels.PacksListViewModel
import atanana.com.sireader.viewmodels.PacksListViewModelFactory
import atanana.com.sireader.viewmodels.PacksListViewState
import atanana.com.sireader.views.gone
import atanana.com.sireader.views.packs.PacksListAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_packs.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class PacksListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: PacksListViewModelFactory

    private lateinit var viewModel: PacksListViewModel

    private val packsAdapter = PacksListAdapter { packId ->
        viewModel.onPackClick(packId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PacksListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_packs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.files.observe(this, Observer { state ->
            state!!
            setViewState(state)
        })

        viewModel.liveBus.observe(this, Observer { action ->
            when (action) {
                is OpenPack -> openPack(action.packId)
            }
        })

        packs_list.layoutManager = LinearLayoutManager(activity)
        packs_list.adapter = packsAdapter
    }

    private fun setViewState(state: PacksListViewState) {
        no_packs_label.gone(state.noPacksLabelGone)
        packs_list.gone(state.packsListGone)
        packsAdapter.packs = state.packs
    }

    private fun openPack(packId: Int) {
        fragmentManager?.apply {
            beginTransaction()
                    .replace(R.id.fragment, PackFragment.newInstance(packId), PackFragment.TAG)
                    .addToBackStack(PackFragment.TAG)
                    .commit()
        }
    }
}
