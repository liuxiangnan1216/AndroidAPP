package com.example.agingmonkeytestv2.fragment;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.agingmonkeytestv2.R;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;



/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-10.
 ************************************************************************/


public class CameraFragment extends Fragment implements SurfaceHolder.Callback {
    public static final String TAG = "lxn-CameraFragment";
    private volatile boolean isSafeTakePicture = false;
    private Camera mCamera;
    private int mCameraId;
    private SurfaceHolder mHolder;
    private String mNextJpgAbsolutePath;
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview();
            try {
                FileOutputStream fos = new FileOutputStream(CameraFragment.this.getNextPicFile());
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            CameraFragment.this.isSafeTakePicture = true;
        }
    };
    private SurfaceView mSurface;
    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d("ShutterCallback", "…onShutter…");
        }
    };

    public static CameraFragment newInstance(int cameraId) {
        Bundle args = new Bundle();
        args.putInt("CameraId", cameraId);
        CameraFragment fragment = new CameraFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCameraId = getArguments().getInt("CameraId", 0);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_video, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mSurface = (SurfaceView) view.findViewById(R.id.camera_surface);
        this.mHolder = this.mSurface.getHolder();
        this.mHolder.addCallback(this);
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        releaseCamera();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        releaseCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        this.mCamera = Camera.open(this.mCameraId);
        Camera.Parameters params = this.mCamera.getParameters();
        params.setPictureFormat(PixelFormat.JPEG);
        this.mCamera.setDisplayOrientation(90);
        this.mCamera.setParameters(params);
        try {
            this.mCamera.setPreviewDisplay(this.mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mCamera.startPreview();
        this.isSafeTakePicture = true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.isSafeTakePicture = false;
        }
    }

    private void releaseCamera() {
        this.isSafeTakePicture = false;
        if (this.mCamera != null) {
            this.mCamera.setPreviewCallback(null);
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
        Log.d(TAG, "releaseCamera");
    }

    private String getNextPicFile() {
        if (getActivity() == null) {
            return "/sdcard//pic_" + System.currentTimeMillis() + ".jpg";
        }
        return getActivity().getExternalFilesDir(null).getAbsolutePath() + "/pic_" + System.currentTimeMillis() + ".jpg";
    }

    public void takePicture() {
        if (this.isSafeTakePicture) {
        	Log.d(TAG, "takePicture");
            this.isSafeTakePicture = false;
            this.mNextJpgAbsolutePath = getNextPicFile();
            this.mCamera.takePicture(null, null, this.mPicture);
            return;
        }
        Log.d(TAG, "it's not safe to take picture, will return and wait");
    }

    public void saveJpeg(Bitmap bm, String path) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
            bm.compress(Bitmap.CompressFormat.JPEG, 10, bos);
            bos.flush();
            bos.close();
            bm.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

