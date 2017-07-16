package com.example.android.news;


public class News {
    private String mimg;
    private String mtitle;
    private String mauthor;
    private String msection;
    private String mdate;
    private String murl;
    public News(String img,String title,String author,String section,String date,String url)
    {
        mimg=img;
        mtitle=title;
        mauthor=author;
        msection=section;
        mdate=date;
        murl=url;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getMurl() {
        return murl;
    }

    public String getMimg() {
        return mimg;
    }

    public String getMsection() {
        return msection;
    }

    public String getMdate() {
        return mdate;
    }

    public String getMauthor() {
        return mauthor;
    }
}
