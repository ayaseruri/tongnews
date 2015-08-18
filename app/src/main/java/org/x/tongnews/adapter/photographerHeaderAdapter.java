package org.x.tongnews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.x.tongnews.R;
import org.x.tongnews.view.MaterialRippleLayout;

/**
 * Created by ayaseruri on 15/8/4.
 */
public class PhotographerHeaderAdapter extends RecyclerView.Adapter<PhotographerHeaderAdapter.PhotographerHeaderViewHolder> {

    String[] photographerAvatarUrl;
    String[] photographerPhotoUrlArray;
    String[] photographerNickName;

    Context mContext;
    PhotographerItemOnClick mPhotographerItemOnClick;

    public PhotographerHeaderAdapter(Context context, PhotographerItemOnClick photographerItemOnClick){
        this.mContext = context;
        photographerAvatarUrl = mContext.getResources().getStringArray(R.array.photographer_avatar_urls);
        photographerPhotoUrlArray = mContext.getResources().getStringArray(R.array.photographer_photo_urls);
        photographerNickName = mContext.getResources().getStringArray(R.array.photographer_nickname);
        mPhotographerItemOnClick = photographerItemOnClick;
    }

    @Override
    public PhotographerHeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotographerHeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.photographer_header_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotographerHeaderViewHolder holder, final int position) {
        holder.photographerNikename.setText(photographerNickName[position]);
        holder.photographerAvatar.setImageURI(Uri.parse(photographerAvatarUrl[position]));
        holder.materialRippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotographerItemOnClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photographerAvatarUrl.length;
    }

    public static class PhotographerHeaderViewHolder extends RecyclerView.ViewHolder{

        public SimpleDraweeView photographerAvatar;
        public TextView photographerNikename;
        public MaterialRippleLayout materialRippleLayout;

        public PhotographerHeaderViewHolder(View itemView) {
            super(itemView);
            photographerAvatar = (SimpleDraweeView)itemView.findViewById(R.id.photographer_avatar);
            photographerNikename = (TextView)itemView.findViewById(R.id.photographer_nikename);
            materialRippleLayout = (MaterialRippleLayout)itemView.findViewById(R.id.photographer_item_root);
        }
    }

    public interface PhotographerItemOnClick{
        void onClick(int postion);
    }


}
