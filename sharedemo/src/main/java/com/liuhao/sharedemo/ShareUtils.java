package com.liuhao.sharedemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by ${lhh} on 2018/1/2.
 */

public class ShareUtils {
    private IWXAPI api;
    private Context context;
    private static final String QQ_APPID = "222222";
    private static final String WX_APPID = "222222";

    private static final String WB_APPKEY = "2045436852";
    private static final String WB_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String WB_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    public ShareUtils(Context context) {
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, WX_APPID);
        WbSdk.install(context,new AuthInfo(context, WB_APPKEY, WB_REDIRECT_URL, WB_SCOPE));
    }

    /**
     *
     * @param title
     * @param describe
     * @param webUrl
     * @param types 1 分享微信朋友，2朋友圈
     */
    public void shareToWechat(String title, String describe, String webUrl,String bitmapUrl, int types) {


        WXWebpageObject webpage = new WXWebpageObject();
        webpage.extInfo = describe;
        webpage.webpageUrl = webUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
//		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap thumb = BitmapFactory.decodeFile(bitmapUrl);
        msg.thumbData = BitmapUtils.bmpToByteArray(thumb, true);
        msg.title = title;//不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。
        msg.description = describe;//描述

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;

        req.scene = types == 2 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }



    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }





    /**
     *
     * @param webUrl 连接
     * @param title
     * @param content
     * @param path
     */
    public void shareToQQ(String title, final String content, String webUrl, String path){
        final Bundle params = new Bundle();
//		Log.e("QQ好友分享的Logo地址==", path);
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, path);//本地图片
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//分享的类型。图文分享(普通分享)填Tencent.SHARE_TO_QQ_TYPE_DEFAULT
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);//必填（要分享的标题）
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);//摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  webUrl);//这条分享消息被好友点击后的跳转URL(需要跳转的链接，URL字符串)。

        // QQ分享要在主线程做
        final Tencent mTencent = Tencent.createInstance(QQ_APPID, context);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (null != mTencent) {
                    mTencent.shareToQQ((Activity) context, params, new MIUiListener());
                }
            }
        });
    }


    /**
     * 分享到QQ空间(无需QQ登录)
     */
    public void shareToQzone(String title,String content,String webUrl,String path ){
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );//分享的类型。图文分享(普通分享)填Tencent.SHARE_TO_QQ_TYPE_DEFAULT
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填（要分享的标题）
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content); //摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webUrl);//这条分享消息被好友点击后的跳转URL(需要跳转的链接，URL字符串)。

        Log.e("QQ空间分享的Logo地址==", path);
        //分享Logo图片
//        ArrayList<String> imageUrls = new ArrayList<String>();
//        imageUrls.add(path);
//        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        // QQ分享要在主线程做
        final Tencent mTencent = Tencent.createInstance(QQ_APPID, context);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (null != mTencent) {
                    mTencent.shareToQzone((Activity) context, params, new MIUiListener());
                }
            }
        });

    }


    private class MIUiListener implements IUiListener {
        public MIUiListener(){
        }
        @Override
        public void onCancel() {
            Toast.makeText(context,"取消", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Object response) {
            Toast.makeText(context,"成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError arg0) {
            Toast.makeText(context,arg0.errorMessage, Toast.LENGTH_SHORT).show();
        }

    }




    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    public void shareWeiBo(String title, String describe, String webUrl,String bitmapUrl) {
        WbShareHandler shareHandler = new WbShareHandler((Activity) context);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        //多媒体消息（网页）对象
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = describe;
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapUrl);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = webUrl;
        mediaObject.defaultText = describe;

        weiboMessage.mediaObject = mediaObject;

        shareHandler.shareMessage(weiboMessage, false);

    }



}
