package org.x.tongnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;
import org.x.tongnews.activity.MainActivity;
import org.x.tongnews.activity.PostDetailActivity_;
import org.x.tongnews.data.DataProvider;
import org.x.tongnews.global.MApplication;
import org.x.tongnews.object.PostsProvider;
import org.x.tongnews.object.SlidersProvider;
import org.x.tongnews.view.HomeHeaderItem;

import java.util.List;

import andy.ayaseruri.lib.mvc.interfaces.Interfaces;
import andy.ayaseruri.lib.mvc.view.Pull2RefreshRecyclerView;
import andy.ayaseruri.tagsviewlib.TagsView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ayaseruri on 15/7/17.
 */

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    private View mHeaderView;
    private SliderLayout mHeaderSlider;

    private Context mContext;
    private boolean hasMore = true;

    @App
    MApplication mApplication;
    @ViewById(R.id.Pull2RefreshRecyclerView)
    Pull2RefreshRecyclerView pull2RefreshRecyclerView;

    private String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = mApplication.getApplicationContext();
        mTitle = getArguments().getString("title");
    }

    @Override
    public void onStop() {
        mHeaderSlider.stopAutoCycle();
        super.onStop();
    }

    @AfterViews
    void init(){
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.home_header, null);
        mHeaderSlider = (SliderLayout)mHeaderView.findViewById(R.id.sliders);
        mHeaderSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mHeaderSlider.setDuration(4000);

        pull2RefreshRecyclerView.setHeaderView(mHeaderView);
        pull2RefreshRecyclerView.setDataSource(new Interfaces.IDataSource() {
            @Override
            public List getFirstInData() {
                return DataProvider.getInstance().getFirstInData(PostsProvider.class);
            }

            @Override
            public void onGetRefreshData() {
                DataProvider.getInstance().onGetRefresh();
            }

            @Override
            public void getLoadMoreData(final Interfaces.IGetDataCallBack callBack) {
                DataProvider.getInstance().getMorePosts(new Callback<PostsProvider>() {
                    @Override
                    public void success(PostsProvider postsProvider, Response response) {
                        callBack.onSuccess(postsProvider.getPostArrayList());
                        hasMore = DataProvider.postPageConut < postsProvider.totalPageCount;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Logger.d(error.toString());
                    }
                });
            }

            @Override
            public void cancleLoadTask() {

            }

            @Override
            public boolean hasMore() {
                return hasMore;
            }
        });

        pull2RefreshRecyclerView.setiAction(new Interfaces.IAction() {
            @Override
            public void onLoadBegin() {
                DataProvider.getInstance().getRefreshSliders(new Callback<SlidersProvider>() {
                    @Override
                    public void success(SlidersProvider slidersProvider, Response response) {
                        updateSlider(slidersProvider);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            public void onLoadSuccess(List list) {

            }

            @Override
            public void onLoadFailed() {
                mApplication.showMsg(MApplication.TOAST_ALERT, "抱歉，但是加载失败啦");
            }

            @Override
            public void onLoadError() {
                mApplication.showMsg(MApplication.TOAST_ALERT, "抱歉，但是什么东西出错啦");
            }

            @Override
            public void onAlreadyEnd() {
                mApplication.showMsg(MApplication.TOAST_ALERT, "已经到达最后一页啦");
            }
        });

        pull2RefreshRecyclerView.setiAdapter(new Interfaces.IAdapter() {
            @Override
            public View getItemView() {
                return LayoutInflater.from(mContext).inflate(R.layout.home_recycler_item, null);
            }

            @Override
            public void bindViewData(View itemView, Object o) {
                TagsView tags = (TagsView) itemView.findViewById(R.id.tags);
                SimpleDraweeView imageView = (SimpleDraweeView) itemView.findViewById(R.id.image);
                TextView time = (TextView) itemView.findViewById(R.id.time);
                TextView title = (TextView) itemView.findViewById(R.id.title);
                TextView shortDescription = (TextView) itemView.findViewById(R.id.short_description);
                TextView author = (TextView) itemView.findViewById(R.id.author);

                PostsProvider.Post homeListItemData = (PostsProvider.Post) o;
                imageView.setImageURI(Uri.parse(homeListItemData.getImageUrl()));
                title.setText(homeListItemData.getTitle());
                time.setText(homeListItemData.getTime());
                shortDescription.setText(homeListItemData.getShortDiscription());
                author.setText(homeListItemData.getAuthor());
                tags.init(homeListItemData.getTags(), null);
            }

            @Override
            public void onItemClick(View itemView, Object o) {
                String videoLink = ((PostsProvider.Post) o).getVideoLink();
                openDetail(((MainActivity) getActivity()).getStartPoint(), String.valueOf(((PostsProvider.Post) o).getId())
                        , ((PostsProvider.Post) o).getTitle(), ((PostsProvider.Post) o).getImageUrl(), videoLink);
            }
        });

        pull2RefreshRecyclerView.getmSwipeRefreshLayout().setColorSchemeColors(mContext.getResources().getColor(R.color.main_color));
        pull2RefreshRecyclerView.init();
    }

    @UiThread
    void updateSlider(SlidersProvider slidersProvider){
        mHeaderSlider.removeAllSliders();
        for(final SlidersProvider.Slider slider : slidersProvider.getSliderArrayList()){
            HomeHeaderItem textSliderView = new HomeHeaderItem(mContext);
            textSliderView.description(slider.getTitle())
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView baseSliderView) {
                                    String id = slider.getLinkAdressId();
                                    if(!"".equals(id)){
                                        String videoLink = slider.getVideoLink();
                                        openDetail(((MainActivity)getActivity()).getStartPoint(), id, slider.getTitle(), slider.getImageUrl(), videoLink);
                                    }
                                }
                            })
                            .image(slider.getImageUrl());
            mHeaderSlider.addSlider(textSliderView);
        }
    }

    @UiThread
    void openDetail(int[] startFormLocation, String id, String title, String header_image_url, String videoLink){
        Intent intent = new Intent();
        intent.setClass(mContext, PostDetailActivity_.class);
        intent.putExtra("start_point", startFormLocation);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.putExtra("header_image_url", header_image_url);
        intent.putExtra("video_link", videoLink);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mTitle);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mTitle);
    }
}
