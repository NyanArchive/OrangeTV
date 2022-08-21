package tv.orange.features.pronouns.component

import android.util.LruCache
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Logger
import tv.orange.features.pronouns.component.data.repository.PronounsRepository
import tv.orange.features.pronouns.di.scope.PronounScope
import tv.twitch.android.util.Optional
import javax.inject.Inject

@PronounScope
class PronounProvider @Inject constructor(
    val repository: PronounsRepository
) {
    private val cache: LruCache<String, Optional<String>> = LruCache(1000)
    private val idsMap: HashMap<String, Optional<String>> = hashMapOf()

    private val disposables = CompositeDisposable()

    fun destroy() {
        disposables.clear()
        cache.evictAll()
        idsMap.clear()
    }

    fun fetchPronouns() {
        disposables.add(
            repository.getPronouns().subscribeOn(Schedulers.io())
                .subscribe({
                    it.forEach { data ->
                        idsMap[data.name] = Optional.of(data.display)
                    }
                }, Throwable::printStackTrace)
        )
    }

    fun getPronounText(userName: String, function: (String) -> Unit) {
        cache.get(userName)?.let { opt ->
            opt.ifPresent { }
            if (opt.isPresent) {
                function.invoke(opt.get())
            }
            return
        }

        disposables.add(
            repository.getUserPronoun(userName).subscribeOn(Schedulers.io())
                .map {
                    it.pronoun_id ?: run {
                        cache.put(userName, Optional.empty())
                        return@map Optional.empty()
                    }

                    return@map idsMap[it.pronoun_id].also { text ->
                        cache.put(userName, text)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { text ->
                        text?.let {
                            if (it.isPresent) {
                                function.invoke(it.get())
                            }
                        }
                    },
                    Throwable::printStackTrace
                )
        )
    }
}