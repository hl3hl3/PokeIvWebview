package com.re.pokeivwebview;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final String URL_POKEIV_LOGIN = "https://pokeiv.net/login";
    private final String OBJ_ID_AUTHCODE = "code";
    private final String OBJ_ID_LOGINCODE = "code";
    private final String OBJ_ID_LOGINFORM = "#login_form";

    private final int MAC_TRY_TIME = 5;
    private int tryTime = 0;

    private RelativeLayout rlWebview ;
    private FloatingActionButton fabRefreh;
    private WebView webview;
    private String nowCode;

    private WebViewClient webviewCilent = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url){
            log("onPageFinished() - " + url);
            if(url.matches(".+?accounts.google.com/o/oauth2/approval.+")){
                log("onPageFinished() - success to auth result page");
                // get code value by console.log
                webview.loadUrl("javascript:console.log('googlecode='+document.getElementById('"+OBJ_ID_AUTHCODE+"').value+'');");
            }else if(url.equals(URL_POKEIV_LOGIN) && nowCode != null) {
                log("onPageFinished() - login page finish");
                // put code into code value
                handler = new Handler();
                tryTime = 0;
                handler.post(checkInputValueCallback);
            }
        }
    };

    private WebChromeClient wcClient = new WebChromeClient(){
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage){
            log("console.log("+consoleMessage.message()+")");
            if(consoleMessage.message() != null) {
                if (consoleMessage.message().matches("googlecode=.*")) {
                    // get code value
                    nowCode = consoleMessage.message().split("=")[1];
                    // open login page to paste the code
                    webview.loadUrl(URL_POKEIV_LOGIN);
                } else if (consoleMessage.message().matches("code=.*")) {
                    // get login code value, ready to paste
                    handler.removeCallbacks(checkInputValueCallback);
                    webview.loadUrl("javascript: (function(){document.getElementById('"+OBJ_ID_LOGINCODE+"').value='" + nowCode + "';})();");
//                    webview.loadUrl("javascript: (function(){$('"+OBJ_ID_LOGINFORM+"').submit();})();");
                    nowCode = null;
                }
            }
            return super.onConsoleMessage(consoleMessage);
        }
    };

    private Handler handler;
    private Runnable checkInputValueCallback = new Runnable() {
        @Override
        public void run() {
            if(tryTime < MAC_TRY_TIME){
                tryTime ++;
                // get login code value by console.log to check whether the object is ready
                webview.loadUrl("javascript: (function(){console.log('code='+document.getElementById('"+OBJ_ID_LOGINCODE+"').value);})();");
                handler.postDelayed(this, 200);
            }else{
                handler.removeCallbacks(this);
            }
        }
    };

    private View.OnClickListener onClickRefresh = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // add a new webview
            webview = getNewWebview();
            rlWebview.removeAllViews();
            System.gc();
            rlWebview.addView(webview);
            webview.loadUrl(URL_POKEIV_LOGIN);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlWebview = (RelativeLayout)findViewById(R.id.rlWebview);

        fabRefreh = (FloatingActionButton)  findViewById(R.id.fabRefresh);
        fabRefreh.setOnClickListener(onClickRefresh);
        fabRefreh.performClick();
    }

    private WebView getNewWebview(){
        WebView wv = new WebView(this);
        WebSettings websettings = wv.getSettings();
        websettings.setSupportZoom(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        wv.setLayoutParams(lp);
        wv.setWebViewClient(webviewCilent);
        wv.setWebChromeClient(wcClient);
        return wv;
    }

    private void log(String msg){
        if(BuildConfig.DEBUG){
            Log.d(TAG, msg);
        }
    }
}
