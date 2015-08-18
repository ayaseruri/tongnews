package org.x.tongnews.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.x.tongnews.R;
import org.x.tongnews.global.MApplication_;

/**
 * Created by ayaseruri on 15/7/31.
 */
public class BaseActivity extends AppCompatActivity {

    private int statusBarColor;

    @Override
    public void setContentView(int layoutResID) {

        View statusBarView = LayoutInflater.from(this).inflate(R.layout.status_bar, null);
        ImageView statusBarImageView = (ImageView)statusBarView.findViewById(R.id.status_bar);
        statusBarImageView.setBackgroundColor(0 == statusBarColor ? getResources().getColor(R.color.main_color) : statusBarColor);
        statusBarImageView.getLayoutParams().height = MApplication_.getInstance().getStatusBarHeight();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(statusBarView);
        linearLayout.addView(LayoutInflater.from(this).inflate(layoutResID, null),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        super.setContentView(linearLayout);
    }

    public void setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
    }
}
