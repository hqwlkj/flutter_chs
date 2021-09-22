package com.parsec.flutter_chs.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.parsec.flutter_chs.R.layout;

public class Loading extends RelativeLayout {
    public Loading(Context context) {
        super(context);
    }

    public Loading(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(layout.vs_main_loading, this);
    }
}
