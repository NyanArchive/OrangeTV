package tv.orange.core.models.flag.core

interface Variant {
    fun getVariants(): List<Variant>
    fun getDefault(): Variant
    fun fromString(str: String): Variant?
    fun isDefault(): Boolean
    override fun toString(): String
}