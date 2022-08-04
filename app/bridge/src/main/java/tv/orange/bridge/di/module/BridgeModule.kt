package tv.orange.bridge.di.module

import dagger.Module

@Module(includes = [BridgeComponentModule::class, BridgeFeatureModule::class])
class BridgeModule