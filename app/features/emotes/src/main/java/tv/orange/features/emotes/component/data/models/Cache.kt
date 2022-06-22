package tv.orange.features.emotes.component.data.models

import android.util.LruCache

class Cache(size: Int) : LruCache<Int, ChannelSet>(size)