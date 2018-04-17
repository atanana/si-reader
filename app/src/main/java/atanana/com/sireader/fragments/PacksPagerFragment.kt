package atanana.com.sireader.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.R
import atanana.com.sireader.viewmodels.PacksPagerViewModel
import atanana.com.sireader.viewmodels.ViewModelFactory
import atanana.com.sireader.viewmodels.getViewModel
import atanana.com.sireader.views.questions.PacksPagesAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_packs_pager.*
import javax.inject.Inject

private const val ARG_FILE_ID = "file_id"
private const val ARG_PACK_ID = "pack_id"

class PacksPagerFragment : BaseFragment() {
    private var fileId: Int? = null
    private var packId: Int? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PacksPagerViewModel

    private lateinit var packsPagesAdapter: PacksPagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(viewModelFactory)

        arguments?.let {
            fileId = it.getInt(ARG_FILE_ID)
            packId = it.getInt(ARG_PACK_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_packs_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        packsPagesAdapter = PacksPagesAdapter(fragmentManager!!)
        packs_pager.adapter = packsPagesAdapter
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
