package com.example.user.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kakao.util.Album;
import com.example.user.kakao.util.Email;
import com.example.user.kakao.util.Movie;
import com.example.user.kakao.util.Phone;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context ctx = MemberDetail.this;

        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        final ItemDetail query = new ItemDetail(ctx);
        query.seq = seq;

        final Member m = (Member)new Main.ObjectService(){
            @Override
            public Object perform() {
                return query.execute();
            }
        }.perform();

        Log.d("선택된 사용자 ",m.toString());


        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView addr = findViewById(R.id.addr);
        ImageView profile = findViewById(R.id.profile);

        name.setText(m.getName());
        email.setText(m.getEmail());
        phone.setText(m.getPhone());
        addr.setText(m.getAddr());

        final ImageDetail im = new ImageDetail(ctx);
        im.seq = m.getSeq()+"";
        String image_name = ((String)new Main.ObjectService(){
            @Override
            public Object perform() {
                return im.execute();
            }
        }.perform()).toLowerCase();

        profile.setImageDrawable(getResources().getDrawable(
                getResources().getIdentifier(
                        ctx.getPackageName()+":drawable/"+image_name,null,null
                ), ctx.getTheme()
        ));

        final String spec = m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.phone+"/"+m.photo;
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MemberUpdate.class);
                intent.putExtra("spec",spec);
                startActivity(intent);
            }
        });

        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberList.class));
            }
        });



        //나머지 버튼들 구현
        findViewById(R.id.callBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone phone = new Phone(ctx, MemberDetail.this);
                phone.setPhoneNum(m.getPhone());
                phone.directCall();
            }
        });

        findViewById(R.id.dialBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone phone = new Phone(ctx, MemberDetail.this);
                phone.setPhoneNum(m.getPhone());
                phone.dial();
            }
        });

        findViewById(R.id.smsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.emailBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email email = new Email(ctx, MemberDetail.this);
                email.sendEmail("wlsghd1216@naver.com");
            }
        });

        findViewById(R.id.albumBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, Album.class));
            }
        });

        findViewById(R.id.movieBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, Movie.class));
            }
        });

        findViewById(R.id.mapBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.musicBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }   //onCreate 끝


    //멤버 한명정보 가져오기
    private class DetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;   //Main.SQLiteHelper 로 만들어도 됨
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
        String seq;
        public ItemDetail(Context ctx) {
            super(ctx);
        }
        public Member execute(){
            Cursor c = getDatabase().rawQuery(String.format(
                    " SELECT * FROM %s WHERE %s LIKE '%s' ",
                    DBInfo.MBR_TABLE,DBInfo.MBR_SEQ, seq
            ),null);

            Member m =null;
            if(c!=null){
                if(c.moveToNext()){
                    m = new Member();
                    m.setSeq(Integer.parseInt(seq));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));

                    Log.d("검색된 회원", m.toString());
                }
            }else{
                Log.d("검색 실패","해당 seq에 일치하는 회원이 없습니다.");
            }

            return m;
        }
    }

    //멤버 한명에 대한 사진이름 가져오기
    private class ImageDetail extends DetailQuery{
        String seq;
        public ImageDetail(Context ctx) {
            super(ctx);
        }

        public String execute(){
            Cursor c = getDatabase().rawQuery(
                    String.format(" SELECT %s FROM %s WHERE %s LIKE %s",DBInfo.MBR_PHOTO, DBInfo.MBR_TABLE,
                    DBInfo.MBR_SEQ, seq
            ),null);

            String image_name = "";
            if(c!=null){
                if(c.moveToNext()){
                    image_name = c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO));
                }
            }
            return image_name;
        }
    }




}
