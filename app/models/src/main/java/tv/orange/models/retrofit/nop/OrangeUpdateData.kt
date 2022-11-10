package tv.orange.models.retrofit.nop

data class OrangeUpdateData(
    val dev: UpdateChannelData,
    val beta: UpdateChannelData,
    val release: UpdateChannelData
)
