package tv.orange.models

interface Bridge {
    fun <T : Feature> getFeature(clazz: Class<T>): T
    fun <T : Feature> destroyFeature(clazz: Class<T>)
}