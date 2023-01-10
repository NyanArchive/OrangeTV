package tv.orange.core.models.flag.core

interface Variant {
    fun getDefault(): Variant
    override fun toString(): String

    companion object {
        fun Variant.getVariants(): List<Variant> {
            return this.javaClass.enumConstants?.toList() ?: emptyList()
        }

        fun Variant.fromString(str: String): Variant? {
            if (this.javaClass.isEnum) {
                return this.getVariants().firstOrNull { it.toString() == str }
            }

            return null
        }
    }
}