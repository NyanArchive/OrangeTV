package tv.orange.features.emotes.component.model.factory

import tv.orange.features.emotes.component.model.room.Room

interface RoomFactory {
    fun create(channelId: Int): Room

    fun createGlobal(): Room
}