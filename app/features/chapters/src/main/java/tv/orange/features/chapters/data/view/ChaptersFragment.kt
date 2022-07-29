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
import tv.orange.core.Logger
import tv.orange.core.ResourceManager
import tv.orange.features.chapters.component.data.model.Chapter
import tv.orange.features.chapters.component.data.repository.ChaptersRepository
import tv.orange.features.chapters.data.adapter.ChaptersAdapter
import javax.inject.Inject

class ChaptersFragment @Inject constructor(val repository: ChaptersRepository) : BottomSheetDialogFragment() {
    lateinit var rv: RecyclerView
    private val adapter = ChaptersAdapter()

    private val disposables = CompositeDisposable()

    fun load(id: String) {
        disposables.clear()
        disposables.add(repository.getChapters(id).observeOn(AndroidSchedulers.mainThread()).subscribe({
            adapter.setData(it)
        }, {
            it.printStackTrace()
        }))
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
}