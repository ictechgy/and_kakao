package com.example.user.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context ctx = MemberDetail.this;

        findViewById(R.id.toList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberList.class));
            }
        });

        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberUpdate.class));
            }
        });

    }

    private class DetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class ItemDetail extends DetailQuery{
        public ItemDetail(Context ctx) {
            super(ctx);
        }

        public Object execute(){
            return super.
                    getDatabase().
                    rawQuery(String.format(
                       " SELECT * FROM %s WHERE %s LIKE %s",
                       DBInfo.MBR_TABLE
                    ),null);
        }

    }



}
