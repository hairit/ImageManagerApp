package com.example.imagemanager;
import static android.os.Looper.prepare;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.imagemanager.Interface.IImageListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rv_images;
    ArrayList<Image> images;
    SimpleDateFormat formatter;
    ArrayList<Integer> listDay;
    ArrayList<Integer> listMonth;
    ArrayList<Integer> listYear;
    ArrayList<ImagesGroup> listImagesGroupWithDay;
    ArrayList<ImagesGroup> listImagesGroupWithMonth;
    ArrayList<ImagesGroup> listImagesGroupWithYear;
    Button btnDay,btnMonth,btnYear;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepare();
        PermissionListener permissionlistener = new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                //updateImage();
                images = getImage();
                if(images.size()==0){
                    btnDay.setEnabled(false);
                    btnMonth.setEnabled(false);
                    btnYear.setEnabled(false);
                    Toast.makeText(MainActivity.this,"Chưa có ảnh",Toast.LENGTH_SHORT).show();
                    return;
                }
                //displayImageGroupWithDay();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Integer> getListDay() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < images.size(); i++) {
            LocalDate localDate = images.get(i).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int day = localDate.getDayOfMonth();
            if (!list.contains(day)) {
                list.add(day);
            }
        }
        return list;
    }

    private ArrayList<Image> getImage() {
        ArrayList<Image> list_image = new ArrayList<Image>();
        String[] projection = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN
        };
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
//                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
//                list_image.add(data);
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN))));
                list_image.add(new Image(id, data, date));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list_image;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayImageGroupWithDay() {
        listDay = getListDay();
        listImagesGroupWithDay = getImagesGroupWithDay();
        ImageGroupAdapter myAdapter = new ImageGroupAdapter(MainActivity.this,listImagesGroupWithDay,"DAY");
        rv_images.setAdapter(myAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayImageGroupWithMonth() {
        listMonth = getListMonth();
        listImagesGroupWithMonth = getImagesGroupWithMonth();
        ImageGroupAdapter myAdapter = new ImageGroupAdapter(MainActivity.this,listImagesGroupWithMonth,"MONTH");
        rv_images.setAdapter(myAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayImageGroupWithYear() {
        listYear = getListYear();
        listImagesGroupWithYear = getImagesGroupWithYear();
        ImageGroupAdapter myAdapter = new ImageGroupAdapter(MainActivity.this,listImagesGroupWithYear,"YEAR");
        rv_images.setAdapter(myAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<ImagesGroup> getImagesGroupWithMonth(){
        ArrayList<ImagesGroup> listImagesGroup = new ArrayList<>();
        for (int month :
                listMonth) {
            ArrayList<Image> listImageSameMonth = new ArrayList<>();
            for (Image image :
                    images) {
                LocalDate localDate = image.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int monthOfImage = localDate.getMonthValue();
                if (month == monthOfImage) {
                    listImageSameMonth.add(image);
                }
            }
            ImagesGroup imagesGroup = new ImagesGroup(listImageSameMonth.get(0).getDate(), listImageSameMonth);
            listImagesGroup.add(imagesGroup);
        }
        return listImagesGroup;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<ImagesGroup> getImagesGroupWithYear() {
        ArrayList<ImagesGroup> listImagesGroup = new ArrayList<>();
        for (int year :
                listYear) {
            ArrayList<Image> listImageSameYear = new ArrayList<>();
            for (Image image :
                    images) {
                LocalDate localDate = image.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int YearOfImage = localDate.getYear();
                if (year == YearOfImage) {
                    listImageSameYear.add(image);
                }
            }
            ImagesGroup imagesGroup = new ImagesGroup(listImageSameYear.get(0).getDate(), listImageSameYear);
            listImagesGroup.add(imagesGroup);
        }
        return listImagesGroup;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<ImagesGroup> getImagesGroupWithDay() {
        ArrayList<ImagesGroup> listImagesGroup = new ArrayList<>();
        for (int day :
                listDay) {
            ArrayList<Image> listImageSameDay = new ArrayList<>();
            for (Image image :
                    images) {
                LocalDate localDate = image.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int dayOfImage = localDate.getDayOfMonth();
                if (day == dayOfImage) {
                    listImageSameDay.add(image);
                }
            }
            ImagesGroup imagesGroup = new ImagesGroup(listImageSameDay.get(0).getDate(), listImageSameDay);
            listImagesGroup.add(imagesGroup);
        }
        return listImagesGroup;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Integer> getListMonth() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < images.size(); i++) {
            LocalDate localDate = images.get(i).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            if (!list.contains(month)) {
                list.add(month);
            }
        }
        return list;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Integer> getListYear() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < images.size(); i++) {
            LocalDate localDate = images.get(i).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getYear();
            if (!list.contains(month)) {
                list.add(month);
            }
        }
        return list;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void prepare() {
        rv_images = (RecyclerView) findViewById(R.id.rv_images_group);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_images.setLayoutManager(layout);
        rv_images.setHasFixedSize(true);
        listDay = new ArrayList<>();
        listMonth = new ArrayList<>();
        listYear = new ArrayList<>();
        listImagesGroupWithDay = new ArrayList<>();
        listImagesGroupWithMonth = new ArrayList<>();
        listImagesGroupWithYear = new ArrayList<>();
        btnDay = (Button) findViewById(R.id.btn_display_day);
        btnMonth = (Button) findViewById(R.id.btn_display_month);
        btnYear = (Button) findViewById(R.id.btn_display_year);
        btnDay.setOnClickListener(MainActivity.this);
        btnMonth.setOnClickListener(MainActivity.this);
        btnYear.setOnClickListener(MainActivity.this);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if(view == btnDay){
            displayImageGroupWithDay();
        }
        if(view == btnMonth){
            displayImageGroupWithMonth();
        }
        if(view == btnYear){
            displayImageGroupWithYear();
        }
    }
    class ImageGroupAdapter extends RecyclerView.Adapter<ImageGroupAdapter.DataImageViewHolder> {

        private Activity acc;
        private ArrayList<ImagesGroup> imagesGroups;
        private String verifyDisplay;
        public ImageGroupAdapter(Activity acc, ArrayList<ImagesGroup> imagesGroups,String VerifyDisplay) {
            this.acc = acc;
            this.imagesGroups = imagesGroups;
            this.verifyDisplay=VerifyDisplay;
        }
        @NonNull
        @Override
        public DataImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_group_item, parent, false);
            return new DataImageViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull DataImageViewHolder holder, int position){
            if(verifyDisplay=="DAY"){
                formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                String date = formatter.format(imagesGroups.get(position).getDate());
                holder.tv_day.setText("Ngày :"+date);
            }
            if(verifyDisplay=="MONTH"){
                formatter = new SimpleDateFormat("MM-yyyy", Locale.ENGLISH);
                String date = formatter.format(imagesGroups.get(position).getDate());
                holder.tv_day.setText("Tháng :"+date);
            }
            if(verifyDisplay=="YEAR"){
                formatter = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                String date = formatter.format(imagesGroups.get(position).getDate());
                holder.tv_day.setText("Năm : "+date);
            }
            holder.rv_imagesGroup.setLayoutManager(new GridLayoutManager(acc,4));
            ImagesAdapter myAdapter = new ImagesAdapter(acc,imagesGroups.get(position).getListImages());
            holder.rv_imagesGroup.setAdapter(myAdapter);
        }
        @Override
        public int getItemCount() {
            return imagesGroups != null ? imagesGroups.size() : 0;
        }
        public class DataImageViewHolder extends RecyclerView.ViewHolder {
            TextView tv_day;
            RecyclerView rv_imagesGroup;
            DataImageViewHolder(@NonNull View view) {
                super(view);
                tv_day = (TextView) view.findViewById(R.id.tv_day);
                rv_imagesGroup = (RecyclerView) view.findViewById(R.id.rv_images_group);
            }
        }
    }
}