package org.x.tongnews.activity;

import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;

import andy.ayaseruri.circularrevealactivitylib.CircularRevealActivity;

@EActivity(R.layout.activity_image_detail)
public class ImageDetailActivity extends CircularRevealActivity {

    @ViewById(R.id.detail_image)
    SimpleDraweeView detailImage;

    @AfterViews
    void init(){
        String imageUrl = getIntent().getStringExtra("image_url");
        detailImage.setImageURI(Uri.parse(imageUrl));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class MGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return super.onScale(detector);
        }
    }
}
