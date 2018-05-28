package com.yibao.hospitalapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yibao.hospitalapp.R;
import com.yibao.hospitalapp.okhttp.CommonHttp;
import com.yibao.hospitalapp.okhttp.CommonHttpCallback;
import com.yibao.hospitalapp.okhttp.UrlObject;
import com.yibao.hospitalapp.util.CommonEntity;
import com.yibao.hospitalapp.util.CommonUtils;
import com.yibao.hospitalapp.util.StorageUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
public class OpenCvCameraActivity extends BaseActivity implements CameraBridgeViewBase.CvCameraViewListener {


    JavaCameraView openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int absoluteFaceSize;
    private TextView tvMsg;
    private void initializeOpenCVDependencies() {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
        // And we are ready to go
        openCvCameraView.enableView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cv_camera);
        System.loadLibrary("opencv_java3");
        openCvCameraView = (JavaCameraView) findViewById(R.id.jcv);
        openCvCameraView.setCameraIndex(-1);
        openCvCameraView.setCvCameraViewListener(this);
        tvMsg=(TextView)findViewById(R.id.tv_msg);
        getScreenParam();

    }
//    private void testpay(String bitmap){
//        showPDialog();
//        tvMsg.setText("ceshi...");
////        HashMap<String,String> params=(HashMap<String,String>) getIntent().getSerializableExtra("paymap");
//        HashMap<String,String> params=new HashMap<>();
//        params.put("image",bitmap);
////            params.put("prescriptionIds",userentity.get)
//        new CommonHttp(OpenCvCameraActivity.this,new CommonHttpCallback() {
//            @Override
//            public void requestSeccess(String json) {
//                dismissPDialog();
////                Intent intent=new Intent(OpenCvCameraActivity.this,MsgActivity.class);
////                intent.putExtra("flag",2);
////                startActivity(intent);
////                finish();
//            }
//
//            @Override
//            public void requestFail(String msg) {
//                dismissPDialog();
//                Intent intent=new Intent(OpenCvCameraActivity.this,MsgActivity.class);
//                intent.putExtra("flag",3);
//                Toast.makeText(OpenCvCameraActivity.this,msg,Toast.LENGTH_SHORT).show();
//                finish();
//            }
//
//            @Override
//            public void requestAbnormal(int code) {
//                Toast.makeText(OpenCvCameraActivity.this,getString(R.string.net_error),Toast.LENGTH_SHORT).show();
//                dismissPDialog();
//                finish();
//            }
//
//        }).doRequest("http://192.168.99.33:8080/hospital-dapp/terminal/api/test",params);
//
//    }

    private void pay(String bitmap){
        showPDialog();
        tvMsg.setText(getString(R.string.paying_pls_wating));
        HashMap<String,String> params=(HashMap<String,String>) getIntent().getSerializableExtra("paymap");
        params.put("image",bitmap);
//            params.put("prescriptionIds",userentity.get)
        new CommonHttp(OpenCvCameraActivity.this,new CommonHttpCallback() {
            @Override
            public void requestSeccess(String json) {
                dismissPDialog();
                try {
                    CommonEntity.balance=new JSONObject(CommonHttp.getBodyObj(json)).getString("balance");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(OpenCvCameraActivity.this,MsgActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent);
                finish();
            }

            @Override
            public void requestFail(String msg) {
                dismissPDialog();
                Intent intent=new Intent(OpenCvCameraActivity.this,MsgActivity.class);
                intent.putExtra("flag",3);
                startActivity(intent);
                Toast.makeText(OpenCvCameraActivity.this,msg,Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void requestAbnormal(int code) {
                Toast.makeText(OpenCvCameraActivity.this,getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                dismissPDialog();
                finish();
            }

        }).doRequest(UrlObject.PAYBYPICTURE,params);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.e("log_wons", "OpenCV init error");
        }
        initializeOpenCVDependencies();

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC3);


        // The faces will be a 20% of the height of the screen
        absoluteFaceSize = (int)Math.floor (height * 0.2);

    }

    @Override
    public void onCameraViewStopped() {
    }
    int faceSerialCount=0;
    String s;
    @Override
    public Mat onCameraFrame(Mat aInputFrame) {
        // Create a grayscale image
//        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_BGR2RGB);
        Core.flip(aInputFrame,aInputFrame,1);

        MatOfRect faces = new MatOfRect();

        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(aInputFrame, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();
        int faceCount = facesArray.length;
        if (faceCount > 0) {
            faceSerialCount++;
        } else {
            faceSerialCount = 0;
        }
        if (faceSerialCount > 3&&boolRight&&isNoSeccess) {
            toImg(aInputFrame);
            isNoSeccess=false;
            faceSerialCount = -5000;

        }
//        ImageView iv=(ImageView)findViewById(R.id.iv_show);
      int tlx= 400;
        int tly= 20;
        int brx= 850;
        int bry= 600;
//        int tlx= 220;
//        int tly=20;
//        int brx=450;
//        int bry=300;

        for (int i = 0; i < facesArray.length; i++) {

//            Imgproc.rectangle(aInputFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
//            s=facesArray[i].tl().x+","+facesArray[i].tl().y+","+facesArray[i].br().x+","+facesArray[i].br().y;
            s="";
//            facesArray[i].tl().x;
            if(isNoSeccess) {
                if (facesArray[i].tl().x < tlx ) {
                    testhandler.sendEmptyMessage(5);
                }else if(facesArray[i].br().x > brx){
                    testhandler.sendEmptyMessage(4);
                }else if(facesArray[i].tl().y<tly){
                    testhandler.sendEmptyMessage(6);
                }else if(facesArray[i].br().y>bry){
                    testhandler.sendEmptyMessage(7);
                }
                else{
                    if ( facesArray[i].br().x-facesArray[i].tl().x<350) {
                        testhandler.sendEmptyMessage(3);
                    } else {
                        testhandler.sendEmptyMessage(2);
                    }
                }
            }
        }
        return aInputFrame;
    }
    int toplx;
    int toply;
    int botrx;
    int boyry;
    boolean boolRight;
    boolean isNoSeccess=true;
    private  Bitmap mBitmap;
    private void toImg(Mat rgba){

        MatOfInt info=new MatOfInt(256);
//        Imgcodecs.imwrite(cacheDir.getAbsolutePath() + "OpenCvimwrite.jpg",aInputFrame);
//               bitmap = Bitmap.createBitmap(aInputFrame.cols(), aInputFrame.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(aInputFrame, bitmap);
        mBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rgba, mBitmap);

        testhandler.sendEmptyMessage(0);


    }
    private void saveImg(Bitmap bitmap){
        //先把mat转成bitmap

//        Bitmap mBitmap = null;

        File cacheDir = StorageUtils.getOwnCacheDirectory(
                getApplicationContext(), "yibao/cache");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir.getAbsolutePath() + "OpenCvimwrite.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
//             testhandler.sendEmptyMessage(0);
//            Log.d(TAG, "图片已保存至本地");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private Handler testhandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//           Toast.makeText(MainActivity.this,"刷新",Toast.LENGTH_SHORT).show();
            switch (msg.what){
                case 0:
//                    File cacheDir = StorageUtils.getOwnCacheDirectory(
//                            getApplicationContext(), "yibao/cache");
//                    Bitmap    bitmap= BitmapFactory.decodeFile(cacheDir.getAbsolutePath() + "OpenCvimwrite.jpg");
                    ImageView iv=(ImageView)findViewById(R.id.iv_show);
                    iv.setImageBitmap(mBitmap);
                    mBitmap=cropBitmap(mBitmap);
//                    saveImg(mBitmap);
                    pay(CommonUtils.bitmapToBase64(mBitmap));
                    break;
                case 1:
//                    Toast.makeText(OpenCvCameraActivity.this,"请将脸部置于扫描框内",Toast.LENGTH_SHORT).show();;
                    boolRight=false;
                    tvMsg.setText(getString(R.string.opencv_1)+s);
                    break;
                case 2:
//                    Toast.makeText(OpenCvCameraActivity.this,"正确",Toast.LENGTH_SHORT).show();
                    tvMsg.setText(getString(R.string.opencv_2)+s);
                    boolRight=true;
                    break;
                case 3:
//                    Toast.makeText(OpenCvCameraActivity.this,"正确",Toast.LENGTH_SHORT).show();
                    tvMsg.setText(getString(R.string.opencv_3)+s);
//                    tvMsg.setText(s);
                    boolRight=false;
                    break;
                case  4:
                    tvMsg.setText(getString(R.string.opencv_4)+s);
//                    tvMsg.setText(s);
                    boolRight=false;

                    break;
                case 5:
                    tvMsg.setText(getString(R.string.opencv_5)+s);
//                    tvMsg.setText(s);
                    boolRight=false;
                    break;
                case 6:
                    tvMsg.setText(getString(R.string.opencv_6)+s);
//                    tvMsg.setText(s);
                    boolRight=false;
                    break;
                case 7:
                    tvMsg.setText(getString(R.string.opencv_7)+s);
//                    tvMsg.setText(s);
                    boolRight=false;
                    break;

            }

            return false;
        }
    });
    private Bitmap cropBitmap(Bitmap bitmap){
        int w=bitmap.getWidth();
        int h=bitmap.getHeight();
        Log.e("wh",w+","+h);
        int nw=400;
        int nh=720;
        int retX=w/2-nw/2;
        int retY=h/2-nh/2;
        return Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null, false);
    }


    @Override
    public void onClick(View v) {

    }
}