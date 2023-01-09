package tv.orange.core.models.flag.core

class IntRangeValue(
    val minValue: Int,
    val maxValue: Int,
    private var currentValue: Int,
    val step: Int = 1
) : ValueHolder {
    override fun getValue(): Int {
        return currentValue
    }

    override fun setValue(value: Any?) {
        this.currentValue = IntValue.fromAny(value)
    }
}