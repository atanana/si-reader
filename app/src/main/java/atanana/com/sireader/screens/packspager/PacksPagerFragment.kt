package atanana.com.sireader.screens.packspager


import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import atanana.com.sireader.R
import atanana.com.sireader.databinding.FragmentPacksPagerBinding
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.fragments.createViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val ARG_FILE_ID = "file_id"
private const val ARG_PACK_ID = "pack_id"

@AndroidEntryPoint
class PacksPagerFragment : BaseFragment<PacksPagerViewModel>(R.layout.fragment_packs_pager) {
    private var fileId: Int? = null
    private var packId: Int? = null

    private lateinit var packsPagesAdapter: PacksPagesAdapter

    private val binding by viewBinding(FragmentPacksPagerBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            fileId = it.getInt(ARG_FILE_ID)
            packId = it.getInt(ARG_PACK_ID)
        }

        viewModel = createViewModel()
        viewModel.loadPacks(fileId!!, packId!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        packsPagesAdapter = PacksPagesAdapter(this)
        binding.packsPager.adapter = packsPagesAdapter

        viewModel.packs.onEach { state ->
            state ?: return@onEach
            packsPagesAdapter.packs = state.packs
            val currentIndex = state.packs.indexOfFirst { it.id == state.currentPackId }
            binding.packsPager.setCurrentItem(currentIndex, false)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override val transactionTag: String
        get() = TAG

    companion object {
        const val TAG = "PacksPagerFragment"

        @JvmStatic
        fun newInstance(fileId: Int, packId: Int) =
            PacksPagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FILE_ID, fileId)
                    putInt(ARG_PACK_ID, packId)
                }
            }
    }
}
