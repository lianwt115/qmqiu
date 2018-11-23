package com.guoxiaoxing.phoenix.picker.ui.camera.listener;

import java.io.File;

public interface CameraStateListener {

    //when the current displayed camera is the back
    void onCurrentCameraBack();

    //when the current displayed camera is the front
    void onCurrentCameraFront();

    //when the flash is at mode auto
    void onFlashAuto();
    //when the flash is at on
    void onFlashOn();
    //when the flash is off
    void onFlashOff();

    //if the camera is ready to take a photo
    void onCameraSetupForPhoto();

    //if the camera is ready to take a video
    void onCameraSetupForVideo();

    //when the camera state is "ready to record a video"
    void onRecordStateVideoReadyForRecord();
    //when the camera state is "recording a video"
    void onRecordStateVideoInProgress();
    //when the camera state is "ready to take a photo"
    void onRecordStatePhoto();

    //after the rotation of the screen / camera
    void shouldRotateControls(int degrees);

    void onStartVideoRecord(File outputFile);
    void onStopVideoRecord();
}
