package andy.ayaseruri.lib.mvc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import andy.ayaseruri.lib.mvc.interfaces.Interfaces;

/**
 * Created by ayaseruri on 15/8/17.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MViewHolder> {

    private Interfaces.IAdapter iAdapter;
    private ArrayList arrayList;

    public Adapter(ArrayList arrayList, Interfaces.IAdapter iAdapter){
        this.iAdapter = iAdapter;
        this.arrayList = arrayList;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MViewHolder(iAdapter.getItemView());
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, final int position) {
        iAdapter.bindViewData(holder.itemView, arrayList.get(position));
        holder.itemView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapter.onItemClick(v, arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MViewHolder extends RecyclerView.ViewHolder{

        public MViewHolder(View itemView) {
            super(itemView);
        }
    }
}
