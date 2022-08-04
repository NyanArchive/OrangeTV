package tv.orange.features.badges.component.model.factory

import tv.orange.features.badges.component.model.room.Room

interface RoomFactory {
    fun create(): Room
}