package com.example.ansan.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText et1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    et1 = (EditText)findViewById(R.id.editText);
    }


    public void onClik(View v) {
        if(isStoragePermissionGranted() == false){
            Toast.makeText(getApplicationContext(),"SD Card 사용 불가",Toast.LENGTH_SHORT).show();
            return;
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = path + "/mydir"; //폴더 생성
        String filename = folder + "/test.txt"; //폴더 안에 파일을 생성

        File file = new File(folder);
        String msg = "";
        switch (v.getId()) {
            case R.id.button: //폴더 생성
                file.mkdir();
                msg = "파일 생성";
                break;
            case R.id.button2: //폴더 삭제
                file.delete();
                msg = "파일 삭제";
                break;
            case R.id.button3: //파일 생성
                try {
                    FileOutputStream fos = new FileOutputStream(filename);
                    fos.write("안녕하세요".getBytes());
                    fos.close();

                    msg = "파일 생성 성공";
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    msg = "파일 생성 오류";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button4: //파일 읽기
                try {
                    FileInputStream fis = new FileInputStream(filename);
                    byte arr[] = new byte[fis.available()];
                    fis.read(arr);
                    fis.close();
                    String str = new String(arr);
                    msg = "파일 내용 = " + str;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    msg = "파일 읽기 오류";

                } catch (IOException e) {
                    e.printStackTrace();

                }
                break;
            case R.id.button5: //폴더 목록 가져오기
                File[] filelist = new File(path).listFiles();
                String str = "";
                for(int i = 0; i < filelist.length; i++){
                    if(filelist[i].isDirectory())
                        str += "<폴더>" + filelist[i].toString() + "\n";
                    else
                        str += "<파일>" + filelist[i].toString() + "\n";
                }
                et1.setText(str);
                msg = "파일 목록 가져오기 성공!!!";
                break;

        }
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    String TAG = "TTT";
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }
}