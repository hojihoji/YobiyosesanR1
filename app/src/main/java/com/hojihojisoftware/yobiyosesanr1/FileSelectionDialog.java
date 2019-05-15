//参考URL(ファイル選択ダイアログ)：https://www.hiramine.com/programming/android/fileselectiondialog.html
//参照URL(ファイルフィルタ)：http://washieagle.blogspot.com/2009/12/java.html
//参照URL(ファイルフィルタ)：https://www.sejuku.net/blog/20707

package com.hojihojisoftware.yobiyosesanr1;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FileSelectionDialog implements AdapterView.OnItemClickListener {

    //ファイル情報クラス。以下の役割を提供する。
    //①ファイル・ディレクトリの表示名とFileオブジェクトの管理
    //②ソート用の比較関数の提供
    public static class  FileInfo implements Comparable<FileInfo>{
        private String mStrName;
        private File mFile;

        //コンストラクタ
        public FileInfo(String strName,File file){
            mStrName = strName;
            mFile = file;
        }

        //ゲッター
        public String getName(){
            return mStrName;
        }

        public File getFile(){
            return mFile;
        }

        //比較
        public int compareTo(FileInfo another){
            if(mFile.isDirectory()&&!another.getFile().isDirectory()){
                return -1;
            }
            if(!mFile.isDirectory()&&another.getFile().isDirectory()){
                return 1;
            }
            return mFile.getName().toLowerCase().compareTo(another.getFile().getName().toLowerCase());
        }
    }

    //ファイル情報配列アダプタクラス
    //①ファイル情報リストの委譲、管理
    //②ListViewのリスト表示における一要素のビューの生成
    public static class FileInfoArrayAdapter extends BaseAdapter{
        private Context mContext;
        private List<FileInfo> mListFileInfo; // ファイル情報リスト

        //コンストラクタ
        public FileInfoArrayAdapter(Context context,List<FileInfo>listFileInfo){
            super();
            mContext = context;
            mListFileInfo = listFileInfo;
        }

        //ゲッター
        @Override
        public int getCount(){
            return  mListFileInfo.size();
        }

        @Override
        public FileInfo getItem(int position){
            return  mListFileInfo.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        static class ViewHolder{
            TextView textViewFileName;
            TextView textViewFileSize;
        }

        // 一要素のビューの生成
        @Override
        public View getView(int position, View converterView, ViewGroup parent){
            ViewHolder viewHolder;
            if(null == converterView){
                //レイアウト
                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                //ファイル名テキスト
                TextView textViewFileName = new TextView(mContext);
                textViewFileName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                layout.addView(textViewFileName, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                //ファイルサイズテキスト
                TextView textViewFileSize = new TextView(mContext);
                textViewFileName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                layout.addView(textViewFileSize, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                converterView = layout;
                viewHolder = new ViewHolder();
                viewHolder.textViewFileName = textViewFileName;
                viewHolder.textViewFileSize = textViewFileSize;
                converterView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)converterView.getTag();
            }
            FileInfo fileInfo = mListFileInfo.get(position);
            if(fileInfo.getFile().isDirectory()){
                viewHolder.textViewFileName.setText(fileInfo.getName()+"/");
                viewHolder.textViewFileSize.setText("(directory)");
            }else{
                viewHolder.textViewFileName.setText(fileInfo.getName());
                viewHolder.textViewFileSize.setText(String.valueOf(fileInfo.getFile().length() / 1024 ) + " [KB]");
            }
            return converterView;
        }
    }


    //ファイル選択ダイアログクラス(本文)
    //①ダイアログの作成と表示
    //②ダイアログのListView内の項目をクリックしたときの処理
    //③選択したファイルの情報を呼び出し元に返すためのしくみの提供
    private Context mContextParent; // 呼び出し元
    private OnFileSelectListener mListener;//結果受取先
    private AlertDialog mDialog;
    private FileInfoArrayAdapter mFileInfoArrayAdapter;
    public String mAudioPath;

    //コンストラクタ
    public FileSelectionDialog(Context context,OnFileSelectListener listener){
        mContextParent = context;
        mListener = listener;
    }

    //ダイアログの作成と表示
    public void show(File fileDirectory){
        //タイトル
        String lineSeparator = System.getProperty("line.separator");
        String strTitle ="ファイル(3gpp,aac,mp3)を開く" + lineSeparator + fileDirectory.getAbsolutePath();

        //リストビュー
        ListView listView = new ListView(mContextParent);
        listView.setScrollingCacheEnabled(false);
        listView.setOnItemClickListener(this);

        //ファイルリスト
        MyFileFilter fileFilter = new MyFileFilter();      //3gpp,aacのみを検索
        File[] aFile = fileDirectory.listFiles(fileFilter);
        List<FileInfo>listFileInfo = new ArrayList<>();
        if(null != aFile){
            for(File fileTemp:aFile){
                listFileInfo.add(new FileInfo(fileTemp.getName(),fileTemp));
            }
            Collections.sort(listFileInfo);
        }
        //親フォルダに戻るパスを追加
        if(null != fileDirectory.getParent()){
            listFileInfo.add(0,new FileInfo("..",new File(fileDirectory.getParent())));
        }
        mFileInfoArrayAdapter = new FileInfoArrayAdapter(mContextParent,listFileInfo);
        listView.setAdapter(mFileInfoArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContextParent);
        builder.setTitle(strTitle);
        builder.setNegativeButton("キャンセル",null);
        builder.setView(listView);
        mDialog = builder.show();
    }

    //ListView内の項目をクリックしたときの処理
    public void onItemClick(AdapterView<?>parent,View view,int position,long id){
        if(null != mDialog){
            mDialog.dismiss();
            mDialog = null;
        }
        FileInfo fileInfo = mFileInfoArrayAdapter.getItem(position);
        if(fileInfo.getFile().isDirectory()){
            show(fileInfo.getFile());
        }else{
            mListener.onFileSelect(fileInfo.getFile());
        }
    }



    public interface OnFileSelectListener{
        void onFileSelect(File file);
    }

    //ファイルフィルタ
    public class MyFileFilter implements FilenameFilter{
        public boolean accept(File dir,String name){
            //フォルダの場合は表示する
            if(!name.contains(".")){
                return true;
            }
            //ファイルの場合は拡張子3gpp,aac,mp3
            int index = name.lastIndexOf(".");
            String ext = name.substring(index +1).toLowerCase();
            if(ext.equals("3gp")){
                return true;
            }else if(ext.equals("aac")){
                return true;
            }else if(ext.equals("mp3")){
                return true;
            }

            return false;
        }
    }

}
