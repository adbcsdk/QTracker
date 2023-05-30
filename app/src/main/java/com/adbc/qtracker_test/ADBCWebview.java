package com.adbc.qtracker_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

public class ADBCWebview extends WebView {

    private Activity mActivity;

    public ADBCWebview(Context context) {
        super(context);
    }

    public ADBCWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ADBCWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Activity activity) {

        mActivity = activity;

        this.setWebViewClient(new WebClient());
        this.setWebChromeClient(new ChromeClient());

        // JavaScriptInterface 를 웹뷰에 추가 합니다.
        // name 값을 입력하여 javascript 에서 사용 할 이름을 지정합니다.
        this.addJavascriptInterface(new QTrackerJSInterface(mActivity), "QTracker");

        this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setLoadWithOverviewMode(true);

        this.setPadding(0, 0, 0, 0);
        this.setHorizontalScrollBarEnabled(false);
        this.setVerticalScrollBarEnabled(false);
        this.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getSettings().setMixedContentMode(0);

            CookieManager.getInstance().setAcceptCookie(true);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
    }

    public void displayError(int type) {
        this.setVisibility(View.GONE);
    }

    private class WebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.invalidate();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            if(!request.getUrl().toString().endsWith("favicon.ico")) {
                displayError(2);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if(!request.getUrl().toString().endsWith("favicon.ico")) {
                displayError(2);
            }
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            AlertDialog alertDialog = builder.create();
            String message = "Error";
            alertDialog.setTitle(message);
            alertDialog.setMessage(error.toString());
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, message,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, message,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return loadUrl(view, request.getUrl().toString());
        }

        private boolean loadUrl(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

            WebView wv = new WebView(mActivity);
            WebViewTransport transport = (WebViewTransport) resultMsg.obj;
            transport.setWebView(wv);
            resultMsg.sendToTarget();

            return true;
        }
    }
}