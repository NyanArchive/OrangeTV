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
    private var lastUpdate: Date = Date(0)
    private var fetching = false

    private val disposables = CompositeDisposable()

    fun getData(): Data? {
        return data
    }

    fun clear() {
        disposables.clear()
        data = null
        lastUpdate = Date()
        fetching = false
    }

    fun refresh(force: Boolean = false) {
        logger.debug("Starting...")
        if (fetching) {
            logger.debug("Skip: fetching")
            return
        }

        if (data != null && !force) {
            val diff = DateUtil.toSeconds(DateUtil.getDiff(lastUpdate, Date()))
            if (diff < refreshTimeout) {
                logger.debug("Skip: diff is $diff")
                return
            }
        }

        fetching = true
        clear()
        disposables.add(dataSourceFactory.create().retryWhen { errors ->
            return@retryWhen errors.take(maxRetry.toLong()).doOnNext {
                logger.debug("Retry...")
            }
        }.subscribe({ res ->
            this.data = res
            this.lastUpdate = Date()
            this.fetching = false
            logger.debug("Fetched: $res")
        }, {
            this.fetching = false
            logger.error("Cannot fetch: ${it.localizedMessage}")
        }))
    }
}
