package org.x.tongnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;
import org.x.tongnews.activity.MainActivity;
import org.x.tongnews.activity.PostDetailActivity_;
import org.x.tongnews.adapter.HomeRecyclerAdapter;
import org.x.tongnews.data.DataProvider;
import org.x.tongnews.global.MApplication;
import org.x.tongnews.object.PostsProvider;
import org.x.tongnews.object.SlidersProvider;
import org.x.tongnews.view.HomeHeaderItem;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ayaseruri on 15/7/17.
 */

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    private ArrayList<PostsProvider.Post> mHomeListData = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

    private View mHeaderView;
    private SliderLayout mHeaderSlider;

    private Context mContext;
    private AlphaInAnimationAdapter mAlphaInAnimationAdapter;

    @App
    MApplication mApplication;
    @ViewById(R.id.home_recycler)
    RecyclerView mRecyclerView;
    @ViewById(R.id.swipe_refreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int refreshTaskCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = mApplication.getApplicationContext();
    }

    @Override
    public void onStop() {
        mHeaderSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @AfterViews
    void init(){

        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.home_header, null);
        mHeaderSlider = (SliderLayout)mHeaderView.findViewById(R.id.sliders);
        mHeaderSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mHeaderSlider.setDuration(4000);

        mSwipeRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.main_color));

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        HomeRecyclerAdapter mHomeRecyclerAdapter = new HomeRecyclerAdapter(mContext, mHomeListData, new HomeRecyclerAdapter.HomeListOnItemClick() {
            @Override
            public void OnItemClick(View v, int postion) {
                String videoLink = mHomeListData.get(postion).getVideoLink();

                openDetail(((MainActivity) getActivity()).getStartPoint(), String.valueOf(mHomeListData.get(postion).getId())
                        , mHomeListData.get(postion).getTitle(), mHomeListData.get(postion).getImageUrl(), videoLink);
            }
        }, mHeaderView);

        SlideInBottomAnimationAdapter slideInBottomAnimationAdapter =
                new SlideInBottomAnimationAdapter(mHomeRecyclerAdapter);
        mAlphaInAnimationAdapter = new AlphaInAnimationAdapter(slideInBottomAnimationAdapter);

        mRecyclerView.setAdapter(mAlphaInAnimationAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mHomeListData.size()) {
                    getMorePost();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRefreshPost();
            }
        });

        getFirstInData();
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
    void getFirstInData(){
        if(!mSwipeRefreshLayout.isRefreshing()) {
            refreshTaskCount = 2;
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
            DataProvider.getInstance().getFirstInData(PostsProvider.class, new Callback<PostsProvider>() {
                @Override
                public void success(PostsProvider postsProvider, Response response) {
                    if (0 != mHomeListData.size()) {
                        mHomeListData.clear();
                    }
                    mHomeListData.addAll(postsProvider.getPostArrayList());
                    mAlphaInAnimationAdapter.notifyDataSetChanged();
                    refreshTaskComplete();
                }

                @Override
                public void failure(RetrofitError error) {
                    refreshTaskComplete();
                }
            });

            DataProvider.getInstance().getFirstInData(SlidersProvider.class, new Callback<SlidersProvider>() {
                @Override
                public void success(SlidersProvider slidersProvider, Response response) {
                    updateSlider(slidersProvider);
                    refreshTaskComplete();
                }

                @Override
                public void failure(RetrofitError error) {
                    refreshTaskComplete();
                }
            });
        }
    }

    @UiThread
    void getRefreshPost(){
        refreshTaskCount = 2;
        DataProvider.getInstance().getRefreshSliders(new Callback<SlidersProvider>() {
            @Override
            public void success(SlidersProvider slidersProvider, Response response) {
                updateSlider(slidersProvider);
                refreshTaskComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                refreshTaskComplete();
            }
        });

        DataProvider.getInstance().getRefreshPosts(new Callback<PostsProvider>() {
            @Override
            public void success(PostsProvider postsProvider, Response response) {
                if (0 != mHomeListData.size()) {
                    mHomeListData.clear();
                }
                mHomeListData.addAll(postsProvider.getPostArrayList());
                mAlphaInAnimationAdapter.notifyDataSetChanged();
                refreshTaskComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                refreshTaskComplete();
            }
        });
    }

    @UiThread
    void refreshTaskComplete(){
        refreshTaskCount--;
        if(0 == refreshTaskCount){
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @UiThread
    void getMorePost(){
        if(!mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(true);
            DataProvider.getInstance().getMorePosts(new Callback<PostsProvider>() {
                @Override
                public void success(PostsProvider postsProvider, Response response) {
                    mHomeListData.addAll(postsProvider.getPostArrayList());
                    mAlphaInAnimationAdapter.notifyDataSetChanged();
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Logger.d(error.toString());
                }
            }, false);
        }
    }

    @UiThread
    void openDetail(int[] startFormLocation, String id, String title, String header_image_url, String videoLink){
        Intent intent = new Intent();
        intent.setClass(mContext, PostDetailActivity_.class);
        intent.putExtra("start_form_location", startFormLocation);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.putExtra("header_image_url", header_image_url);
        intent.putExtra("video_link", videoLink);
        startActivity(intent);
    }
}
