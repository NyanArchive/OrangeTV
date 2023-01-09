package tv.orange.features.settings.bridge.settings

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Core
import tv.orange.core.ResourcesManagerCore
import tv.orange.features.settings.bridge.model.OrangeSubMenu
import tv.orange.features.settings.component.OrangeSettingsController
import tv.orange.features.tracking.Tracking
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.infomenu.InfoMenuModel

class OrangeInfoSettingsPresenter constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    controller: OrangeSettingsController
) : BasedSettingsPresenter(
    activity,
    adapterBinder,
    settingsTracker,
    controller,
    OrangeSubMenu.Info
) {
    val disposables = CompositeDisposable()

    override fun updateSettingModels() {
        super.updateSettingModels()
        settingModels.addAll(
            listOf(
                createInfoMenu(
                    "orange_settings_info_dev",
                    "orange_settings_info_dev_desc",
                    "https://t.me/nopbreak"
                ),
                createInfoMenu(
                    "orange_settings_info_itsfadixx",
                    "orange_settings_info_itsfadixx_desc",
                    "https://t.me/itsfadixx"
                ),
                createInfoMenu(
                    "orange_settings_info_lanzzz",
                    "orange_settings_info_lanzzz_desc",
                    "https://www.twitch.tv/lanzzopoulos"
                ),
                createInfoMenu(
                    "orange_settings_info_tg",
                    "orange_settings_info_tg_desc",
                    "https://t.me/pubTwChat"
                ),
                createInfoMenu(
                    "orange_settings_info_discord",
                    "orange_settings_info_discord_desc",
                    "https://discord.gg/DkjMM4ThhG"
                )
            )
        )
        val rm = ResourcesManagerCore.get()
        disposables.add(
            Tracking.get().getPinnyInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    settingModels.add(
                        InfoMenuModel(
                            rm.getString("orange_pinny_active_users", it.build),
                            rm.getString("orange_pinny_total_users", it.total),
                            null, null, null, null, null
                        ) as MenuModel
                    )
                    this.bindSettings()
                }, Throwable::printStackTrace)
        )
    }

    companion object {
        fun createInfoMenu(main: String, desc: String?, url: String): MenuModel {
            return InfoMenuModel(
                ResourcesManagerCore.get().getString(main),
                desc?.let { str -> ResourcesManagerCore.get().getString(str) },
                null, null, null, null
            ) {
                openUrl(url)
            } as MenuModel
        }

        fun openUrl(url: String) {
            if (url.isBlank()) {
                return
            }

            Core.get().context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }
}