package org.x.tongnews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.x.tongnews.R;
import org.x.tongnews.object.PostsProvider;

import java.util.ArrayList;

import andy.ayaseruri.tagsviewlib.TagsView;

/**
 * Created by ayaseruri on 15/7/17.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_POSTION = 0;
    private static final int TYPE_HEADER = -1;
    private static final int TYPE_ITEM = -2;

    private ArrayList<PostsProvider.Post> mHomeListData;
    private LayoutInflater mLayoutInflater;
    private HomeListOnItemClick mHomeListOnItemClick;
    private View mHeaderView;

    public HomeRecyclerAdapter(Context context, ArrayList<PostsProvider.Post> homeListData, HomeListOnItemClick homeListOnItemClick, View headerView){
        mHomeListData = homeListData;
        mLayoutInflater = LayoutInflater.from(context);
        mHomeListOnItemClick = homeListOnItemClick;
        mHeaderView = headerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            return new HomeRecyclerHeaderViewHolder(mHeaderView);
        }else if(viewType == TYPE_ITEM){
            return new HomeRecyclerItemViewHolder(mLayoutInflater.inflate(R.layout.home_recycler_item, parent, false));
        }
        throw new RuntimeException("no view type matched");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position != HEADER_POSTION) {
            PostsProvider.Post homeListItemData = mHomeListData.get(position - 1);
            HomeRecyclerItemViewHolder homeRecyclerItemViewHolder = (HomeRecyclerItemViewHolder)holder;
            homeRecyclerItemViewHolder.imageView.setImageURI(Uri.parse(homeListItemData.getImageUrl()));
            homeRecyclerItemViewHolder.title.setText(homeListItemData.getTitle());
            homeRecyclerItemViewHolder.time.setText(homeListItemData.getTime());
            homeRecyclerItemViewHolder.shortDescription.setText(homeListItemData.getShortDiscription());
            homeRecyclerItemViewHolder.author.setText(homeListItemData.getAuthor());
            homeRecyclerItemViewHolder.tags.init(homeListItemData.getTags(), null);
            homeRecyclerItemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHomeListOnItemClick.OnItemClick(v, position - 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mHomeListData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == HEADER_POSTION){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public static class HomeRecyclerItemViewHolder extends RecyclerView.ViewHolder{

        public SimpleDraweeView imageView;
        public TextView time;
        public TextView title;
        public CardView cardView;
        public TextView shortDescription;
        public TextView author;
        public TagsView tags;

        public HomeRecyclerItemViewHolder(View itemView) {
            super(itemView);
            tags = (TagsView)itemView.findViewById(R.id.tags);
            imageView = (SimpleDraweeView)itemView.findViewById(R.id.image);
            time = (TextView)itemView.findViewById(R.id.time);
            title = (TextView)itemView.findViewById(R.id.title);
            shortDescription = (TextView)itemView.findViewById(R.id.short_description);
            author = (TextView)itemView.findViewById(R.id.author);
            cardView = (CardView)itemView.findViewById(R.id.home_card_root);
        }
    }

    public static class HomeRecyclerHeaderViewHolder extends RecyclerView.ViewHolder{
        public HomeRecyclerHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface HomeListOnItemClick{
        void OnItemClick(View rootView, int postion);
    }
}
