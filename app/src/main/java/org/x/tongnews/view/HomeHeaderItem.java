package org.x.tongnews.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import org.x.tongnews.R;

/**
 * Created by ayaseruri on 15/8/1.
 */
public class HomeHeaderItem extends BaseSliderView {
    public HomeHeaderItem(Context context) {
        super(context);
        this.setScaleType(ScaleType.CenterCrop);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.home_header_item, null);
        ImageView target = (ImageView)v.findViewById(R.id.header_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
