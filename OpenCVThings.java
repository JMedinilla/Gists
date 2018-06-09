private JavaCameraView openCamera;
private Mat frame;

CameraBridgeViewBase.CvCameraViewListener cameraViewListener = new CameraBridgeViewBase.CvCameraViewListener() {

    @Override
    public void onCameraViewStarted(int width, int height) {
      frame = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
      if (frame != null) {
        frame.release();
      }
      frame = null;
    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
      if (inputFrame != null) {
        if (!inputFrame.empty()) {
          inputFrame.copyTo(frame);
          
          /*
          Crear zona de interés interna
          
          int rows = (int) frame.size().height;
          int cols = (int) frame.size().width;
          int top = (rows * 38) / 100;
          int left = (cols * 16) / 100;
          if (innerFrame == null) {
              innerFrame = frame.submat(top, rows - top, left, cols - left);
          }
          
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  if (innerFrame != null) {
                      overlay.getLayoutParams().width = innerFrame.width() + 4;
                      overlay.getLayoutParams().height = innerFrame.height() + 4;
                      overlay.requestLayout();
                  }
              }
          });
          
          */
          return frame;
        }
      }
      return null;
    }
};

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_main);
    
    openCamera = (JavaCameraView) findViewById(R.id.open_camera);
    openCamera.setCvCameraViewListener(cameraViewListener);
}
@Override
protected void onPause() {
    if (openCamera != null) {
      openCamera.disableView();
    }
    super.onPause();
}
  
@Override
protected void onResume() {
    super.onResume();
    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, loaderCallback);
}
  
@Override
protected void onDestroy() {
    super.onDestroy();
    if (openCamera != null) {
      openCamera.disableView();
    }
}
  
private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
    @Override
    public void onManagerConnected(int status) {
      switch (status) {
        case LoaderCallbackInterface.SUCCESS: {
          openCamera.enableView();
        }
          break;
        default: {
          super.onManagerConnected(status);
        }
          break;
      }
    }
};