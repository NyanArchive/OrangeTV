package tv.orange.core.models.flag.core

import tv.orange.core.models.flag.variants.ProxyImpl

interface Variant {
    fun getDefault(): Variant
    override fun toString(): String

    companion object {
        fun Variant.getVariants(): List<Variant> {
            return this.javaClass.enumConstants?.toList() ?: emptyList()
        }

        fun Variant.fromString(str: String): Variant? {
            return ProxyImpl.values().firstOrNull { it.value == str }
        }

        fun Variant.isDefault(): Boolean {
            return this == getDefault()
        }
    }
}