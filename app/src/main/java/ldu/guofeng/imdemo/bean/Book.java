package ldu.guofeng.imdemo.bean;

/**
 * Created by Administrator on 2019/3/27.
 */

public class Book {
    private String bookname;
    private String author;
    private String score;
    private String discription;
    private String imgurl;
    private String mark;

    public Book(String bookname, String author, String score,  String imgurl, String mark) {
        this.bookname = bookname;
        this.author = author;
        this.score = score;
        this.imgurl = imgurl;
        this.mark = mark;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
