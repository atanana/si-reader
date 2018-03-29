package atanana.com.sireader

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


private const val ARG_PACK_ID = "pack_id"

class PackFragment : Fragment() {
    private var packId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            packId = it.getInt(ARG_PACK_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pack, container, false)
    }

    companion object {
        val TAG = "PackFragment"

        @JvmStatic
        fun newInstance(packId: Int) =
                PackFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PACK_ID, packId)
                    }
                }
    }
}
