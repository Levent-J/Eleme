package com.levent_j.baselibrary.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.levent_j.baselibrary.base.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @auther : levent_j on 2018/4/2.
 * @desc :
 */

public class ImageUtil {

    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_GALLERY = 2;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int REQUEST_CROP = 3;

    private static File mImageFile;

    public static void showImageDialog(final BaseActivity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("选择照片")
                .setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //调用系统相机拍照
                            camera(activity);
                        } else {
                            //从相册选图
                            gallery(activity);
                        }
                    }
                })
                .create()
                .show();
    }

    public static void camera(BaseActivity activity) {
        createImageFile();
        if (!mImageFile.exists()) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    public static void gallery(BaseActivity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_GALLERY);
    }

    public static Uri getImageUri() {
        return Uri.fromFile(mImageFile);
    }

    public static File getFile() {
        return mImageFile;
    }

    public static byte[] File2Bytes(File file) {
        byte[] result = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int n;
            while ((n = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, n);
            }

            inputStream.close();
            byteArrayOutputStream.close();
            result = byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            MyLog.e(e.getMessage());
        } catch (IOException e) {
            MyLog.e(e.getMessage());
        }
        return result;
    }

    public static File Bytes2File(byte[] bytes){
        createImageFile();
        if (!mImageFile.exists()) {
            return mImageFile;
        }
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(mImageFile);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            MyLog.e(e.getMessage());
        } catch (IOException e) {
            MyLog.e(e.getMessage());
        }finally {
            if (bufferedOutputStream!=null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    MyLog.e(e.getMessage());
                }
            }

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    MyLog.e(e.getMessage());
                }
            }
        }

        return mImageFile;

    }

    public static void cropImage(BaseActivity activity, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        //比例 1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        activity.startActivityForResult(intent, REQUEST_CROP);
    }

    public static void createImageFile() {
        mImageFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            mImageFile.createNewFile();
        } catch (IOException e) {
            MyLog.e("createImageFile failed : " + e);
        }
    }

    public static Observable<Boolean> storeImage(final Context context, final Bitmap bitmap) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "qrcode";
                File fileDir = new File(storePath);
                if (!fileDir.exists()){
                    fileDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(fileDir,fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG,60,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    Uri uri = Uri.fromFile(file);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));

                    emitter.onNext(result);
                } catch (FileNotFoundException e) {
                    emitter.onError(e);
                } catch (IOException e) {
                    emitter.onError(e);
                }
            }
        });

    }
}
