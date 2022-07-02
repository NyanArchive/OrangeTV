package tv.orange.features.emotes.component.model

import android.util.LruCache

class EmoteContainerCache(size: Int) : LruCache<Int, EmoteContainer>(size)