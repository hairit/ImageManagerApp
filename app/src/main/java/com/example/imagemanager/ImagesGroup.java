package com.example.imagemanager;

import java.util.ArrayList;
import java.util.Date;

public class ImagesGroup {
    private Date date;
    private ArrayList<Image> listImages;

    public ImagesGroup(Date date, ArrayList<Image> listImages) {
        this.date = date;
        this.listImages = listImages;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Image> getListImages() {
        return listImages;
    }

    public void setListImages(ArrayList<Image> listImages) {
        this.listImages = listImages;
    }
}
