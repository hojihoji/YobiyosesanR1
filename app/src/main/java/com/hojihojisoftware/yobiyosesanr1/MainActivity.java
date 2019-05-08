package com.hojihojisoftware.yobiyosesanr1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //パーミッション(ACCESS_AUDIO&WRITE_EXTERNAL_STORAGE)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSION,PERMISSION_REQUEST_CODE);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[]permissions,int[]grantsResults){
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantsResults.length>0){
                for(int i = 0; i < permissions.length; i++){
                    if(permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        if(grantsResults[i] == PackageManager.PERMISSION_GRANTED){
                            Log.d("PERMISSION","外部ストレージアクセスを許可");
                        }else{
                            Log.d("PERMISSION","外部ストレージアクセスを非許可");
                        }
                    }else if(permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
                        if (grantsResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.d("PERMISSION", "マイク使用を許可");
                        } else {
                            Log.d("PERMISSION", "マイク使用を非許可");
                        }
                    }
                }
            }
        }
    }
}
