package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.Internal

enum class LocalLogs(val value: String) : Internal.Variant {
    L0("0"),
    L500("500"),
    L1000("1000"),
    L5000("5000"),
    L10000("10000");

    override fun getVariants(): List<Internal.Variant> {
        return values().toList()
    }

    override fun getDefault(): Internal.Variant {
        return L0
    }

    override fun fromString(str: String): Internal.Variant? {
        return values().firstOrNull { it.value == str }
    }

    override fun toString(): String {
        return value
    }

    override fun isDefault(): Boolean {
        return this == getDefault()
    }
}