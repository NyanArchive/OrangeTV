package tv.orange.core.models.flag.core

import java.lang.Integer.min

class IntRangeValue(
    val minValue: Int,
    val maxValue: Int,
    private var currentValue: Int = min(minValue, maxValue),
    val step: Int = 1
) : ValueHolder {
    override fun getValue(): Int {
        return currentValue
    }

    override fun setValue(value: Any?) {
        this.currentValue = IntValue.fromAny(value)
    }
}