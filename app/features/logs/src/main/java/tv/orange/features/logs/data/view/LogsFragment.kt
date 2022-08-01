package tv.orange.features.logs.data.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.ResourceManager
import tv.orange.core.ViewUtil.getView
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.component.data.repository.LogsRepository
import tv.orange.features.logs.data.adapter.LogsAdapter
import javax.inject.Inject

class LogsFragment @Inject constructor(
    val logsRepository: LogsRepository,
    val logsAdapter: LogsAdapter
) : BottomSheetDialogFragment() {
    private lateinit var rv: RecyclerView
    private lateinit var pb: ProgressBar

    private val disposables = CompositeDisposable()

    fun bind(fragment: FragmentActivity) {
        logsAdapter.bindActivity(fragment)
    }

    private fun render(messages: List<MessageItem>) {
        logsAdapter.setData(messages)
        pb.visibility = View.GONE
        rv.visibility = View.VISIBLE
    }

    fun load(userLogin: String, channelId: String) {
        disposables.clear()
        disposables.add(
            logsRepository.getLogs(userLogin, channelId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ render(it) }, Throwable::printStackTrace)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            ResourceManager.getId("orangetv_logs_container", "layout"),
            container,
            false
        )
        pb = view.getView("orangetv_logs_container__pb")
        rv = view.getView<RecyclerView>("orangetv_logs_container__rv").apply {
            adapter = logsAdapter
        }

        return view
    }
}