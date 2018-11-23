package com.example.user.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context ctx = MemberUpdate.this;

        //Intent intent = this.getIntent();
        //String spec = intent.getExtras().getString("spec");
        /*String spec = getIntent().getStringExtra("spec");
        Log.d("spec값", spec);
        String[] arr = spec.split("/");*/

        final String[] arr = getIntent().getStringExtra("spec").split("/");

        //넘어오는 값 순서 - m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.phone+"/"+m.photo;

        //수정 전, 이 페이지가 로드됐을 때 값을 띄워놓기
        final EditText name = findViewById(R.id.name);
        final EditText email = findViewById(R.id.email);
        final EditText phone = findViewById(R.id.phone);
        final EditText addr = findViewById(R.id.addr);
        final EditText pass = findViewById(R.id.pass);
        addr.setHint(arr[1]);
        email.setHint(arr[2]);
        name.setHint(arr[3]);
        pass.setHint(arr[4]);
        phone.setHint(arr[5]);


        ImageView photo = findViewById(R.id.photo);
        photo.setImageDrawable(getResources().getDrawable(
                getResources().getIdentifier(
                        ctx.getPackageName()+":drawable/"+arr[6].toLowerCase(),null,null
                ),ctx.getTheme()
        ));


        //수정 이후
        //만약 사용자가 값을 변경하지 않고 일부분만 수정했다면?
        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = new Member();  //쿼리 실행시 넘겨줄 데이터를 가지고 있을 놈
                final ItemUpdate query = new ItemUpdate(ctx);

                //member에 값을 넣을건데 만약 EditText가 NULL값이라면 밖에 있는 배열 arr의 값을 가져오도록 하자.
                member.seq = Integer.parseInt(arr[0]);
                member.name = (name.getText().length()>0)? name.getText().toString():arr[3];
                member.email = (email.getText().length()>0)? email.getText().toString() : arr[2];
                member.addr = (addr.getText().length()>0)? addr.getText().toString() : arr[1];
                member.pass = (pass.getText().length()>0)? pass.getText().toString() : arr[4];
                member.phone = (phone.getText().length()>0)? phone.getText().toString() : arr[5];
                member.photo = arr[6];

                //null은 주소값이 없는것을 체크하기 위함이다. 따라서 name.getText()==null 으로 검증하지는 말자.
                //주소값검증은 getText().toString=="" 또는 getText().equals("") 로 해도 된다.

                //member.setName((name.getText().length()>0)? name.getText().toString():arr[3]);  선생님이 하신방식

                query.member = member;
                new Main.ExecuteService(){
                    @Override
                    public void perform() {
                        query.execute();
                    }
                }.perform();

                Intent intent = new Intent(ctx, MemberDetail.class);
                intent.putExtra("seq", member.seq+"");  //MemberDetail 페이지는 seq값을 필요로 한다.
                startActivity(intent);

            }
        });

        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private class UpdateQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public UpdateQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private class ItemUpdate extends UpdateQuery{
        Member member;  //메모리에 저장되는 값
        //이곳은 메모리공간이므로 기본적으로 연산자가 존재해서는 안된다.
        //연산자 넣으면 메모리에 CPU기능을 하라고 시키게 되는 것이다.

        public ItemUpdate(Context ctx) {
            super(ctx);
            member = new Member();
            //인스턴스 멤버변수는 반드시 생성자 내부에서 초기화한다.
            //로직은 반드시 에어리어(메소드) 내부에서 이루어져야 한다.
            //에어리어 내부는 CPU를 뜻하며 필드는 RAM 영역을 의미한다.
        }

        public void execute(){
            getDatabase().execSQL(
                    String.format(" UPDATE %s SET " +   //데이터베이스명
                            " %s = '%s', " +     //왼쪽은 칼럼명, 오른쪽은 바꿀 값
                            " %s = '%s', " +
                            " %s = '%s', " +
                            " %s = '%s', " +
                            " %s = '%s', " +
                            " %s = '%s' " +
                            " WHERE %s LIKE '%s' ",
                            DBInfo.MBR_TABLE, DBInfo.MBR_NAME, member.name,
                            DBInfo.MBR_ADDR, member.addr, DBInfo.MBR_PHONE, member.phone,
                            DBInfo.MBR_EMAIL, member.email, DBInfo.MBR_PHOTO, member.photo,
                            DBInfo.MBR_PASS, member.pass, DBInfo.MBR_SEQ, member.seq)
            );

        }
    }

}
