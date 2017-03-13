package com.bj.yt.newapplication.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bj.yt.newapplication.R;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

public class ThreeDFragment extends BaseFragment {
    private  View view;
    private WebView webView;
    private TextView title_center;
    private XWalkView xwalk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_threed, container, false);

//        setUpViews();
        loadWeb();
        return view;
    }

    private void loadWeb() {
        title_center= (TextView)view.findViewById(R.id.title_center);
        title_center.setText("3D展示");
        xwalk= (XWalkView) view.findViewById(R.id.xwalk);

        //添加对javascript支持
        XWalkPreferences.setValue("enable-javascript", true);
        //开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        //置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        //JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
        // enable multiple windows.
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);


//        xwalk.load("https://baidu.com",null);
        xwalk.load("file:///android_asset/load_remote.html",null);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
//    private void setUpViews() {
//        title_center= (TextView)view.findViewById(R.id.title_center);
//        title_center.setText("3D展示");
//
//        webView= (WebView) view.findViewById(R.id.web_threed);
////        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
//            }//如果不设置这个，JS代码中的按钮会显示，但是按下去却不弹出对话框
//        });
//
//        mWebSettings = webView.getSettings();
//        mWebSettings.setJavaScriptEnabled(true); // 允许加载javascript
//        mWebSettings.setSupportZoom(true); // 允许缩放
//        mWebSettings.setBuiltInZoomControls(true); // 原网页基础上缩放
//        mWebSettings.setUseWideViewPort(true); // 任意比例缩放
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
//        mWebSettings.setAppCacheEnabled(true);//是否使用缓存
//        mWebSettings.setDomStorageEnabled(true);//DOM Storage
//        /**
//         * 在点击请求的是链接时才会调用，
//         * 重写此方法返回true表明点击网页里
//         * 面的链接还是在当前的WebView里跳转，
//         * 不会跳到浏览器上运行。
//         **/
//        webView.setWebViewClient(new WebViewClient() {
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
//                handler.proceed();  // 接受所有网站的证书
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.startsWith("tel")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//
//                    return true;
//                } else {
//                    view.loadUrl(url);
//                }
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });
//        webView.loadUrl("file:///android_asset/load_remote.html");
////        webView.loadUrl("https://baidu.com");
//    }


}
