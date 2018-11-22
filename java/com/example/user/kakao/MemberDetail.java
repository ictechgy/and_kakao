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

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context ctx = MemberDetail.this;

        Intent intent = this.getIntent(); //이전페이지에서 넘어온 Intent를 받음. this 생략가능
        String seq = intent.getExtras().getString("seq");
        final ItemDetail query = new ItemDetail(ctx);
        query.seq = seq;

        Member m = (Member)new Main.ObjectService(){
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
        //findViewById() 에다가 바로 setText()이런게 안되는구나. 속성값으로서 반드시 변수에 받아둔 후 변경이 가능하다.
        //이 말인 즉슨, getView에서도 전개한 뷰v에 값을 넣을 때 클래스객체를 통해 한게 이해가 된다.
        //클래스객체의 각각의 멤버에 값을 담고 그 멤버에다가 setText()같은걸 붙였었다.
        //그리고 이미 view를 만들었었던 상태였었다면 전개를 하지 않아도 되고 이전에 설정했던 태그값을 가져옴으로서
        //바로 설정을 할 수 있도록 Tag메소드들도 사용했음
        name.setText(m.getName());
        email.setText(m.getEmail());
        phone.setText(m.getPhone());
        addr.setText(m.getAddr());

        // String photo_name = m.getPhoto().toLowerCase();  이걸로 해도 되지만 추가적으로 데이터 가져오도록 만들자.
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
                //이번에는 해당 사람의 모든 정보를 같이 싣자. Member객체를 통째로 보내는건 안됨
                startActivity(intent);
            }
        });

        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberList.class));
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
