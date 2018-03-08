package com.liuhao.sharedemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * Created by ${lhh} on 2018/1/2.
 */

public class ShareDialog extends Dialog implements View.OnClickListener{
    private ShareUtils shareUtils;
    private String title,describe,webUrl,bitmapUrl;

    public ShareDialog(@NonNull Context context) {
        this(context,0);
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        shareUtils = new ShareUtils(context);

        init(context);
    }


    public void setContent(String title,String describe,String webUrl){
        this.title = title;
        this.describe = describe;
        this.webUrl = webUrl;
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.sharedialog, null);
        ImageView wechatIv = view.findViewById(R.id.wechat_iv);
        ImageView wechatMomentsIv = view.findViewById(R.id.wechat_moments_iv);
        ImageView qqIv = view.findViewById(R.id.qq_iv);
        ImageView qzoneIv = view.findViewById(R.id.qzone_iv);
        ImageView sinaIv = view.findViewById(R.id.sina_iv);
        TextView cancelTv = view.findViewById(R.id.cancel_tv);

        wechatIv.setOnClickListener(this);
        wechatMomentsIv.setOnClickListener(this);
        qqIv.setOnClickListener(this);
        qzoneIv.setOnClickListener(this);
        sinaIv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

        setContentView(view);
        this.setCancelable(true);

        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialog_buttom_in);
        window.setGravity(Gravity.BOTTOM);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wechat_iv:
                shareUtils.shareToWechat(title,describe,webUrl,bitmapUrl,1);
                break;
            case R.id.wechat_moments_iv:
                shareUtils.shareToWechat(title,describe,webUrl,bitmapUrl,2);
                break;
            case R.id.qq_iv:
                shareUtils.shareToQQ(title,describe,webUrl,bitmapUrl);
                break;
            case R.id.qzone_iv:
                shareUtils.shareToQzone(title,describe,webUrl,bitmapUrl);
                break;
            case R.id.sina_iv:
                shareUtils.shareWeiBo(title,describe,webUrl,bitmapUrl);
                break;
            case R.id.cancel_tv:
                this.dismiss();
                break;
        }
    }
}
