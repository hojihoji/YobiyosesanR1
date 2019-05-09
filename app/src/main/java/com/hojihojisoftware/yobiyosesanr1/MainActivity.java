package com.hojihojisoftware.yobiyosesanr1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    final int PERMISSION_REQUEST_CODE = 100;
    ImageButton mPlayImageButton,mStopImageButton,mRepeatImageButton;
    SeekBar mSeekBar;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //パーミッション(ACCESS_AUDIO&WRITE_EXTERNAL_STORAGE)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSION,PERMISSION_REQUEST_CODE);

        }

        //ボタンの初期化
        mPlayImageButton = (ImageButton) findViewById(R.id.imbPlay);
        mPlayImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });

        mStopImageButton = (ImageButton) findViewById(R.id.imbStop);
        mStopImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
            }
        });

        mRepeatImageButton = (ImageButton) findViewById(R.id.imbRepeat);
        mRepeatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatAudio;
            }
        });

        Button selectMusicButton = (Button) findViewById(R.id.btnSelectMusic);
        selectMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAudio();
            }
        });

        Button intentRecButton = (Button) findViewById(R.id.btnIntentRec);
        intentRecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intetRec();
            }
        });

        //シークバーの初期設定
        mSeekBar = (SeekBar)findViewById(R.id.seekBar);

    }

    //各種メソッド
    private void selectAudio(){

    }

    private boolean audioSetup(){
        boolean fileCheck = false;

        mMediaPlayer = new MediaPlayer();

    }


    public void playAudio(){
        if(mMediaPlayer == null){
            if(audioSetup()){
                Toast.makeText(getApplication(),"再生を開始します!",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplication(),"再生失敗。音声ファイルが、設定されているか確認してください",Toast.LENGTH_LONG).show();
                return;
            }
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
