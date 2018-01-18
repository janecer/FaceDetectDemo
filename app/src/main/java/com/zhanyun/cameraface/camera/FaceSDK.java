package com.zhanyun.cameraface.camera;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FaceSDK {
    private static final String TAG = "FaceSDK";

    public static List<Rect> detectionBitmap(Bitmap bitmap,int screenWidth,int screenHeight) {
        // 设置你想检测的数量，数值越大错误率越高，所以需要置信度来判断,但有时候置信度也会出问题
        int MAX_FACES = 5;
        FaceDetector faceDet = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACES);
        // 将人脸数据存储到faceArray中
        FaceDetector.Face[] faceArray = new FaceDetector.Face[MAX_FACES];
        // 返回找到图片中人脸的数量，同时把返回的脸部位置信息放到faceArray中，过程耗时,图片越大耗时越久
        int findFaceNum = faceDet.findFaces(bitmap, faceArray);
        Log.e("tag","找到脸部数量:" + findFaceNum);
        if(findFaceNum == 0) {
            return null ;
        }
        List<Rect> rects = new ArrayList<>() ;
        PointF pf = new PointF();
        Rect r = null;
        float scaleX  = screenWidth * 1.0f/bitmap.getWidth(); //这里需要 算出TexutView的宽度跟 抓取每一帧图像宽度的比例，主要是为了让图像的某个坐标转化实际TexutView的坐标
        float scaleY = screenHeight * 1.0f/ bitmap.getHeight(); //高度同理
        float scale = Math.max(scaleX,scaleY) ;
        Log.i("janecer"," bm W:" + bitmap.getWidth() +" H:" + bitmap.getHeight() + " scaleX : " + scaleX +" scaleY: " + scaleY);
        for (FaceDetector.Face face : faceArray) {
            r = new Rect() ;
            Log.i(TAG,"face 位null吗 ：" + (face == null)) ;
            if (face == null) {
                continue;
            }
            face.getMidPoint(pf);
            Log.i("janecer", "confidence:"+face.confidence());
            if(face.confidence() < 0.3){
                continue;
            }
            // 这里的框，参数分别是：左上角的X,Y 右下角的X,Y
            // 也就是左上角（r.left,r.top），右下角( r.right,r.bottom)。
            // 该宽度是两眼珠黑色外边距
            float eyesDistance1 = face.eyesDistance();
            r.left = (int) (pf.x * scale - eyesDistance1 );
            r.right = (int) (pf.x * scale + eyesDistance1 );
            r.top = (int) (pf.y * scale - eyesDistance1);
            r.bottom = (int) (pf.y * scale + eyesDistance1 * 1.3f);
            Log.d(TAG, r.toString());
            rects.add(r);
        }
        return rects ;
    }

}
