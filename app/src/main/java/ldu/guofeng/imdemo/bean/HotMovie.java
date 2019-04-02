package ldu.guofeng.imdemo.bean;

/**
 * Created by Administrator on 2019/3/30.
 */

public class HotMovie {
    private String title;
    private String score;
    private String imgurl;

    public HotMovie(String title, String score, String imgurl) {
        this.title = title;
        this.score = score;
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
