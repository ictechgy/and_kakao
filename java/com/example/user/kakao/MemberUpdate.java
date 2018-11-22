package com.example.user.kakao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context ctx = MemberUpdate.this;

        Intent intent = this.getIntent();
        String spec = intent.getExtras().getString("spec");
        Log.d("spec값", spec);
        String[] arr = spec.split("/");
        //넘어오는 값 - m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.phone+"/"+m.photo;

        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        EditText phone = findViewById(R.id.phone);
        EditText addr = findViewById(R.id.addr);

        addr.setHint(arr[1]);
        email.setHint(arr[2]);
        name.setHint(arr[3]);
        phone.setHint(arr[5]);



    }
}
