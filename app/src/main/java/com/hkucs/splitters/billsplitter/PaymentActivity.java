package com.hkucs.splitters.billsplitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

public class PaymentActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://wallet.google.com");
    }


    public void onBackPressed() {
        // your code.
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
