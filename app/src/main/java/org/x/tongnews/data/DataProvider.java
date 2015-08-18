package org.x.tongnews.data;

import com.j256.ormlite.dao.Dao;

import org.x.tongnews.global.MApplication;
import org.x.tongnews.global.MApplication_;
import org.x.tongnews.net.NetBusiness;
import org.x.tongnews.object.PostsProvider;
import org.x.tongnews.object.RawPosts;
import org.x.tongnews.object.RawSliders;
import org.x.tongnews.object.SlidersProvider;
import org.x.tongnews.object.VideoInfo;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ayaseruri on 15/7/29.
 */
public class DataProvider {

    private static DataProvider instance;
    private static int postPageConut = 1;

    private Pattern mPattern = Pattern.compile("id=(\\d+)");


    public static DataProvider getInstance(){
        if(null == instance){
            instance = new DataProvider();
        }
        return instance;
    }

    private DataProvider(){

    }

    public void getRefreshSliders(final Callback<SlidersProvider> cb){
        NetBusiness.netService.getSliders(new Callback<RawSliders>() {
            @Override
            public void success(RawSliders rawSliders, Response response) {
                cb.success(RawSlider2SliderProvider(rawSliders, true), response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    public void getVideoInfo(String id, Callback<VideoInfo> cb){
        NetBusiness.netServiceVideo.getVideoInfo(id, cb);
    }

    public void getFirstInData(Class clazz, Callback cb){
        Dao dao = DataHelper.getInstance().getDao(clazz);
        try {
            List list = dao.queryForAll();
            if(null != list && 0 != list.size()){
                cb.success(list.get(0), null);
            }else {
                if(clazz.getSimpleName().equals(PostsProvider.class.getSimpleName())){
                    getRefreshPosts(cb);
                }else if(clazz.getSimpleName().equals(SlidersProvider.class.getSimpleName())){
                    getRefreshSliders(cb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            cb.failure(null);
        }
    }

    public void getRefreshPosts(Callback<PostsProvider> cb){
        postPageConut = 1;
        getMorePosts(cb, true);
    }

    public void getMorePosts(final Callback<PostsProvider> cb, final boolean needStore){
        NetBusiness.netService.getPosts(postPageConut++, new Callback<RawPosts>() {
            @Override
            public void success(RawPosts rawPosts, Response response) {
                cb.success(posts2PostsProvider(rawPosts, needStore), response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    private PostsProvider posts2PostsProvider(RawPosts rawPosts, boolean needStore){
        ArrayList<PostsProvider.Post> postArrayList = new ArrayList<>();
        for(RawPosts.PostsEntity postsEntity : rawPosts.getPosts()){
            PostsProvider.Post post = new PostsProvider.Post();

            List<String> videoLink = postsEntity.getCustom_fields().getVideo_link();
            post.setVideoLink(videoLink == null ? null : videoLink.get(0));

            post.setId(postsEntity.getId() + "");
            post.setImageUrl(postsEntity.getCustom_fields().getThumbnail_url().get(0));
            post.setTitle(postsEntity.getTitle());
            post.setTime(formatTime(postsEntity.getDate()));
            post.setAuthor(postsEntity.getAuthor().getNickname());
            post.setViewCount(postsEntity.getCustom_fields().getViewer_count().get(0));
            post.setCommentCount((postsEntity.getCustom_fields().getFloat_comment().get(0).split("\\u0024").length - 1) + "");
            post.setShortDiscription(postsEntity.getCustom_fields().getIntro().get(0));

            ArrayList<String> tagArrayList = new ArrayList<>();
            for(RawPosts.PostsEntity.TagsEntity tagsEntity : postsEntity.getTags() ){
                tagArrayList.add(tagsEntity.getTitle());
            }
            post.setTags(tagArrayList);

            postArrayList.add(post);
        }

        PostsProvider postsProvider = new PostsProvider();
        postsProvider.setPostArrayList(postArrayList);
        postsProvider.setTotalPageCount(rawPosts.getPages());

        if(needStore){
            try {
                DataHelper.getInstance().clearTable(PostsProvider.class);
                DataHelper.getInstance().getDao(PostsProvider.class).create(postsProvider);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return postsProvider;
    }

    private SlidersProvider RawSlider2SliderProvider(RawSliders rawSliders, boolean needStore){
        ArrayList<SlidersProvider.Slider> sliderArrayList = new ArrayList<>();
        for(RawSliders.SlidesEntity.PostsEntity sliderPost : rawSliders.getSlides().getPosts()){
            SlidersProvider.Slider slider = new SlidersProvider.Slider();
            slider.setTitle(sliderPost.getTitle());

            List<String> videoList = sliderPost.getCustom_fields().getVideo_link();
            slider.setVideoLink(null == videoList ? null : videoList.get(0));

            slider.setImageUrl(sliderPost.getCustom_fields().getScreen_image_url().get(0));
            slider.setLinkAdressId(findSilderId(sliderPost.getCustom_fields().getLinkaddr().get(0)));
            sliderArrayList.add(slider);
        }
        SlidersProvider slidersProvider = new SlidersProvider();
        slidersProvider.setSliderArrayList(sliderArrayList);

        if(needStore){
            DataHelper.getInstance().clearTable(SlidersProvider.class);
            try {
                DataHelper.getInstance().getDao(SlidersProvider.class).create(slidersProvider);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return slidersProvider;
    }

    private String findSilderId(String s){
        Matcher matcher = mPattern.matcher(s);
        if(matcher.find()){
            return matcher.group(1);
        }else {
            MApplication_.getInstance().showMsg(MApplication.TOAST_ALERT, "抱歉，服务器开了一个小差");
            return "";
        }
    }

    private String formatTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
             Date date = format.parse(time);
             SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
             return timeFormat.format(date);
          } catch (ParseException e) {
              e.printStackTrace();
             return "0000-00-00";
          }
    }
}


