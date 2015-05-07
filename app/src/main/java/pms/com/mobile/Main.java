package pms.com.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import pms.com.mobile.globalclass.log.RbLog;
import pms.com.mobile.globalclass.webview.BackPressCloseHandler;
import pms.com.mobile.push.PreferenceUtil;
import pms.com.mobile.push.globalstring;

import java.io.IOException;

public class Main extends Activity {

    String TAG ="Main";
    WebView mWebView;
    private BackPressCloseHandler backPressCloseHandler;

    private GoogleCloudMessaging _gcm;
    private String _regId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        _gcm = GoogleCloudMessaging.getInstance(Main.this);
        _regId = getRegistrationId();

        RbLog.dd(TAG,"Login pushpush!!!!     gcm 받은 레지스터 아이디  = "+_regId);

        if (TextUtils.isEmpty(_regId))
            registerInBackground();

        backPressCloseHandler = new BackPressCloseHandler(this);

        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        final Context myApp = this;

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(myApp)
                        .setTitle("AlertDialog")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();

                return true;
            }

            ;
        });

        mWebView.addJavascriptInterface(new MyBridge(), "AppInterface");
        // 구글홈페이지 지정
        mWebView.loadUrl("http://203.229.186.213:9090/m/login/loginBackAction.do");
        // WebViewClient 지정
        mWebView.setWebViewClient(new WebViewClientClass());

    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class MyBridge{
        public void backStep( ){

            mWebView.loadUrl("javascript:androidBackKey()");

        }
    }

    @Override
    public void onBackPressed() {
        String str = this.mWebView.getUrl();

        RbLog.dd(TAG, "mWebView.getUrl()  = " + str);
        if(str.equals("http://203.229.186.213:9090/m/login/loginBackAction.do")){
            backPressCloseHandler.onBackPressed();
        }else{
            mWebView.loadUrl("javascript:androidBackKey()");
        }

    }

    private String getRegistrationId() {
        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
        if (TextUtils.isEmpty(registrationId)) {
            Log.i("lkm", "|Registration not found.|");
            return "";
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i("lkm", "|App version changed.|");
            return "";
        }
        return registrationId;
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {


                Log.e("lkm", "&&&&&&&&&&&&&     3      &&&&&&&&&&&&&");

                String msg = "";
                try {
                    if (_gcm == null) {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }

                    RbLog.dd(TAG,"sendid  = "+globalstring.SENDER_ID);

                    _regId = _gcm.register(globalstring.SENDER_ID);

                    RbLog.dd(TAG,"sendid  = "+globalstring.SENDER_ID);

                    msg = "Device registered, registration ID=" + _regId;

                    Log.e("jdh","registration ID=    "+ _regId);

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return msg;
            }


            @Override
            protected void onPostExecute(String msg) {
                Log.i("lkm", "|" + msg + "|");

                // registraion id를 preference에 저장한다.
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(String regId) {
        int appVersion = getAppVersion();
        Log.i("lkm", "|" + "Saving regId on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }

}
