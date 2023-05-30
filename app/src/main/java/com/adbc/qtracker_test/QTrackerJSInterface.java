package com.adbc.qtracker_test;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.adbc.tracker.ADBCTracker;

public class QTrackerJSInterface {

    private Context mContext;

    public QTrackerJSInterface(Context context) {
        mContext = context;
    }

    //만약 init 과 sendOpen 역시 웹에서 호출하고자 하시면 아래의 주석을 풀어주시면 됩니다.
    /*@JavascriptInterface
    public void init() {
        ADBCTracker.init(mContext);
    }
    @JavascriptInterface
    public void sendOpen() {
        ADBCTracker.sendOpen(mContext);
    }*/

    @JavascriptInterface
    public void sendEvent(String eventName) {
        ADBCTracker.sendEvent(mContext, eventName);
    }

    @JavascriptInterface
    public void sendPurchase(int amount, long price, String productId) {
        ADBCTracker.sendPurchase(mContext, amount, price, productId);
    }

    @JavascriptInterface
    public void test() {
//        ADBCTracker.sendTestInstall(mContext, new ADBCTracker.OnTestListener() {
//            @Override
//            public void onResult(boolean b, String s) {
//                Toast.makeText(mContext, "result : " + s, Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
