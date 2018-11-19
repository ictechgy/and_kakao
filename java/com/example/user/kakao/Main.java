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
                /*
                Intent intent = new Intent(ctx, Login.class);  //아까 manifest에서 메인을 띄우려 한것도 intent filter였다.
                //생성자에 인자로 '어디에서 출발해서 어디로 가는 것인지'를 입력해주도록 한다. (from to 형식)
                //이 Main페이지 ctx에서 Login이라는 곳으로 이동시킨다. 이 때 Login 파일은 이미 컴파일되어있어서 class파일이다.
                그 클래스파일로 이동되록 의도시킬 것이다.

                startActivity(intent);  //출발지와 목적지를 지정한 후 해당 정보를 가지고 본격적으로 이동해라 라고 실행시키는 것.

                */

                startActivity(new Intent(ctx, Login.class));    //한줄로 줄이기

            }
        });
    }   //onCreate End

    static interface ExecuteService{
        public void perform();    //보통 perform이라는 이름으로 많이 쓴다.
    }
    static interface ListService{
        public List<?> perform();
    }
    static interface ObjectService{
        public Object perform();
    }

    //인터페이스를 파일로 따로 만들지 않고 여기에다가 바로 만들었다.(편리)
    //static으로 만든 이유는.. 재사용성등을 높이기 위해서?

    static abstract class QueryFactory{  //쿼리를 반드시 작성해서 이 객체를 만들도록? - 추상팩토리 패턴
        Context ctx;                        //이 클래스에 대한 객체를 만들때 무언가를 강제시킴(오버라이딩 등)

        public QueryFactory(Context ctx) {
            this.ctx = ctx;
        }

        public abstract SQLiteDatabase getDatabase();
    }

    static class SqliteHelper extends SQLiteOpenHelper{

        public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DBInfo.DBNAME, null, 1);
            // super(context, name, factory, version);
            //factory는 우리가 만든 것을 쓰겠다는 의미의 null

            this.getWritableDatabase();  //사용자의 정보등을 데이터베이스에 입력할 것이다.
        }


        //Implements 한 메소드들
        //아예 추상으로 형태만 잡혀있는건 Implements라고 하고 조금이라도 내용있는걸 바꾸는걸 오버라이딩이라고 하는건가
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format(
                    " CREATE TABLE IF NOT EXIST %s " +
                            " ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            "   %s TEXT," +
                            ")"
                    ,DBInfo.MBR_TABLE,
                    DBInfo.MBR_SEQ,
                    DBInfo.MBR_NAME,
                    DBInfo.MBR_EMAIL,
                    DBInfo.MBR_PASS,
                    DBInfo.MBR_ADDR,
                    DBInfo.MBR_PHONE,
                    DBInfo.MBR_PHOTO
            );   //s는 string을 의미. 앞뒤 한칸씩 띄운 것은 쿼리문이 붙는 것 방지
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

        }
    }

}
