package org.x.tongnews.object;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ayaseruri on 15/8/9.
 */
@DatabaseTable(tableName = "sliders")
public class SlidersProvider {
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    ArrayList<Slider> sliderArrayList;

    public ArrayList<Slider> getSliderArrayList() {
        return sliderArrayList;
    }

    public void setSliderArrayList(ArrayList<Slider> sliderArrayList) {
        this.sliderArrayList = sliderArrayList;
    }

    public static class Slider implements Serializable{
        String imageUrl;
        String linkAdressId;
        String videoLink;
        String title;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLinkAdressId() {
            return linkAdressId;
        }

        public void setLinkAdressId(String linkAdressId) {
            this.linkAdressId = linkAdressId;
        }

        public String getVideoLink() {
            return videoLink;
        }

        public void setVideoLink(String videoLink) {
            this.videoLink = videoLink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
