package atanana.com.sireader

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import atanana.com.sireader.viewmodels.FileViewModel
import atanana.com.sireader.viewmodels.FileViewModelFactory
import atanana.com.sireader.views.files.FileInfoAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_file.*
import javax.inject.Inject


private const val ARG_FILE_ID = "file_id"

class FileFragment : Fragment() {
    private var fileId: Int? = null

    @Inject
    lateinit var viewModelFactory: FileViewModelFactory

    private lateinit var viewModel: FileViewModel

    private val packsAdapter = FileInfoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(FileViewModel::class.java)

        arguments?.let {
            fileId = it.getInt(ARG_FILE_ID)
        }

        fileId?.let {
            viewModel.loadFileInfo(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.file.observe(this, Observer { state ->
            state!!
            packsAdapter.packs = state.packs
        })

        file_info.layoutManager = LinearLayoutManager(activity)
        file_info.adapter = packsAdapter
    }

    companion object {
        const val TAG = "FileFragment"

        @JvmStatic
        fun newInstance(fileId: Int) =
                FileFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_FILE_ID, fileId)
                    }
                }
    }
}
