package tv.orange.features.highlighter.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.features.highlighter.data.mapper.HighlighterMapper_Factory
import tv.orange.features.highlighter.data.model.KeywordData
import tv.orange.features.highlighter.data.repository.HighlighterRepository
import tv.orange.features.highlighter.data.repository.HighlighterRepository_Factory
import tv.orange.features.highlighter.data.source.HighlighterSource_Factory
import tv.orange.features.highlighter.mvp.HighlighterContract

class HighlighterPresenter(view: HighlighterContract.View) : HighlighterContract.Presenter(view) {
    private val disposables = CompositeDisposable()

    private val repository = newHighlighterRepositoryInstance()

    override fun onStart() {
        fill()
    }

    private fun fill() {
        view.render(HighlighterContract.View.State.Loading)
        disposables.add(repository.getFlow().observeOn(AndroidSchedulers.mainThread()).subscribe({
            view.render(HighlighterContract.View.State.Update(it))
        }, { it.printStackTrace() }))
    }

    override fun onStop() {
        disposables.clear()
    }

    override fun onItemRemoved(keyword: KeywordData) {
        disposables.add(
            repository.delete(keyword = keyword).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.debug("Removed: $keyword")
                }, { it.printStackTrace() })
        )
    }

    override fun onItemColorChanged(keyword: KeywordData, newColor: Int) {
        disposables.add(
            repository.update(
                item = keyword.copy(color = KeywordData.Color.resolve(newColor).value)
            ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                Logger.debug("Keyword: $keyword, newColor: $newColor")
            }, { it.printStackTrace() })
        )
    }

    override fun addNewItems(rawText: String) {
        disposables.add(
            repository.addNewItems(rawText)
                .subscribe({
                    Logger.debug("text: $rawText")
                }, { it.printStackTrace() })
        )
    }

    override fun onVibrationItemClicked(keyword: KeywordData) {
        disposables.add(
            repository.update(
                item = keyword.copy(vibration = !keyword.vibration),
            ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                Logger.debug("Changed: $keyword")
            }, { it.printStackTrace() })
        )
    }

    override fun onChangeColorItemClicked(keyword: KeywordData) {
        view.showChangeItemColorDialog(keyword)
    }

    override fun onChangeTypeItemClicked(keyword: KeywordData) {
        disposables.add(
            repository.changeType(
                item = keyword,
                newType = when (keyword.type) {
                    KeywordData.Type.CASESENSITIVE -> KeywordData.Type.INSENSITIVE
                    KeywordData.Type.INSENSITIVE -> KeywordData.Type.USERNAME
                    KeywordData.Type.USERNAME -> KeywordData.Type.CASESENSITIVE
                }
            ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                Logger.debug("Changed: $keyword")
            }, { it.printStackTrace() })
        )
    }

    override fun onAddButtonClicked() {
        view.showAddKeywordsDialog()
    }

    companion object {
        private fun newHighlighterRepositoryInstance(): HighlighterRepository {
            return HighlighterRepository_Factory.create(
                HighlighterSource_Factory.create(
                    HighlighterMapper_Factory.create()
                )
            ).get()
        }
    }
}