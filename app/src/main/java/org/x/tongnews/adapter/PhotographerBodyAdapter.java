package org.x.tongnews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import org.x.tongnews.R;

import java.util.ArrayList;

/**
 * Created by ayaseruri on 15/8/4.
 */
public class PhotographerBodyAdapter extends RecyclerView.Adapter<PhotographerBodyAdapter.PhotographerBodyViewHolder> {

    private static final String PHOTO_URL_BASE = "http://7xk7dq.com1.z0.glb.clouddn.com/images/photo/pcp";

    public Context mContext;
    ArrayList<String> mPhotosId;

    public PhotographerBodyAdapter(Context context, ArrayList<String> photosId){
        mContext = context;
        mPhotosId = photosId;
    }

    @Override
    public PhotographerBodyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotographerBodyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.photographer_body_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotographerBodyViewHolder holder, int position) {
        holder.photo.setImageURI(Uri.parse(PHOTO_URL_BASE + mPhotosId.get(position) + ".jpg"));
    }

    @Override
    public int getItemCount() {
        return mPhotosId.size();
    }

    public static class PhotographerBodyViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView photo;

        public PhotographerBodyViewHolder(View itemView) {
            super(itemView);
            photo = (SimpleDraweeView)itemView.findViewById(R.id.photo);
        }
    }
}
