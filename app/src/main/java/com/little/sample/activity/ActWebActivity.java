package com.little.sample.activity;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.little.sample.R;
import com.little.sample.base.BaseActivity;
import com.little.sample.base.BaseConstant;
import com.little.sample.util.SystemUtil;
import com.little.visit.listener.IOnRetryListener;
import com.little.visit.util.HttpUtil;
import com.little.visit.util.ViewUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.GONE;


public class ActWebActivity extends BaseActivity {
    @InjectView(R.id.activity_act_web_webView)
    public WebView webView;
    @InjectView(R.id.visit_link_container)
    LinearLayout visitLinkContainer;
    @InjectView(R.id.visit_link_loading_layout)
    LinearLayout visitLinkLoadingLayout;
    @InjectView(R.id.visit_link_progress)
    ProgressBar visitLinkProgress;
    @InjectView(R.id.activity_act_web_title)
    TextView activityActWebTitle;
    private String titleStr, urlStr;
    public ViewUtil viewTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTaskTag(getClass().getSimpleName());
        setContentView(R.layout.activity_act_web);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            //解决第二次进入无法跳转问题
            if (webView != null) {
                webView.getSettings().setJavaScriptEnabled(false);
                webView.setVisibility(GONE);
                webView.removeAllViews();
                webView.destroy();
                ViewGroup parent = (ViewGroup) webView.getParent();
                if (parent != null) {
                    parent.removeView(webView);
                }
                webView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void init() {
        viewTool = new ViewUtil();
        visitLinkLoadingLayout.setVisibility(GONE);
        try {
            titleStr = getIntent().getStringExtra(BaseConstant.INTENT_TYPE);
            urlStr = getIntent().getStringExtra(BaseConstant.INTENT_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        activityActWebTitle.setText(titleStr);
        setClient(webView);
        SystemUtil.setWebSettingNoZoom(webView);
        readHtml();
    }


    /**
     * 从网络上获取协议
     */
    private void readHtml() {
        try {
            if (!HttpUtil.isNet(this)) {
                viewTool.addErrorView(this, getString(R.string.visit3),
                        visitLinkContainer, visitLinkLoadingLayout, onRetryListener);
                return;
            }
            webView.clearHistory();
            webView.loadUrl(urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setClient(WebView webView) {
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
    }

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                visitLinkProgress.setVisibility(View.GONE);
            } else {
                if (visitLinkProgress.getVisibility() == View.GONE)
                    visitLinkProgress.setVisibility(View.VISIBLE);
                visitLinkProgress.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {

        //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

//        //重写此方法才能够处理在浏览器中的按键事件
//        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//            return super.shouldOverrideKeyEvent(view, event);
//        }

        //报告错误信息
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            viewTool.addErrorView(ActWebActivity.this, "发生错误：" + errorCode + "\n错误描述：" + description,
                    visitLinkContainer, visitLinkLoadingLayout, onRetryListener);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        //更新历史记录
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {

            super.doUpdateVisitedHistory(view, url, isReload);
        }

        //在页面加载开始时调用
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            viewTool.addLoadView(ActWebActivity.this, getString(R.string.visit0), visitLinkContainer, visitLinkLoadingLayout);
            visitLinkContainer.setVisibility(View.VISIBLE);
            visitLinkProgress.setVisibility(View.VISIBLE);
            visitLinkProgress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }

        //在页面加载结束时调用
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (visitLinkContainer != null && visitLinkLoadingLayout != null) {
                viewTool.removeLoadView(visitLinkContainer, visitLinkLoadingLayout);
            }
        }


    };

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finishSelf();
        }
    }

    private IOnRetryListener onRetryListener = new IOnRetryListener() {

        @Override
        public void onRetry() {
            readHtml();
        }

        @Override
        public void onOption() {

        }
    };


}