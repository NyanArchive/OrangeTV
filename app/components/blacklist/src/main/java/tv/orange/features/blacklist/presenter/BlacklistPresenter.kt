package tv.orange.features.blacklist.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.LoggerImpl
import tv.orange.features.blacklist.data.mapper.BlacklistMapper_Factory
import tv.orange.features.blacklist.data.model.KeywordData
import tv.orange.features.blacklist.data.repository.BlacklistRepository
import tv.orange.features.blacklist.data.repository.BlacklistRepository_Factory
import tv.orange.features.blacklist.data.source.BlacklistSource_Factory
import tv.orange.features.blacklist.mvp.BlacklistContract

class BlacklistPresenter(view: BlacklistContract.View) : BlacklistContract.Presenter(view) {
    private val disposables = CompositeDisposable()

    private val repository = newBlacklistRepositoryInstance()

    override fun onStart() {
        fill()
    }

    private fun fill() {
        view.render(BlacklistContract.View.State.Loading)
        disposables.add(repository.getFlow().observeOn(AndroidSchedulers.mainThread()).subscribe({
            view.render(BlacklistContract.View.State.Update(it))
        }, { it.printStackTrace() }))
    }

    override fun onStop() {
        disposables.clear()
    }

    override fun onItemRemoved(keyword: KeywordData) {
        disposables.add(
            repository.delete(keyword = keyword).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LoggerImpl.debug("Removed: $keyword")
                }, { it.printStackTrace() })
        )
    }

    override fun addNewItems(rawText: String) {
        disposables.add(
            repository.addNewItems(rawText)
                .subscribe({
                    LoggerImpl.debug("text: $rawText")
                }, { it.printStackTrace() })
        )
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
                LoggerImpl.debug("Changed: $keyword")
            }, { it.printStackTrace() })
        )
    }

    override fun onAddButtonClicked() {
        view.showAddKeywordsDialog()
    }

    companion object {
        private fun newBlacklistRepositoryInstance(): BlacklistRepository {
            return BlacklistRepository_Factory.create(
                BlacklistSource_Factory.create(
                    BlacklistMapper_Factory.create()
                )
            ).get()
        }
    }
}