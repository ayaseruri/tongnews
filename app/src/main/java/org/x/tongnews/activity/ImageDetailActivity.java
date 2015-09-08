package org.x.tongnews.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;

import com.facebook.drawee.view.SimpleDraweeView;

import org.x.tongnews.R;

import andy.ayaseruri.lib.CircularRevealActivity;

public class ImageDetailActivity extends CircularRevealActivity {

    SimpleDraweeView detailImage;

    void init(){
        String imageUrl = getIntent().getStringExtra("image_url");
        detailImage.setImageURI(Uri.parse(imageUrl));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        detailImage = (SimpleDraweeView)findViewById(R.id.detail_image);
        init();
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
