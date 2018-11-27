package com.example.user.kakao.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.user.kakao.R;

public class Movie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie);
        final Context ctx = Movie.this;
        //외부 URL 을 끌어올 것인데 이제 WebView가 쓰인다.
        //인터넷 사용에 대해 권한을 허가받아야 한다. - Manifest.xml이용

        WebView webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.youtube.com/");
    }
}
