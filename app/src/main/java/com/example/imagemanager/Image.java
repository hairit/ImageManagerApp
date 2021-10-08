package com.example.imagemanager;

import java.util.Date;

public class Image {
    private int idImage;
    private Date date;
    private String dataImage;
    public Image(int id , String dataImage , Date date) {
        this.idImage=id;
        this.dataImage=dataImage;
        this.date=date;
    }
    public int getIdImage() {
        return idImage;
    }
    public void setIdImage(int idImage){
        this.idImage = idImage;
    }
    public Date getDate(){
        return date;
    }
    public void setDateAdded(Date date){
        this.date = date;
    }
    public String getDataImage(){
        return dataImage;
    }
    public void setDataImage(String dateImage){
        this.dataImage = dataImage;
    }
}
