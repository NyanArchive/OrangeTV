package tv.orange.features.chapters.data.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.ResourceManager
import tv.orange.features.chapters.component.data.model.Chapter
import tv.orange.features.chapters.component.data.repository.ChaptersRepository
import tv.orange.features.chapters.data.adapter.ChaptersAdapter
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter
import javax.inject.Inject

class ChaptersFragment @Inject constructor(val repository: ChaptersRepository) :
    BottomSheetDialogFragment(), ChaptersAdapter.OnChapterClickedListener {
    lateinit var rv: RecyclerView
    private val adapter = ChaptersAdapter(this)
    private var seekPresenter: SeekbarOverlayPresenter? = null

    private val disposables = CompositeDisposable()

    fun load(id: String) {
        disposables.clear()
        disposables.add(
            repository.getChapters(id).observeOn(AndroidSchedulers.mainThread()).subscribe({
                adapter.setData(it)
            }, {
                it.printStackTrace()
            })
        )
    }

    fun bindSeekPresenter(presenter: SeekbarOverlayPresenter) {
        seekPresenter = presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            ResourceManager.getId("orange_chapters_container", "layout"),
            container,
            false
        )
        rv = view.findViewById(ResourceManager.getId("orange_chapters_container_rv", "id"))
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(
            inflater.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        return view
    }

    override fun onClicked(item: Chapter) {
        if (item.timestamp >= 0) {
            seekPresenter?.xSeekTo(item.timestamp)
        }
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        seekPresenter = null
    }
}