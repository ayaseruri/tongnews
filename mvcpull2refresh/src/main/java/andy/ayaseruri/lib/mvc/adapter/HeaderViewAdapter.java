package andy.ayaseruri.lib.mvc.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import andy.ayaseruri.lib.mvc.interfaces.Interfaces;

/**
 * Created by ayaseruri on 15/8/18.
 */
public class HeaderViewAdapter extends Adapter {

    private static final int HEADER_POSTION = 0;
    private static final int TYPR_HEADER = 0;
    private static final int TYPE_NORMAL = 1;

    private Interfaces.IAdapter iAdapter;
    private View mHeaderView;

    public HeaderViewAdapter(ArrayList arrayList, Interfaces.IAdapter iAdapter, View headerView) {
        super(arrayList, iAdapter);
        this.mHeaderView = headerView;
        this.iAdapter = iAdapter;
        arrayList.add(0, null);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == HEADER_POSTION){
            return TYPR_HEADER;
        }else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPR_HEADER){
            return new MViewHolder(mHeaderView);
        }else {
            return new MViewHolder(iAdapter.getItemView());
        }
    }


    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        if(position != HEADER_POSTION){
            super.onBindViewHolder(holder, position);
        }
    }
}
