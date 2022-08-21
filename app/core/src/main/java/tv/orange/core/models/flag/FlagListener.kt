package tv.orange.core.models.flag

interface FlagListener {
    fun onFlagValueChanged(flag: Flag)
}