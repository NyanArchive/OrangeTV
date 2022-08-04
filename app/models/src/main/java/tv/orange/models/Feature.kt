package tv.orange.models

interface Feature {
    fun onDestroyFeature()

    fun onCreateFeature()
}