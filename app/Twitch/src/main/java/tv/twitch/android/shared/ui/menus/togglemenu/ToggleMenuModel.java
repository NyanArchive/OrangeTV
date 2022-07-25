package tv.twitch.android.shared.ui.menus.togglemenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import tv.twitch.android.core.mvp.lifecycle.VisibilityProvider;
import tv.twitch.android.shared.ui.menus.SettingActionListener;
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController;
import tv.twitch.android.shared.ui.menus.core.MenuModel;


public class ToggleMenuModel extends MenuModel.SingleItemModel { // TODO: __REMOVE_FINAL
    public ToggleMenuModel(String primaryText, String secondaryText, String auxiliaryText, boolean toggleState, boolean isEnabled, Drawable drawable, String eventName, boolean includeBackground, String pillText, Integer pillColor, Integer pillTextColor, SettingsPreferencesController.SettingsPreference settingsPref, View.OnClickListener titleClickListener) {
        super(null, null, null, null, null);
    }

    public final String getEventName() {
        return null;
    }

    @Override
    public ToggleMenuRecyclerItem toRecyclerAdapterItem(Context context, SettingActionListener settingActionListener, VisibilityProvider visibilityProvider) {
        return null;
    }
}
