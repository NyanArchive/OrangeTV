package tv.orange.models.data

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import tv.orange.models.util.DateUtil
import tv.orange.models.util.Logger
import java.util.*

abstract class SimpleFetcher<Data>(
    private val dataSourceFactory: SourceFactory<Data>,
    private val refreshTimeout: Int = 60,
    private val maxRetry: Int = 3,
    private val logger: Logger
) {
    interface SourceFactory<Data> {
        fun create(): Single<Data>
    }

    private var data: Data? = null
    private var lastUpdate: Date? = null
    private var fetching = false

    private val disposables = CompositeDisposable()

    fun getData(): Data? {
        return data
    }

    fun clear() {
        disposables.clear()
        data = null
        lastUpdate = null
        fetching = false
    }

    fun refresh(force: Boolean = false) {
        logger.devDebug("Starting...")
        if (fetching) {
            logger.devDebug("Skip: fetching")
            return
        }

        if (data != null && !force) {
            lastUpdate?.let { ld ->
                val diff = DateUtil.toSeconds(DateUtil.getDiff(ld, Date()))
                if (diff < refreshTimeout) {
                    logger.devDebug("Skip: diff is $diff")
                    return
                }
            }
        }

        fetching = true
        clear()
        disposables.add(dataSourceFactory.create().retryWhen { errors ->
            return@retryWhen errors.take(maxRetry.toLong()).doOnNext {
                logger.warning("Retry...")
            }
        }.subscribe({ res ->
            this.data = res
            this.lastUpdate = Date()
            this.fetching = false
            logger.devDebug { "Fetched: $res" }
        }, {
            this.fetching = false
            logger.error("Cannot fetch: ${it.localizedMessage}")
        }))
    }
}
