package org.x.tongnews.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import org.x.tongnews.global.Constant;
import org.x.tongnews.global.MApplication_;
import org.x.tongnews.object.PostDetail;
import org.x.tongnews.object.RawPosts;
import org.x.tongnews.object.RawSliders;
import org.x.tongnews.object.VideoInfo;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ayaseruri on 15/7/29.
 */

public class NetBusiness{

    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    public static GsonConverter gsonConverter = new GsonConverter(gson);
    public static OkClient okClient = new OkClient(MApplication_.getInstance().okHttpClient);

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setConverter(gsonConverter)
            .setEndpoint(Constant.BASE_URL)
            .setClient(okClient)
            .build();

    private static RestAdapter restAdapterVideo = new RestAdapter.Builder()
            .setConverter(gsonConverter)
            .setEndpoint(Constant.BILIBILI_API)
            .setClient(okClient)
            .build();

    public static NetService netService = restAdapter.create(NetService.class);
    public static NetServiceB netServiceVideo = restAdapterVideo.create(NetServiceB.class);

    public interface NetService {
        @GET("/?json=get_index_static_all_in_one")
        void getSliders(Callback<RawSliders> cb);

        @GET("/?json=get_category_posts_main&category_slug=post&count=12")
        void getPosts(@Query("page") int pageConut, Callback<RawPosts> cb);

        @GET("/?json=view_post")
        void getPostDetail(@Query("id") String id, Callback<PostDetail> cb);
    }

    public interface NetServiceB{
        @GET("/m/html5")
        void getVideoInfo(@Query("aid") String id, Callback<VideoInfo> cb);
    }
}
