package tv.orange.bridge.di.module

import dagger.Module
import dagger.Provides
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.features.api.component.repository.ProxyRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.badges.component.BadgeProvider
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.features.highlighter.Highlighter
import tv.orange.features.highlighter.di.component.HighlighterComponent
import tv.orange.features.pronouns.component.PronounProvider
import tv.orange.features.pronouns.di.component.PronounComponent
import tv.orange.features.vodhunter.Vodhunter
import tv.orange.models.abc.TCPProvider
import tv.orange.models.abc.TwitchComponentProvider
import tv.twitch.android.app.core.ApplicationContext

@Module(includes = [BridgeComponentModule::class, BridgeFeatureModule::class])
class BridgeModule {
    @BridgeScope
    @Provides
    fun provideInjector(): TwitchComponentProvider {
        val context = ApplicationContext.getInstance().getContext()
        if (context is TCPProvider) {
            return context.provideTCP()
        }

        throw IllegalStateException("Application context must provide injector")
    }

    @BridgeScope
    @Provides
    fun provideEmotes(emotesComponent: EmotesComponent): EmoteProvider {
        return emotesComponent.emoteProvider
    }

    @BridgeScope
    @Provides
    fun provideBadges(badgesComponent: BadgesComponent): BadgeProvider {
        return badgesComponent.badgeProvider
    }

    @BridgeScope
    @Provides
    fun providePronouns(pronounComponent: PronounComponent): PronounProvider {
        return pronounComponent.pronounProvider
    }

    @BridgeScope
    @Provides
    fun provideStvRepository(apiComponent: ApiComponent): StvRepository {
        return apiComponent.stvRepository
    }

    @BridgeScope
    @Provides
    fun provideNopRepository(apiComponent: ApiComponent): NopRepository {
        return apiComponent.nopRepository
    }

    @BridgeScope
    @Provides
    fun provideHighlighter(highlighterComponent: HighlighterComponent): Highlighter {
        return highlighterComponent.highlighter
    }

    @BridgeScope
    @Provides
    fun provideProxyRepository(apiComponent: ApiComponent): ProxyRepository {
        return apiComponent.proxyRepository
    }
}