package digitalcard.digitalcard.Scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.Result;

import digitalcard.digitalcard.PopUpPanel.PopUpPanelLayout;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.CustomAppCompatActivity;

public class CameraActivity extends CustomAppCompatActivity implements digitalcard.digitalcard.Scan.ZXingScannerView.ResultHandler {
    private digitalcard.digitalcard.Scan.ZXingScannerView mScannerView;
    private digitalcard.digitalcard.Scan.FocusHandler focusHandler;
    Button mScan;
    EditText mSerialNumber;

    public static int               OPEN_FRAGMENT_SMARTWARRANTY   = 0;

    private PopUpPanelLayout mSlidingLayout;
    private boolean                 mSlidingOpen            = false;
    private int                     flag_fragment           = -1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_camera);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        if(ContextCompat.checkSelfPermission(CameraActivity.this , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 0);
        }

        mScannerView = new digitalcard.digitalcard.Scan.ZXingScannerView(this);
        focusHandler = new digitalcard.digitalcard.Scan.FocusHandler(new Handler(), mScannerView);
        contentFrame.addView(mScannerView);

        mScan = (Button) findViewById(R.id.buttonScanner);
        mScan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(1 , intent);
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(false);
        mScannerView.startCamera();
        focusHandler.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

//        Bundle a = new Bundle();
//        a.putString("text" , rawResult.getText());
//        AddWarrantyFormFragment frag = new AddWarrantyFormFragment();
//        frag.setArguments(a);


        //setContentView(R.layout.fragment_smartphone_warranty);
//        mSerialNumber = (EditText) findViewById(R.id.codeSerial);
        String stringcode = rawResult.getText();
        //mSerialNumber.setText(stringcode);


        Intent intent = this.getIntent();
        intent.putExtra("CODE_DATA" ,rawResult.getText());

        setResult(2, intent);
        finish();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(CameraActivity.this);
            }
        }, 2000);
    }


//    @Override
//    public void handleResult(Result rawResult) {
////        Toast.makeText(this, "Contents = " + rawResult.getText() +
////                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
//
////        Bundle a = new Bundle();
////        a.putString("text" , rawResult.getText());
////        AddWarrantyFormFragment frag = new AddWarrantyFormFragment();
////        frag.setArguments(a);
//
//
//        //setContentView(R.layout.fragment_smartphone_warranty);
////        mSerialNumber = (EditText) findViewById(R.id.codeSerial);
//        String stringcode = rawResult.getText();
//        //mSerialNumber.setText(stringcode);
//
//
//        Intent intent = this.getIntent();
//        intent.putExtra("CODE_DATA" ,rawResult.getText());
//
//        setResult(2, intent);
//        finish();
//
//        // Note:
//        // * Wait 2 seconds to resume the preview.
//        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(CameraActivity.this);
//            }
//        }, 2000);
//    }
}