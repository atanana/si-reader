package atanana.com.sireader.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.R
import atanana.com.sireader.viewmodels.PackViewModel
import atanana.com.sireader.viewmodels.ViewModelFactory
import atanana.com.sireader.viewmodels.getViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

private const val ARG_PACK_ID = "pack_id"

class PackFragment : Fragment() {
    private var packId: Int? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(viewModelFactory)

        arguments?.apply {
            packId = getInt(ARG_PACK_ID)
        }

        packId?.let {
            viewModel.loadPack(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pack, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.pack.observe(this, Observer { state ->

        })
    }

    companion object {
        const val TAG = "PackFragment"

        @JvmStatic
        fun newInstance(packId: Int) =
                PackFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PACK_ID, packId)
                    }
                }
    }
}