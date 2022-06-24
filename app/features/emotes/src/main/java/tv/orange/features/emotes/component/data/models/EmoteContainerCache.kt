package tv.orange.features.emotes.component.data.models

import android.util.LruCache

class EmoteContainerCache(size: Int) : LruCache<Int, EmoteContainer>(size)