package org.x.tongnews.global;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.squareup.okhttp.OkHttpClient;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.EApplication;

/**
 * Created by ayaseruri on 15/7/18.
 */
@EApplication
public class MApplication extends Application {

    public int mScreenWidth;
    public int mScreenHeight;
    public OkHttpClient okHttpClient = new OkHttpClient();

    public static final int TOAST_ALERT = 1;
    public static final int TOAST_INFO = 2;
    public static final int TOAST_SUCCESS = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                .build();
        Fresco.initialize(this, config);
        MobclickAgent.openActivityDurationTrack(false);
    }

    public int getmScreenHeight(){
        return mScreenHeight;
    }

    public int getScreenWidth(){
        return mScreenWidth;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //通用的一些方法
    public int getVersionCode(){
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return 1;
        }
    }

    public void showMsg(int kind, String msg){
        SuperToast superToast = null;
        switch (kind){
            case TOAST_ALERT:
                superToast = SuperToast.create(this, msg, SuperToast.Duration.LONG,
                        Style.getStyle(Style.RED, SuperToast.Animations.POPUP));
            break;
            case TOAST_INFO:
            superToast = SuperToast.create(this, msg, SuperToast.Duration.LONG,
                    Style.getStyle(Style.GRAY, SuperToast.Animations.POPUP));
            break;
            case TOAST_SUCCESS:
            superToast = SuperToast.create(this, msg, SuperToast.Duration.LONG,
                    Style.getStyle(Style.GREEN, SuperToast.Animations.POPUP));
            break;
        }
        superToast.show();
    }
}
