package org.x.tongnews.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateDecelerateInterpolator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;
import org.x.tongnews.adapter.PhotographerBodyAdapter;
import org.x.tongnews.adapter.PhotographerHeaderAdapter;
import org.x.tongnews.global.MApplication;

import java.util.ArrayList;
import java.util.Arrays;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by ayaseruri on 15/8/3.
 */
@EFragment(R.layout.fragment_photographer)
public class PhotographerFragment extends Fragment {

    @App
    MApplication mApplication;
    @ViewById(R.id.photographer_fragment_header)
    RecyclerView header;
    @ViewById(R.id.photographer_fragment_body)
    RecyclerView body;

    private ArrayList<String> mPhotoIds = new ArrayList<>();
    private PhotographerBodyAdapter photographerBodyAdapter;
    @AfterViews
    void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mApplication.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        header.setLayoutManager(layoutManager);
        header.setAdapter(new PhotographerHeaderAdapter(mApplication.getApplicationContext(), new PhotographerHeaderAdapter.PhotographerItemOnClick() {
            @Override
            public void onClick(int postion) {
                updatePhotos(postion);
            }
        }));

        body.setLayoutManager(new LinearLayoutManager(mApplication.getApplicationContext()));
        photographerBodyAdapter = new PhotographerBodyAdapter(mApplication.getApplicationContext(), mPhotoIds);
        body.setAdapter(photographerBodyAdapter);
    }

    void updatePhotos(int postion){
        mPhotoIds.clear();
        mPhotoIds.addAll(Arrays.asList(mApplication.getResources().getStringArray(R.array.photographer_photo_urls)[postion].split(",")));
        photographerBodyAdapter.notifyDataSetChanged();
        int cx = (body.getLeft() + body.getRight()) / 2;
        int cy = (body.getTop() + body.getBottom()) / 2;
        int finalRadius = Math.max(body.getWidth(), body.getHeight());
        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(body, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1500);
        animator.start();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
