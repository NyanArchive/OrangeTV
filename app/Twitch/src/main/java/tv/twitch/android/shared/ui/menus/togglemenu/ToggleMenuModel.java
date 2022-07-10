package tv.twitch.android.shared.ui.menus.togglemenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import tv.twitch.android.core.adapters.RecyclerAdapterItem;
import tv.twitch.android.core.mvp.lifecycle.VisibilityProvider;
import tv.twitch.android.shared.ui.menus.SettingActionListener;
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController;
import tv.twitch.android.shared.ui.menus.formvalue.FormValueDelegate;

public class ToggleMenuModel extends FormValueDelegate<Boolean> { // TODO: __REMOVE_FINAL
    public ToggleMenuModel(String var1, String var2, String var3, boolean var4, boolean var5, Drawable var6, String var7, boolean var8, String var9, Integer var10, Integer var11, SettingsPreferencesController.SettingsPreference var12, View.OnClickListener var13) {
        super(var1, var2, var3, var6, var4, var5);
    }

    public final String getEventName() {
        return null;
    }

    @Override
    protected RecyclerAdapterItem toRecyclerAdapterItem(Context context, SettingActionListener settingActionListener, VisibilityProvider visibilityProvider) {
        return null;
    }
}
