package com.example.user.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context ctx = Main.this;

        findViewById(R.id.moveLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SqliteHelper helper = new SqliteHelper(ctx);

                startActivity(new Intent(ctx, Login.class));

            }
        });
    }   //onCreate End


    static interface ExecuteService{
        public void perform();
    }
    static interface ListService{
        public List<?> perform();
    }
    static interface ObjectService{
        public Object perform();
    }



    static abstract class QueryFactory{
        Context ctx;

        public QueryFactory(Context ctx) {
            this.ctx = ctx;
        }

        public abstract SQLiteDatabase getDatabase();
    }


    static class SqliteHelper extends SQLiteOpenHelper{

        public SqliteHelper(Context context) {
            super(context, DBInfo.DBNAME, null, 1);

            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format(
                    " CREATE TABLE IF NOT EXISTS %s " +
                            " ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT" +
                            ")",
                    DBInfo.MBR_TABLE,
                    DBInfo.MBR_SEQ,
                    DBInfo.MBR_NAME,
                    DBInfo.MBR_EMAIL,
                    DBInfo.MBR_PASS,
                    DBInfo.MBR_ADDR,
                    DBInfo.MBR_PHONE,
                    DBInfo.MBR_PHOTO
            );
            Log.d("실행할 쿼리 :", sql);
            db.execSQL(sql);
            Log.d("================================= :", "쿼리 실행");
            String[] names = {"강동원", "윤아", "임수정", "박보검", "송중기"};
            String[] emails = {"kang@naver.com", "yoon@naver.com", "lim@naver.com", "park@naver.com", "song@naver.com"};
            String[] addrs = {"강동구", "서초구", "강남구", "용산구", "중구"};
            for(int i = 0; i<names.length; i++){
                Log.d("입력하는 이름 :", names[i]);
                db.execSQL(String.format(
                        " INSERT INTO %s "+
                        " ( %s ," +
                        "   %s ," +
                        "   %s ," +
                        "   %s ," +
                        "   %s ," +
                        "   %s " +
                        ")VALUES( " +
                        "'%s' ,"+
                        "'%s' ,"+
                        "'%s' ,"+
                        "'%s' ,"+
                        "'%s' ,"+
                        "'%s' "+
                        ")",
                        DBInfo.MBR_TABLE, DBInfo.MBR_NAME, DBInfo.MBR_EMAIL, DBInfo.MBR_PASS,
                        DBInfo.MBR_ADDR, DBInfo.MBR_PHONE, DBInfo.MBR_PHOTO,
                        names[i], emails[i], '1', addrs[i], "010-1234-567"+i, "PHOTO_"+(i+1)
                ));
            }
            Log.d("***************","친구등록완료");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + DBInfo.MBR_TABLE);
            onCreate(db);
        }
    }

}
