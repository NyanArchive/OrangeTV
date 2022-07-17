package tv.twitch.android.shared.ui.menus.dropdown;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import tv.twitch.android.core.adapters.RecyclerAdapterItem;
import tv.twitch.android.core.mvp.lifecycle.VisibilityProvider;
import tv.twitch.android.shared.ui.menus.SettingActionListener;
import tv.twitch.android.shared.ui.menus.core.MenuModel;

public class DropDownMenuModel<T> extends MenuModel.SingleItemModel {
    public interface DropDownMenuItemSelection<T> {
        void onDropDownItemSelected(DropDownMenuModel<T> dropDownMenuModel, int i);
    }

    public DropDownMenuModel(ArrayAdapter<T> adapter, int position, String primaryText, String secondaryText, String auxiliaryText, View.OnClickListener clickListener, DropDownMenuModel.DropDownMenuItemSelection<T> dropDownMenuItemSelection) {
        super(null, null, null, null, null);
    }

    @Override
    protected RecyclerAdapterItem toRecyclerAdapterItem(Context context, SettingActionListener settingActionListener, VisibilityProvider visibilityProvider) {
        return null;
    }
}
