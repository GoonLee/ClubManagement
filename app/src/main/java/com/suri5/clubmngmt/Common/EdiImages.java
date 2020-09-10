package com.suri5.clubmngmt.Common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.suri5.clubmngmt.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class EdiImages {


    //크기는 그대로 두되, 파일 크기를 기준으로 리사이즈하는 함수.
    public static Bitmap resize_filesize(Context context, Uri uri, int maxSize) {
        int filesize;   //기존의 파일 크기
        int quality = 90;//화질. 초기값은 90
        Bitmap resultBitmap = null;
        try {
            //uri에서부터 파일을 읽어들일수 있는 스트림 생성
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            filesize = inputStream.available(); //파일 크기 조회
            inputStream.close();
            Log.d("이미지 크기", "원본 : " + filesize);

            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File storageDir = new File(path+"/"+context.getString(R.string.app_name)); //내장메모리/폴더명 에 저장]
            if(!storageDir.exists()){
                if(!storageDir.mkdir()){
                    Log.d("저장소", "저장 실패");
                }
            }

            if (filesize > maxSize){
                int rate = 5+ 15/((maxSize/filesize)+1);
                //퀄리티 로 리사이즈. 매번 퀄리티가 낮아지면서 계속 시도.
                uri = compress(context, uri, quality, maxSize, path, rate);
            }
            resultBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBitmap;
    }

    //파일 압축 함수.
    public static Uri compress(Context context, Uri uri, int quality, int maxsize, String path, int rate){
        int compressSize = 0;
        Bitmap newbitmap;
        Uri newuri = uri;

            File image = new File(path,"resize.jpg");
        try {
            do{

                if(image.exists()) {//만약 이미 이 파일이 존재한다면(1회 이상 했다면)
                    image.delete();//중복되므로 과거 파일은 삭제
                    image = new File(path, "resize.jpg");//그리고 다시 오픈

                    quality -= rate;

                    //uri에서 이미지 불러오기
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    FileOutputStream outputStream = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.close();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//sdk 24 이상, 누가(7.0)
                        newuri = FileProvider.getUriForFile(context,// 7.0에서 바뀐 부분은 여기다.
                                context.getPackageName()+".fileprovider", image);
                    } else {//sdk 23 이하, 7.0 미만
                        newuri = Uri.fromFile(image);
                    }

                    InputStream inputStream = context.getContentResolver().openInputStream(newuri);
                    compressSize = inputStream.available(); //파일 크기 조회
                    inputStream.close();

                    Log.d("리사이즈 결과",compressSize + " " + maxsize + " " + quality);
                }

            }while(compressSize > maxsize);

            String text = "저장 성공. 크기 : " + compressSize;
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
            Log.d("리사이즈 결과",text);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newuri;
    }

}
