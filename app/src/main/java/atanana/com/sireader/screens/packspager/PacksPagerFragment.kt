package atanana.com.sireader.screens.packspager


import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import atanana.com.sireader.R
import atanana.com.sireader.databinding.FragmentPacksPagerBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.createViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PacksPagerFragment : BaseFragment<PacksPagerViewModel>(R.layout.fragment_packs_pager) {

    @Inject
    lateinit var viewModelFactory: PacksPagerViewModel.Factory

    private val args: PacksPagerFragmentArgs by navArgs()

    private lateinit var packsPagesAdapter: PacksPagesAdapter

    private val binding by viewBinding(FragmentPacksPagerBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val params = getViewModelParams()
        viewModel = createViewModel {
            viewModelFactory.create(params)
        }
    }

    private fun getViewModelParams(): PacksPagerViewModel.Params {
        return PacksPagerViewModel.Params(args.fileId, args.packId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        packsPagesAdapter = PacksPagesAdapter(this)
        binding.packsPager.adapter = packsPagesAdapter

        viewModel.packs.onEach { state ->
            packsPagesAdapter.packs = state.packs
            val currentIndex = state.packs.indexOfFirst { it.id == state.currentPackId }
            binding.packsPager.setCurrentItem(currentIndex, false)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
