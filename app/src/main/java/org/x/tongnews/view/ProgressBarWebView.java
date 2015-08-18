package org.x.tongnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.rey.material.widget.ProgressView;

import org.x.tongnews.R;
import org.x.tongnews.global.MApplication_;

/**
 * Created by ayaseruri on 15/7/18.
 */
public class ProgressBarWebView extends FrameLayout {

    private ProgressView mProgressView;
    private WebView mWebView;
    private RelativeLayout mFrameLayout;

    public ProgressBarWebView(Context context) {
        super(context);
    }

    public ProgressBarWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.progressbar_webview, this);
        mProgressView = (ProgressView)findViewById(R.id.progressbar);
        mWebView = (WebView)findViewById(R.id.web_view);

        mFrameLayout = (RelativeLayout)findViewById(R.id.progressbar_webview_frame_root);
        mFrameLayout.setMinimumHeight(MApplication_.getInstance().getmScreenHeight()
                - context.getResources().getDimensionPixelSize(R.dimen.fragment_detail_head));

        mWebView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
    }

    public WebView getWebView(){
        return mWebView;
    }
}
