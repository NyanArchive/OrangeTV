package com.jaredrummler.android.colorpicker;

import android.content.Context;
import android.widget.GridView;

import tv.orange.models.exception.VirtualImpl;

public class NestedGridView extends GridView {
    public NestedGridView(Context context) {
        super(context);
        throw new VirtualImpl();
    }
}
