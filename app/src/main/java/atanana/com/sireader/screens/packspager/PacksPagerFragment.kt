package atanana.com.sireader.screens.packspager


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.R
import atanana.com.sireader.fragments.BaseFragment
import atanana.com.sireader.viewmodels.observe
import kotlinx.android.synthetic.main.fragment_packs_pager.*

private const val ARG_FILE_ID = "file_id"
private const val ARG_PACK_ID = "pack_id"

class PacksPagerFragment : BaseFragment<PacksPagerViewModel>() {
    private var fileId: Int? = null
    private var packId: Int? = null

    private lateinit var packsPagesAdapter: PacksPagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            fileId = it.getInt(ARG_FILE_ID)
            packId = it.getInt(ARG_PACK_ID)
        }

        viewModel.loadPacks(fileId!!, packId!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_packs_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        packsPagesAdapter = PacksPagesAdapter(fragmentManager!!)
        packs_pager.adapter = packsPagesAdapter

        viewModel.packs.observe(this, { state ->
            packsPagesAdapter.packs = state.packs
            val currentIndex = state.packs.indexOfFirst { it.id == state.currentPackId }
            packs_pager.setCurrentItem(currentIndex, false)
        })
    }

    override val viewModelClass: Class<PacksPagerViewModel>
        get() = PacksPagerViewModel::class.java

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
