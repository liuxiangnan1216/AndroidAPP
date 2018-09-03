package com.example.agingmonkeytestv2.fragment;

import java.io.IOException;

import com.example.agingmonkeytestv2.R;

import android.app.Fragment;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-10.
 ************************************************************************/



public class CameraVideoFragment extends Fragment implements SurfaceHolder.Callback {
    public static final String TAG = "lxn-CameraVideoFragment";
    private Camera mCamera;
    private int mCameraId;
    private SurfaceHolder mHolder;
    private boolean mIsPrepare;
    private boolean mIsRecording;
    private MediaRecorder mMediaRecorder;
    private String mNextVideoAbsolutePath;
    private SurfaceView mSurface;

    public static CameraVideoFragment newInstance(int cameraId) {
        Bundle args = new Bundle();
        args.putInt("CameraId", cameraId);
        CameraVideoFragment fragment = new CameraVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCameraId = getArguments().getInt("CameraId", 0);
    }


    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
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
        releaseMediaRecorder();
        releaseCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        this.mCamera = Camera.open(this.mCameraId);
        try {
            this.mCamera.setPreviewDisplay(this.mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mCamera.startPreview();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
        }
    }

    public void startRecordingVideo() {
        if (!this.mIsRecording && setupMediaRecorder()) {
            this.mMediaRecorder.start();
            this.mIsRecording = true;
        }
    }

    public void stopRecordingVideo() {
        if (this.mIsRecording) {
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.stop();
            }
            releaseMediaRecorder();
            if (this.mCamera != null) {
                this.mCamera.lock();
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(CameraVideoFragment.TAG, "save video " + CameraVideoFragment.this.mNextVideoAbsolutePath);
                        Toast.makeText(CameraVideoFragment.this.getActivity(), "Video save success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            this.mIsRecording = false;
        }
    }

    private boolean setupMediaRecorder() {
        this.mMediaRecorder = new MediaRecorder();
        if (this.mCamera == null) {
            releaseMediaRecorder();
            return false;
        }
        this.mCamera.unlock();
        this.mMediaRecorder.setCamera(this.mCamera);
        this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        this.mMediaRecorder.setVideoSource(MediaRecorder.AudioSource.MIC);
        this.mMediaRecorder.setProfile(CamcorderProfile.get(1));
        this.mNextVideoAbsolutePath = getVideoFilePath();
        this.mMediaRecorder.setOutputFile(this.mNextVideoAbsolutePath);
        this.mMediaRecorder.setPreviewDisplay(this.mHolder.getSurface());
        try {
            this.mMediaRecorder.prepare();
            this.mIsPrepare = true;
            return true;
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e2) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e2.getMessage());
            releaseMediaRecorder();
            return false;
        }
    }

    private void releaseMediaRecorder() {
        if (this.mMediaRecorder != null) {
            if (this.mIsPrepare) {
                this.mMediaRecorder.reset();
            }
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
            if (this.mCamera != null) {
                this.mCamera.lock();
            }
        }
    }

    private void releaseCamera() {
        if (this.mCamera != null) {
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    private String getVideoFilePath() {
        if (getActivity() == null) {
            return "/sdcard//video_" + System.currentTimeMillis() + ".mp4";
        }
        return getActivity().getExternalFilesDir(null).getAbsolutePath() + "/video_" + System.currentTimeMillis() + ".mp4";
    }
}
