package tv.orange.features.badges.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.badges.component.model.factory.RoomFactory
import tv.orange.features.badges.component.model.factory.RoomFactoryImpl

@Module
abstract class BadgesModule {
    @Binds
    abstract fun bind(roomFactory: RoomFactoryImpl): RoomFactory
}