package tv.orange.core.models.flag

interface FlagListener {
    fun onFlagChanged(flag: Flag)
}