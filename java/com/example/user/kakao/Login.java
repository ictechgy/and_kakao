package com.example.user.kakao;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context ctx = Login.this;

        final EditText id_space = findViewById(R.id.etID);
        final EditText pw_space = findViewById(R.id.etPass);

        findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = id_space.getText().toString();
                String pw = pw_space.getText().toString();

                Toast.makeText(ctx, "아이디 : "+id+" 비밀번호 : "+pw, Toast.LENGTH_SHORT).show();

                //이제 다음주에 데이터베이스 연결을 해서 아이디 및 비밀번호 검증과정 거칠 것
                }
            });

        findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

    }
}
