package com.example.user.kakao.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

public class Email {
    private Context ctx;
    private AppCompatActivity activity;

    public Email(Context ctx, AppCompatActivity activity) {
        this.ctx = ctx;
        this.activity = activity;
    }

    public void sendEmail(String email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"+email));  //아까는 이걸 intent 객체생성시 같이 넣었는데 이번에는 따로 넣네
        intent.setType("text/plain");  //MIME 설정
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello");
        intent.putExtra(Intent.EXTRA_TEXT, "안녕하세요.");

        ctx.startActivity(intent.createChooser(intent, "사용할 어플리케이션 선택"));
        //선택자를 생성해서 실행시킨다.
    }
}
