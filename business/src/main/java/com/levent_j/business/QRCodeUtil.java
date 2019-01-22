package com.levent_j.business;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * @auther : levent_j on 2018/4/5.
 * @desc :  二维码工具类
 */

public class QRCodeUtil {

    //生成bitmap
    public static Bitmap createBitmap(String str,int size){
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE,size,size);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String createContent(String shopId,String seatId){
        String str = shopId + "/" + seatId;
        return str;
    }


}
