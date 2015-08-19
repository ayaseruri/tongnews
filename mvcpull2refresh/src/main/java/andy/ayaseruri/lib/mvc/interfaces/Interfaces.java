package andy.ayaseruri.lib.mvc.interfaces;

import android.view.View;

import java.util.List;

/**
 * Created by ayaseruri on 15/8/16.
 */
public class Interfaces {
    public interface IDataSource<DATA>{
        List<DATA> getFirstInData();
        void onGetRefreshData();
        void getLoadMoreData(IGetDataCallBack callBack);
        void cancleLoadTask();
        boolean hasMore();
    }

    public interface IGetDataCallBack<DATA>{
        void onSuccess(List<DATA> dataList);
        void onFail();
        void onError();
    }

    public interface IAction<DATA>{
        void onLoadBegin();
        void onLoadSuccess(List<DATA> dataList);
        void onLoadFailed();
        void onLoadError();
        void onAlreadyEnd();
    }

    public interface IAdapter<DATA>{
        View getItemView();
        void bindViewData(View itemView, DATA data);
        void onItemClick(View itemView, DATA data);
    }
}
