package com.hojihojisoftware.yobiyosesanr1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements  FileSelectionDialog.OnFileSelectListener{

    final int PERMISSION_REQUEST_CODE = 100;
    ImageButton mPlayImageButton,mStopImageButton,mRepeatImageButton;
    Button mSelectMusicButton,mIntentRecButton;
    SeekBar mSeekBar;
    TextView mMusicName;

    MediaPlayer mMediaPlayer;

    private String mStrInitialDir = Environment.getExternalStorageDirectory().getPath();
    String mStrMusicName;

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
                repeatAudio();
            }
        });

        mSelectMusicButton = (Button) findViewById(R.id.btnSelectMusic);
        mSelectMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAudio();
            }
        });

        mIntentRecButton = (Button) findViewById(R.id.btnIntentRec);
        mIntentRecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentRec();
            }
        });

        //シークバーの初期設定
        mSeekBar = (SeekBar)findViewById(R.id.seekBar);

    }

    //各種メソッド
    private void selectAudio(){
        FileSelectionDialog dig = new FileSelectionDialog(this,this);
        dig.show(new File(mStrInitialDir));
    }

    private boolean audioSetup(){
       boolean fileCheck = false;

       //再生中はmIntentRecButton、mSelectMusicButtonを無効
        mIntentRecButton.setEnabled(false);
        mSelectMusicButton.setEnabled(false);

        mMediaPlayer = new MediaPlayer();

        if(mStrMusicName == null){
            Toast.makeText(MainActivity.this,"音声ファイルを選択してください",Toast.LENGTH_LONG).show();
        }else{
            try{
                mMediaPlayer.setDataSource(mStrMusicName);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        //音量調整を端末のボタンで行えるようにする
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        try{
            mMediaPlayer.prepare();
            fileCheck = true;
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        //音楽に合わせ、シークバーの上限値を設定
        mSeekBar.setMax(mMediaPlayer.getDuration());
        return fileCheck;
    }


    public void playAudio(){
        if(audioSetup()){
            Toast.makeText(getApplication(),"再生を開始します",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplication(),"再生できません",Toast.LENGTH_LONG).show();
        }
    }

    public void stopAudio(){
        if(mMediaPlayer == null){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;

            //mIntentRecButton、mSelectMusicButtonを有効にする
            mIntentRecButton.setEnabled(true);
            mSelectMusicButton.setEnabled(true);
        }
    }

    public void repeatAudio(){
        Toast.makeText(MainActivity.this,"repeatAudio()は未実装です！",Toast.LENGTH_LONG).show();
    }

    public void intentRec(){
        Toast.makeText(MainActivity.this,"intentRec()は未実装です！",Toast.LENGTH_LONG).show();
    }

    //ファイル選択時の関数
    public void onFileSelect(File file){
        Toast.makeText(MainActivity.this,"選択ファイル"+file.getPath(),Toast.LENGTH_LONG).show();
        mStrInitialDir = file.getParent();

        mMusicName = findViewById(R.id.tvMusicName);
        mStrMusicName = file.getAbsolutePath();
        mMusicName.setText(mStrMusicName);

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
