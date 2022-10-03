package tv.orange.features.chat.data.repository

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.features.chat.data.source.FavEmotesDataSource
import tv.orange.features.chat.db.entities.FavEmoteEntity
import tv.orange.models.abc.EmotePackageSet
import javax.inject.Inject

class FavEmotesRepository @Inject constructor(
    val source: FavEmotesDataSource
) {
    private val disposables = CompositeDisposable()

    fun getChannelEmotes(channelId: Int): Single<List<FavEmoteEntity>> {
        return source.getChannelEmotes(channelId)
    }

    fun deleteEmote(uid: Int) {
        disposables.add(
            source.delete(uid).subscribeOn(Schedulers.io()).subscribe({}, { it.printStackTrace() })
        )
    }

    fun addEmote(
        channelId: Int,
        emoteCode: String,
        emoteId: String?,
        packageSet: EmotePackageSet
    ) {
        disposables.add(
            source.addEmote(
                channelId = channelId.toString(),
                emoteCode = emoteCode,
                emoteId = emoteId,
                packageSet = packageSet
            ).subscribeOn(Schedulers.io()).subscribe({}, { it.printStackTrace() })
        )
    }
}