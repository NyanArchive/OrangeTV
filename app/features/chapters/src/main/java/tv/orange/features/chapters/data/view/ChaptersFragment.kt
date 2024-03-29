package tv.orange.features.chapters.data.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.util.ViewUtil
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.inflate
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

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val id = ResourcesManagerCore.get().getId("design_bottom_sheet")
            if (ViewUtil.isValidId(id)) {
                dialog.findViewById<View>(id)?.let { view ->
                    BottomSheetBehavior.from(view).setState(STATE_EXPANDED)
                }
            }
        }
    }

    fun load(vodId: String) {
        disposables.clear()
        disposables.add(
            repository.getChapters(vodId = vodId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::setData, Throwable::printStackTrace)
        )
    }

    fun bindSeekPresenter(presenter: SeekbarOverlayPresenter) {
        seekPresenter = presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            container = container,
            resName = "orangetv_chapters_container"
        )

        rv = view.getView(resName = "orangetv_chapters_container_rv")
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(
            inflater.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        return view
    }

    override fun onClicked(item: Chapter) {
        item.timestamp.let { timestamp ->
            if (timestamp >= 0) {
                seekPresenter?.xSeekTo(timestamp)
            }
        }

        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        seekPresenter = null
    }
}