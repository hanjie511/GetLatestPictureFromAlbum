package com.example.hj.getlatestpicture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/*
获得通过手机拍照的最新的一张照片
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<File> mList=null;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        image=findViewById(R.id.image1);
    }
    public void showPic(View v){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            initData();
           Glide.with(MainActivity.this).load(mList.get(0).getAbsolutePath()).into(image);
       }else{
           Toast.makeText(MainActivity.this,"该操作未经授权",Toast.LENGTH_SHORT).show();
       }
    }
    /*
    获得手机相册下面的照片路径，并按照拍照时间升序排列
     */
    private void initData(){
        mList = new ArrayList<File>();
        String url = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera";
        File albumdir = new File(url);
        File[] imgfile = albumdir.listFiles(filefiter);
        int len = imgfile.length;
        for(int i=0;i<len;i++){
            mList.add(imgfile[i]);
        }
        Collections.sort(mList, new FileComparator());
    }
    /*
    文件过滤器，过滤掉非png,jpg,jpeg的其他图片
     */
    private FileFilter filefiter = new FileFilter(){

        @Override
        public boolean accept(File f) {
            String tmp = f.getName().toLowerCase();
            if(tmp.endsWith(".png")||tmp.endsWith(".jpg")
                    ||tmp.endsWith(".jpeg")){
                return true;
            }
            return false;
        }
    };
    /*
    文件排序的接口
     */
    private class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if(lhs.lastModified()<rhs.lastModified()){
                return 1;//最后修改的照片在前
            }else{
                return -1;
            }
        }

    }
}
