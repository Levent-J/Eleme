        package com.levent_j.client.common.util;

        import android.graphics.Bitmap;
        import android.graphics.BitmapShader;
        import android.graphics.Canvas;
        import android.graphics.Paint;

        import com.levent_j.baselibrary.utils.BitmapUtils;
        import com.squareup.picasso.Transformation;

        /**
         * @auther : levent_j on 2018/4/8.
         * @desc :
         */
        public class MyCircleImgTransformation implements Transformation {
            private float width;
            private float height;
            public MyCircleImgTransformation(float width,float height) {
                this.width = width;
                this.height = height;
            }

            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap zoomBitmap = BitmapUtils.zoom(source,width,height);
                Bitmap bitmap = BitmapUtils.circleBitmap(zoomBitmap);
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "";
            }
        }
