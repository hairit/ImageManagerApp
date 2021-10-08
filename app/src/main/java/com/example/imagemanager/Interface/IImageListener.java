package com.example.imagemanager.Interface;
import com.example.imagemanager.Image;

import java.util.Date;

public interface IImageListener {
    default void imageClick(Date s){ }
}
