package org.x.tongnews.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;
import org.x.tongnews.data.DataProvider;
import org.x.tongnews.global.Constant;
import org.x.tongnews.global.MApplication;
import org.x.tongnews.net.NetBusiness;
import org.x.tongnews.object.PostDetail;
import org.x.tongnews.object.VideoInfo;
import org.x.tongnews.view.ProgressBarWebView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import andy.ayaseruri.lib.CircularRevealActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_post_detail)
public class PostDetailActivity extends CircularRevealActivity {

    @App
    MApplication mApplication;

    @ViewById(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    public static final String CSS_STYLE = "<style>img {\n" +
            "\tmargin-top: 0.4em;\n" +
            "\tmargin-bottom: 0.4em;\n" +
            "\tvertical-align: bottom;\n" +
            "\twidth: 98%;\n" +
            "\tbox-shadow: 1px 1px 3px 1px rgba(0, 0, 0, 0.4);\n" +
            "\tborder-radius: 0.2em;\n" +
            "\theight:auto!important;\n" +
            "\t}\n" +
            "iframe {\n" +
            "\tmax-width:100%!important;\n" +
            "\theight:270;\n" +
            "\t}\n" +
            "a {\n" +
            "\tword-break:break-all!important;\n" +
            "\t}\n" +
            "p {\n" +
            "\tline-height:1.5;\n" +
            "\t}</style>";

    @ViewById(R.id.detail_header)
    SimpleDraweeView mDetailHeader;
    @ViewById(R.id.ic_play_normal)
    ImageView playBtn;
    @ViewById(R.id.post_detail_sence)
    CoordinatorLayout revealLinearLayout;

    private Pattern mPattern = Pattern.compile("aid=(\\d+)");
    private String videoLink;
    private String headerImageUrl;

    @AfterViews
    void init() {
        String id = getIntent().getStringExtra("id");
        videoLink = getIntent().getStringExtra("video_link");
        headerImageUrl = getIntent().getStringExtra("header_image_url");
        String tile = getIntent().getStringExtra("title");
        mCollapsingToolbarLayout.setTitle(tile);

        if (null != videoLink && !"".equals(videoLink)) {
            Matcher matcher = mPattern.matcher(videoLink);
            if (matcher.find()) {
                DataProvider.getInstance().getVideoInfo(matcher.group(1), new Callback<VideoInfo>() {
                    @Override
                    public void success(VideoInfo videoInfo, Response response) {
                        updateVideoView(videoInfo);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        } else {
            mDetailHeader.setImageURI(Uri.parse(headerImageUrl));
        }

        NetBusiness.netService.getPostDetail(id, new Callback<PostDetail>() {
            @Override
            public void success(PostDetail postDetail, Response response) {
                onDataArrived(postDetail);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @UiThread
    void onDataArrived(PostDetail postDetail) {
        String content = postDetail.getPost().getContent();
        List<String> videoLinks = postDetail.getPost().getCustom_fields().getVideo_link();
        content = content.replaceAll(" height=", " replaced=");
        content = content.replaceAll(" height:", " replaced:");
        ProgressBarWebView progressBarWebView = (ProgressBarWebView) findViewById(R.id.progressbar_webview);
        WebView webView = progressBarWebView.getWebView();
        webView.loadDataWithBaseURL(Constant.BASE_URL, CSS_STYLE + content, "text/html", "utf-8", null);
    }

    @UiThread
    void updateVideoView(final VideoInfo videoInfo) {
        mDetailHeader.setImageURI(Uri.parse(headerImageUrl));
        playBtn.setVisibility(View.VISIBLE);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("video_link", videoInfo.getSrc());
                intent.setClass(PostDetailActivity.this, VideoPlayActivity_.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("文章详情");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("文章详情");
        MobclickAgent.onPause(this);
    }
}
