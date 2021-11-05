package com.example.aliatsimactivation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    TextView testView;
    private String BACK,BACK_CROPED,value,stroffile,globalMode,agentNumber,globalsimid,picsdate ;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    private ImageView cropImg;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    private String result,Date,IDnb,FirstName,MiddleName,LastName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cropImg = findViewById(R.id.cropImg);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // get picture file name using date day month hour min and sec
        LocalDateTime picsdT= LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = picsdT.format(format);
        System.out.println("After Formatting: " + formatDateTime);
        picsdate=formatDateTime;

        Intent intent = CameraActivity.this.getIntent();
        globalsimid= intent.getStringExtra("message_key");
        stroffile= intent.getStringExtra("db-offline-to-main");
        globalMode=intent.getStringExtra("globalMode");
        agentNumber=intent.getStringExtra("agentNumber");


        jpegCallback = new Camera.PictureCallback() {
            @SuppressLint("WrongConstant")
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                BACK = "PICK_BACK_"+picsdate;
                try {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), BACK + ".jpg");
                    outStream = new FileOutputStream(file);
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Toast.makeText(getApplicationContext(), "Picture Saved", 2000).show();

                //get the saved bitmap
                File backsave = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), BACK + ".jpg");
                if (backsave.exists()) {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    cropImg.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(backsave)));
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) cropImg.getDrawable();
                    //rotate the saved bitmap
                    Bitmap b = bitmapDrawable.getBitmap();
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap b1 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                    //crop the saved bitmap old one
                    //int x0 = b1.getWidth()/2;
                   // int y0 = b1.getHeight()/2;
                   // int dx = b1.getHeight()/4;
                  //  int dy = b1.getHeight()/4;
                   // Bitmap b2 = Bitmap.createBitmap(b1, x0-dx, y0-dy, x0+dx, y0+dy, null, true);
                  //  cropImg.setImageBitmap(b2);
                  //  TextRecognizer(b2);

                    //crop the saved bitmap
                    int x0 = b1.getWidth()/2;
                    int y0 = b1.getHeight()/2;
                    int dx = b1.getHeight()/4;
                    int dy = b1.getHeight()/4;
                    int dy1 = b1.getHeight()/8;
                    Bitmap b2 = Bitmap.createBitmap(b1, x0-dx, y0+dy1, x0+dx, dy, null, true);
                    cropImg.setImageBitmap(b2);
                    TextRecognizer(b2);


                }
                System.out.println("Capture photo "+result);
                System.out.println("--------------------");
                Intent intent = new Intent(CameraActivity.this, SimRegInfo.class);
                value = BACK;
                intent.putExtra("key_value",value);
                intent.putExtra("key_result",result);
                intent.putExtra("message_key",globalsimid);
                intent.putExtra("db-offline-to-main",stroffile);
                intent.putExtra("globalMode",globalMode);
                intent.putExtra("agentNumber",agentNumber);
                intent.putExtra("keyIDnb",IDnb);
                intent.putExtra("keyFirstName",FirstName);
                intent.putExtra("key1MiddleName",MiddleName);
                intent.putExtra("keyLastName",LastName);
                intent.putExtra("keyDate",Date);

                startActivity(intent);

            }
        };
    }

    public void captureImage(View v) throws IOException {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        CameraBox box = new CameraBox(this);
        addContentView(box, new WindowManager.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT));
        try {
            // open the camera
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            setCamFocusMode();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        param.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        param.setExposureCompensation(0);
        param.setPictureFormat(ImageFormat.JPEG);
        param.setJpegQuality(100);
        for (Camera.Size previewSize: camera.getParameters().getSupportedPreviewSizes())
        {
            param.setPreviewSize(previewSize.width, previewSize.height);
            break;
        }
        camera.setParameters(param);




        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    private void setCamFocusMode(){

        if(null == camera) {
            return;
        }

        // Set Auto focus
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        camera.setParameters(parameters);
    }



    public void TextRecognizer(Bitmap bitmap) {
        try {
            TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!recognizer.isOperational()) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = recognizer.detect(frame);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myItem = items.valueAt(i);
                    sb.append(myItem.getValue());
                    sb.append("\n");
                }
                System.out.println("RESULT = " + sb.toString());
                result = sb.toString();


                //START filling the data in editText
                String data[] = result.split("\n");
                for (int i = 0; i < data.length; i++) {

                    if (data[i].length() > 10 && data[i].length() < 50) {
                        String check = data[i].substring(0, 1);
                        String check1 = data[i].substring(1, 2);
                        String check2 = data[i].substring(2, 3);
                        String check3 = data[i].substring(3, 4);
                        String check4 = data[i].substring(4, 5);
                        String check5 = data[i].substring(5, 6);
                        boolean alpahacheck = isAlpha(check);
                        boolean alpahacheck1 = isAlpha(check1);
                        boolean alpahacheck2 = isAlpha(check2);
                        boolean alpahacheck3 = isAlpha(check3);
                        boolean alpahacheck4 = isAlpha(check4);
                        boolean alpahacheck5 = isAlpha(check5);

                        //FINDING THE DATE ROW
                        if (data[i].contains("<") && alpahacheck == false && alpahacheck1 == false && alpahacheck2 == false && alpahacheck3 == false && alpahacheck4 == false && alpahacheck5 == false) {

                            //START filling date and id number
                            String daterow = data[i].replaceAll("\\s", "");
                            String DOB = daterow.substring(0, 6);
                            String day = DOB.substring(4, 6);
                            String month = DOB.substring(2, 4);
                            String year = DOB.substring(0, 2);
                            String year2 = daterow.substring(8, 10);
                            System.out.println("year2   " + year2.trim());
                            int yy = Integer.parseInt(year2.trim());
                            int finalyear = yy - 18;
                            if (finalyear >= 0) {
                                Date = day + "-" + month + "-20" + year;
                            } else {
                                Date = day + "-" + month + "-19" + year;
                            }
                            String[] Number = daterow.split("<");
                            IDnb = Number[1].substring(2, 10);
                            //END filling date and id number

                            /*//START filling  firstname, middlename, and lastname old one
                            if (data[i + 1].contains("<")) {
                                String namerow = data[i + 1].replaceAll("\\s", "");
                                String[] FullName = namerow.split("<");
                                FirstName = FullName[0];
                                LastName = FullName[1];
                                if (FullName.length > 2) {
                                    FirstName = FullName[0];
                                    MiddleName = FullName[1];
                                    LastName = FullName[2];
                                }
                            } // END filling firstname, middlename and lastname */

                            //START filling  firstname, middlename, and lastname
                            if(data[i+1].contains("<") ){
                                String namerow= data[i+1].replaceAll("\\s", "");
                                String[] FullName = namerow.split("<");
                                if (FullName.length == 3){
                                    FirstName = FullName[0];
                                    MiddleName = FullName[1];
                                    LastName = FullName[2];
                                }
                                if (FullName.length == 2){
                                    FirstName = FullName[0];
                                    MiddleName = FullName[1];
                                }
                                if (FullName.length == 1){
                                    FirstName = FullName[0];
                                }

                            } // END filling firstname, middlename and lastname

                        }

                    }

                } //END filling the data in editText


            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static boolean isAlpha(String s)
    {
        if (s == null) {
            return false;
        }

        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
                return false;
            }
        }
        return true;
    }

}