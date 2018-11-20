package com.example.user.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
                    //Validation 유효성 체크.
                    // 만약 빈 값을 넣는다면 getText()했을 때 ""빈 값으로 들어온다. null값으로 들어오는게 아님에 유의
                    //따라서 toString()까지 해도 문제가 발생하지는 않는다.
                    //검증은 다양하게 할 수 있다. getText().equals("") 해도 되고 getText()=="" 도 되고
                    if(id_space.getText().length()!=0 && pw_space.getText().length()!=0){
                        String id = id_space.getText().toString();
                        String pw = pw_space.getText().toString();

                        final ItemExist ie = new ItemExist(ctx);
                        ie.setId(id);   //멤버가 private가 아니므로 그냥 ie.id = id 로 만들어도 된다.
                        ie.setPw(pw);

                        /*if(ie.execute()){
                            startActivity(new Intent(ctx, MemberList.class));
                        }else{
                            Toast.makeText(ctx, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ctx, Login.class));
                        }*/

                        new Main.ExecuteService() {
                            @Override
                            public void perform() {
                                if(ie.execute()){    //ie가 final 되어있어야 한다. 순수함수
                                    startActivity(new Intent(ctx, MemberList.class));
                                }else{
                                    startActivity(new Intent(ctx, Login.class));
                                }
                            }
                        }.perform();
                        //메소드를 오버라이딩하고 객체를 생성하자마자 메소드 작동시키기
                        //선생님은 이렇게 작성하셨다.

                        //굳이 이렇게 객체를 만들어서 따로 perform이 되도록 하는 이유는 뭘까?
                        //서비스를 구동시키는데 나중에 필요한 구조식임


                    }else{
                        Toast.makeText(ctx, "아이디 또는 비밀번호를 입력하십시오", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ctx, Login.class));  //잘못 입력한 경우 다시 이 페이지를 띄움(내용 삭제)
                    }
                }
            });

            findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0);
                }
            });

        } //onCreate End

        //이 안에서만 쓸 것이므로 private로 만듬
        private class LoginQuery extends Main.QueryFactory{
            SQLiteOpenHelper helper;
            public LoginQuery(Context ctx) {
                super(ctx);
                helper = new Main.SqliteHelper(ctx);  //기존에 있던 데이터베이스를 참고
            }

            @Override
            public SQLiteDatabase getDatabase() {
                return helper.getReadableDatabase();
            }
        }

        private class ItemExist extends LoginQuery{   //데이터베이스에서 값이 존재하는지 검증하기 위한 전용 클래스
            String id, pw;
            public ItemExist(Context ctx) {
                super(ctx);
            }
            public boolean execute(){
                return super
                        .getDatabase()
                        .rawQuery(String.format(
                                " SELECT * FROM %s "+
                                " WHERE %s LIKE '%s' AND %s LIKE '%s' ",
                                DBInfo.MBR_TABLE, DBInfo.MBR_SEQ, id,
                                DBInfo.MBR_PASS, pw
                        ),null)     //특정 기본값은 null로서 내가 쓰는 값만 쓰겠다고 표현
                .moveToNext();
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPw() {
                return pw;
            }

            public void setPw(String pw) {
                this.pw = pw;
            }
        }
        //기능별로 분리하기 위해 두 클래스로 나눈 것이다.


}
