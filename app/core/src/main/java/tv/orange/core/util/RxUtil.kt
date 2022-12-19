package tv.orange.core.util

import io.reactivex.Single
import tv.orange.core.Core
import tv.orange.core.ResourceManager

object RxUtil {
    fun <T> Single<T>.nRetry(count: Int): Single<T> {
        return this.retryWhen { errors ->
            return@retryWhen errors.take(count.toLong()).doOnNext {
                Core.showToast(
                    ResourceManager.get().getString(
                        "orange_generic_error_d",
                        "nRetry",
                        it.localizedMessage ?: "<empty>"
                    )
                )
                it.printStackTrace()
            }
        }
    }
}