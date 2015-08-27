package andy.ayaseruri.lib.mvc.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import andy.ayaseruri.lib.mvc.R;
import andy.ayaseruri.lib.mvc.adapter.Adapter;
import andy.ayaseruri.lib.mvc.adapter.HeaderViewAdapter;
import andy.ayaseruri.lib.mvc.interfaces.Interfaces;
import andy.ayaseruri.lib.mvc.model.Model;

/**
 * Created by ayaseruri on 15/8/16.
 */

public class Pull2RefreshRecyclerView extends LinearLayout implements Interfaces.IAction{

    private Interfaces.IDataSource iDataSource;
    private Interfaces.IAction iAction;
    private Interfaces.IAdapter iAdapter;

    private Adapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mHeaderView;

    private Model model;

    private LinearLayoutManager mLayoutManager;
    private boolean alreadyHasTask = false;

    public Pull2RefreshRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public Pull2RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        int layoutId = getResources().getIdentifier("pull_to_refresh_recycler_view", "layout", context.getPackageName());
        View rootView = LayoutInflater.from(context).inflate(layoutId, this);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void init(){
        model = new Model(iDataSource, this);
        if(null != mHeaderView){
            mAdapter = new HeaderViewAdapter(model.dataList, iAdapter, mHeaderView);
        }else {
            mAdapter = new Adapter(model.dataList, iAdapter);
        }
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == model.dataList.size() && !alreadyHasTask){
                    model.loadMore(false);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!alreadyHasTask)
                model.refresh();
            }
        });

        firstIn();
    }

    public void firstIn(){
        model.firstIn();
    }

    public void setDataSource(Interfaces.IDataSource iDataSource){
        this.iDataSource = iDataSource;
    }

    public void setiAction(Interfaces.IAction iAction){
        this.iAction = iAction;
    }

    public void setiAdapter(Interfaces.IAdapter iAdapter) {
        this.iAdapter = iAdapter;
    }

    @Override
    public void onLoadBegin() {
        alreadyHasTask = true;
        if(!mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
        iAction.onLoadBegin();
    }

    @Override
    public void onLoadSuccess(List dataList) {
        alreadyHasTask = false;
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        iAction.onLoadSuccess(dataList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed() {
        alreadyHasTask = false;
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        iAction.onLoadFailed();
    }

    @Override
    public void onLoadError() {
        alreadyHasTask = false;
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        iAction.onLoadError();
    }

    @Override
    public void onAlreadyEnd() {
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        iAction.onAlreadyEnd();
    }

    public SwipeRefreshLayout getmSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void setHeaderView(View headerView){
        this.mHeaderView = headerView;
    }
}
