package com.frabbi.myfirebasedemo.pdf_reader;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityPdfViewBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PdfViewActivity extends BaseActivity<ActivityPdfViewBinding> {

    @Override
    protected ActivityPdfViewBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityPdfViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getIntent().getStringExtra("title"));
        progressDialog.setMessage("Opening....!!");

        String url= "";
        try {
            url = URLEncoder.encode(getIntent().getStringExtra("fileUrl"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        mBind.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        mBind.webView.getSettings().setJavaScriptEnabled(true);
        String viewUrl = "http://docs.google.com/gview?embedded=true&url="+url;
        Log.i("ViewUrl", "setViewCreated: "+viewUrl);
        mBind.webView.loadUrl(
                viewUrl
        );
    }
}