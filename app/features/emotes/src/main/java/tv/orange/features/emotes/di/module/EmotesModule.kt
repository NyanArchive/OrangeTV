package tv.orange.features.emotes.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.emotes.component.model.factory.RoomFactory
import tv.orange.features.emotes.component.model.factory.RoomFactoryImpl

@Module
abstract class EmotesModule {
    @Binds
    abstract fun bind(roomFactory: RoomFactoryImpl): RoomFactory
}