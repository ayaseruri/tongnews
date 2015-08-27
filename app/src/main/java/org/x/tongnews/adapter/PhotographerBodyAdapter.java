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
    private ArrayList<String> mPhotosId;
    private IPhotographerOnClick mIPhotographerOnClick;

    public PhotographerBodyAdapter(Context context, ArrayList<String> photosId, IPhotographerOnClick iPhotographerOnClick){
        mContext = context;
        mPhotosId = photosId;
        this.mIPhotographerOnClick = iPhotographerOnClick;
    }

    @Override
    public PhotographerBodyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotographerBodyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.photographer_body_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotographerBodyViewHolder holder, final int position) {
        final String url = PHOTO_URL_BASE + mPhotosId.get(position) + ".jpg";
        holder.photo.setImageURI(Uri.parse(url));
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIPhotographerOnClick.onPhotoClick(v.getRootView(), position, url);
            }
        });
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

    public interface IPhotographerOnClick{
        void onPhotoClick(View itemView, int position, String url);
    }
}
