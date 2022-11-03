package tv.orange.core

import android.content.Context
import androidx.core.content.ContextCompat
import tv.orange.core.models.ResIdCache
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Feature
import tv.twitch.android.core.strings.StringResource

@AutoInitialize
class ResourceManager(private val context: Context) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(ResourceManager::class.java)
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}

    private enum class Id(type: String, size: Int) {
        ID("id", 100),
        STRING("string", 100),
        DRAWABLE("drawable", 10),
        COLOR("color", 10),
        LAYOUT("layout", 10),
        DIMEN("dimen", 10),
        ARRAY("array", 10),
        ATTR("attr", 100);

        private val cache: ResIdCache = ResIdCache(type, size)

        fun get(resName: String): Int {
            return cache.get(resName)
        }
    }

    fun getId(resName: String): Int {
        return Id.ID.get(resName)
    }

    fun getLayoutId(resName: String): Int {
        return Id.LAYOUT.get(resName)
    }

    fun getStringId(resName: String): Int {
        return Id.STRING.get(resName)
    }

    fun getStringResource(resName: String): StringResource {
        return StringResource.Companion!!.fromStringId(getStringId(resName = resName))
    }

    fun getDrawableId(resName: String): Int {
        return Id.DRAWABLE.get(resName)
    }

    fun getColorId(resName: String): Int {
        return Id.COLOR.get(resName)
    }

    fun getColor(resName: String): Int {
        return ContextCompat.getColor(context, getColorId(resName))
    }

    fun getDimenId(resName: String): Int {
        return Id.DIMEN.get(resName)
    }

    fun getArrayId(resName: String): Int {
        return Id.ARRAY.get(resName)
    }

    fun getAttrId(resName: String): Int {
        return Id.ATTR.get(resName)
    }

    fun getString(resName: String, vararg formatArgs : Any): String {
        val id = Id.STRING.get(resName)

        return if (id == 0 || id == -1) {
            "$resName: ID NOT FOUND"
        } else {
            context.getString(id, *formatArgs)
        }
    }

    fun getString(resName: String): String {
        val id = Id.STRING.get(resName)

        return if (id == 0 || id == -1) {
            "$resName: ID NOT FOUND"
        } else {
            context.getString(id)
        }
    }

    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}