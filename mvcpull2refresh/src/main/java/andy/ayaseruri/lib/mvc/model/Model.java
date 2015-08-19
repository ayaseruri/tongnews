package andy.ayaseruri.lib.mvc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import andy.ayaseruri.lib.mvc.interfaces.Interfaces;

/**
 * Created by ayaseruri on 15/8/16.
 */
public class Model{
    private Interfaces.IAction iAction;
    private Interfaces.IDataSource iDataSource;
    public ArrayList<Objects> dataList = new ArrayList();

    public Model(Interfaces.IDataSource iDataSource, Interfaces.IAction iAction){
        this.iDataSource = iDataSource;
        this.iAction = iAction;
    }

    public void firstIn(){
        this.dataList.clear();
        iDataSource.onGetRefreshData();
        if(null != iDataSource.getFirstInData()){
            dataList.addAll(iDataSource.getFirstInData());
            iAction.onLoadSuccess(Model.this.dataList);
        }
        loadMore(true);

    }

    public void refresh(){
        iDataSource.onGetRefreshData();
        loadMore(true);
    }

    public void loadMore(final Boolean needClear){
        if(!iDataSource.hasMore()){
            iAction.onAlreadyEnd();
            return;
        }

        iDataSource.cancleLoadTask();

        iAction.onLoadBegin();
        iDataSource.getLoadMoreData(new Interfaces.IGetDataCallBack() {
            @Override
            public void onSuccess(List dataList) {
                if(needClear){
                    Model.this.dataList.clear();
                }
                Model.this.dataList.addAll(dataList);
                iAction.onLoadSuccess(Model.this.dataList);
            }

            @Override
            public void onFail() {
                iAction.onLoadFailed();
            }

            @Override
            public void onError() {
                iAction.onLoadError();
            }

        });
    }
}
