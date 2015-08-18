package org.x.tongnews.object;

/**
 * Created by ayaseruri on 15/8/5.
 */
public class VideoInfo {

    /**
     * img : http://i0.hdslb.com/video/b8/b8382f3345dffcf905230f788b4501a0.jpg
     * src : http://cn-zjjh1-dx.acgvideo.com/8/95/4150574.mp4?expires=1438777200&ssig=7cBateJ7bCkHYcDikdpYQw&o=2045085273&appkey=84b739484c36d653&rate=0
     * cid : http://comment.bilibili.com/4150574.xml
     */
    private String img;
    private String src;
    private String cid;

    public void setImg(String img) {
        this.img = img;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getImg() {
        return img;
    }

    public String getSrc() {
        return src;
    }

    public String getCid() {
        return cid;
    }
}
