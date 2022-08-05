package tv.orange.models.abc

interface Bridge {
    fun <T : Feature> getFeature(clazz: Class<T>): T
    fun <T : Feature> destroyFeature(clazz: Class<T>)
}