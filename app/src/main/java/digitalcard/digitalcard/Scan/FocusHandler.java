package digitalcard.digitalcard.Scan;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 4/4/17.
 */

public class FocusHandler implements Runnable {

    private final int FOCUS_OFF_TIME = 2000;
    private final int FOCUS_ON_TIME = 20000;
    private boolean flag = false;
    private boolean state = false;
    private Handler handler;
    private WeakReference<digitalcard.digitalcard.Scan.ZXingScannerView> scannerView;

    public FocusHandler(Handler handler, digitalcard.digitalcard.Scan.ZXingScannerView scannerView){
        this.handler = handler;
        this.flag = false;
        this.scannerView = new WeakReference<>(scannerView);
    }

    public void start(){
        state = true;
        this.handler.post(this);
    }

    public void stop(){
        state = false;
        scannerView.clear();
    }

    @Override
    public void run() {
        if (!state || this.scannerView.get() == null){
            return;
        }

        int time;
        if (!flag){
            this.scannerView.get().setAutoFocus(flag);
            time = FOCUS_OFF_TIME;
        }
        else{
            this.scannerView.get().setAutoFocus(flag);
            time = FOCUS_ON_TIME;
        }

        flag = !flag;
        handler.postDelayed(this, time);
    }
}