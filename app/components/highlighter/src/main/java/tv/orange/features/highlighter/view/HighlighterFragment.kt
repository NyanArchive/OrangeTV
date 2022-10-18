package tv.orange.features.highlighter.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import tv.orange.core.Logger
import tv.orange.core.ResourceManager
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.highlighter.data.model.KeywordData
import tv.orange.features.highlighter.mvp.HighlighterContract
import tv.orange.features.highlighter.presenter.HighlighterPresenter
import tv.orange.features.highlighter.util.SwipeToDeleteCallback

class HighlighterFragment : Fragment(), HighlighterContract.View, ClickListener {
    private lateinit var rv: RecyclerView
    private lateinit var loading: ProgressBar
    private lateinit var addButton: TextView

    private val presenter = HighlighterPresenter(this)
    val adapter = HighlighterAdapter(this)

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            ResourceManager.get().getLayoutId("orangetv_highlighter"),
            container,
            false
        )
        addButton = view.getView("orangetv_highlighter__add_button")
        loading = view.getView("orangetv_highlighter__loading")
        rv = view.getView("orangetv_highlighter__rv")
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this.context)

        val swipeHandler = object : SwipeToDeleteCallback(inflater.context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    adapter.getItem(pos)?.let { keyword ->
                        presenter.onItemRemoved(keyword)
                    } ?: adapter.removeAt(pos)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv)

        addButton.setOnClickListener { presenter.onAddButtonClicked() }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = HighlighterFragment()
    }

    override fun render(state: HighlighterContract.View.State) {
        when (state) {
            is HighlighterContract.View.State.Added -> TODO()
            is HighlighterContract.View.State.Deleted -> TODO()
            HighlighterContract.View.State.Loading -> {
                loading.changeVisibility(isVisible = true)
                rv.changeVisibility(isVisible = false)
            }
            is HighlighterContract.View.State.Update -> {
                adapter.setData(state.keywords)
                loading.changeVisibility(isVisible = false)
                rv.changeVisibility(isVisible = true)
            }
        }
    }

    override fun showAddKeywordsDialog() {
        val editText = EditText(requireActivity())
        val dialog = AlertDialog.Builder(requireContext())
            .setView(editText)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val rawText = editText.text.toString().trim()
                presenter.addNewItems(rawText)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
        dialog.show()
    }

    override fun showChangeItemColorDialog(keyword: KeywordData) {
        val dialog = ColorPickerDialog.newBuilder()
            .setColor(keyword.color)
            .create()

        dialog.addColorPickerDialogListener(object : ColorPickerDialogListener {
            override fun onColorSelected(dialogId: Int, newColor: Int) {
                presenter.onItemColorChanged(keyword = keyword, newColor = newColor)
            }

            override fun onDialogDismissed(dialogId: Int) {
                Logger.debug("called!")
            }
        })
        dialog.show(requireActivity().supportFragmentManager, "orange_change_color")
    }

    override fun onChangeColorClicked(item: KeywordData) {
        presenter.onChangeColorItemClicked(keyword = item)
    }

    override fun onVibrationClicked(item: KeywordData) {
        presenter.onVibrationItemClicked(keyword = item)
    }
}