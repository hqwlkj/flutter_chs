package com.parsec.flutter_chs.loading;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.parsec.flutter_chs.R.id;
import com.parsec.flutter_chs.R.style;
import com.parsec.flutter_chs.R.layout;

public class LoadingDialog extends Dialog {
    private static final int MSG_UPDATE_LOADING_DOT = 1;
    private final ImageView mLoadingDot1;
    private final ImageView mLoadingDot2;
    private final ImageView mLoadingDot3;
    private final TextView mLoadingBrand;
    private final Handler mHandler;
    private int mDotSeq;

    public LoadingDialog(Context context){
        super(context, style.Loading);
        this.setContentView(layout.vs_main_loading);
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = 17;
        window.setAttributes(params);
        this.mLoadingDot1 = this.findViewById(id.loading_dot1);
        this.mLoadingDot2 = this.findViewById(id.loading_dot2);
        this.mLoadingDot3 = this.findViewById(id.loading_dot3);
        this.mLoadingBrand = this.findViewById(id.loading_brand);
        this.mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (LoadingDialog.this.mDotSeq == 0) {
                        LoadingDialog.this.mLoadingDot1.setAlpha(0.6F);
                        LoadingDialog.this.mLoadingDot2.setAlpha(1.0F);
                        LoadingDialog.this.mLoadingDot3.setAlpha(0.6F);
                        LoadingDialog.this.mDotSeq++;
                    } else if (LoadingDialog.this.mDotSeq == 1) {
                        LoadingDialog.this.mLoadingDot1.setAlpha(0.3F);
                        LoadingDialog.this.mLoadingDot2.setAlpha(0.6F);
                        LoadingDialog.this.mLoadingDot3.setAlpha(1.0F);
                        LoadingDialog.this.mDotSeq++;
                    } else if (LoadingDialog.this.mDotSeq == 2) {
                        LoadingDialog.this.mLoadingDot1.setAlpha(1.0F);
                        LoadingDialog.this.mLoadingDot2.setAlpha(0.6F);
                        LoadingDialog.this.mLoadingDot3.setAlpha(0.3F);
                        LoadingDialog.this.mDotSeq = 0;
                    }
                    LoadingDialog.this.mHandler.sendEmptyMessageDelayed(1, 800L);
                }
            }
        };
    }


    public void show(String msg) {
        this.mHandler.sendEmptyMessageDelayed(1, 800L);
        if(msg == null){
            msg = "中国医疗保障";
        }
        mLoadingBrand.setText(msg);
        this.mDotSeq = 0;
        super.show();
    }

    public void hide() {
        this.mHandler.removeMessages(1);
        this.mDotSeq = 0;
        this.mLoadingDot1.setAlpha(1.0F);
        this.mLoadingDot2.setAlpha(0.6F);
        this.mLoadingDot3.setAlpha(0.3F);
        super.dismiss();
    }
}
