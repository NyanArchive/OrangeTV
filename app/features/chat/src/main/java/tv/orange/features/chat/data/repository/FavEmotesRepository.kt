package tv.orange.features.chat.data.repository

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.features.chat.data.model.FavEmote
import tv.orange.features.chat.data.source.FavEmotesDataSource
import javax.inject.Inject

class FavEmotesRepository @Inject constructor(
    val source: FavEmotesDataSource
) {
    private val disposables = CompositeDisposable()

    fun getChannelEmotes(channelId: Int): Single<List<FavEmote>> {
        return source.getChannelEmotes(channelId)
    }

    fun deleteEmote(type: String, channelId: String, code: String) {
        disposables.add(
            source.delete(type, channelId, code).subscribeOn(Schedulers.io()).subscribe({}, { it.printStackTrace() })
        )
    }

    fun addEmote(favEmote: FavEmote) {
        disposables.add(
            source.addEmote(favEmote).subscribeOn(Schedulers.io())
                .subscribe({}, { it.printStackTrace() })
        )
    }
}