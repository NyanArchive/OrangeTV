package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class FontSize(val value: String) : Variant {
    SP8("8sp"),
    SP9("9sp"),
    SP10("10sp"),
    SP11("11sp"),
    SP12("12sp"),
    SP13("13sp"),
    SP14("14sp"),
    SP15("15sp"),
    SP16("16sp"),
    SP17("17sp"),
    SP18("18sp");

    override fun getVariants(): List<Variant> {
        return values().toList()
    }

    override fun getDefault(): Variant {
        return SP13
    }

    override fun fromString(str: String): Variant? {
        return values().firstOrNull { it.value == str }
    }

    override fun toString(): String {
        return value
    }

    override fun isDefault(): Boolean {
        return this == getDefault()
    }
}