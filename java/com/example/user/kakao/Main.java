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

                //이 버튼을 눌렀을 때 데이터베이스가 먼저 만들어지도록 하자.
                SqliteHelper helper = new SqliteHelper(ctx);
                //생성자가 작동하면서 데이터베이스 파일이 만들어지고, 테이블이 존재하지 않았으니 onCreate()또한 자동으로 실행시킨다.
                //helper라는 객체를 만드는 것은 곧 SQLite DB를 만드는 것이다.

                //onCreate 라는 것은 말 그대로 객체가 만들어지고 호출당하면 실행시키는 메소드이다.
                //Main이든 SqliteHelper든

                startActivity(new Intent(ctx, Login.class));

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

            this.getWritableDatabase();  //요건 없어도 될 것 같은데?
            //SQLiteDatabase객체를 생성하는 메소드로서.. 데이터베이스 파일 생성 또는 오픈이 성공했다면
            //해당 데이터베이스에 대한 참조객체를 반환받을 수 있다. (테이블 자체에 대한 직접적인 참조객체가 아님에 유의)
            //또한 이것은 읽기 및 쓰기가 다 가능한 객체를 반환받을 수 있다. getReadableDatabase()는 읽기전용

            //선생님께 물어보니, 이후에 바로 진행하는 INSERT 때문에 저 구문을 넣었다고 하셨다.
            //정말이다. 저 구문을 주석처리 했더니 테이블조차 생성이 되지 않는다.
            //onCreate()메소드에서 필요로 하는 SQLiteDatabase db 매개변수를 채워주기 위해 존재하는 것으로 보인다.
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
            //데이터베이스 테이블을 업그레이드 할 때 사용되는 메소드
            //어플을 삭제했다가 다시 설치시켰을 때에도 작동이 되나
        }
    }

}
