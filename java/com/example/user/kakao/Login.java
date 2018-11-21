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
                    if(id_space.getText().length()!=0 && pw_space.getText().length()!=0){
                        String id = id_space.getText().toString();
                        String pw = pw_space.getText().toString();

                        final ItemExist ie = new ItemExist(ctx);
                        ie.setId(id);
                        ie.setPw(pw);


                        new Main.ExecuteService() {
                            @Override
                            public void perform() {
                                if(ie.execute()){
                                    startActivity(new Intent(ctx, MemberList.class));
                                }else{
                                    startActivity(new Intent(ctx, Login.class));
                                }
                            }
                        }.perform();



                    }else{
                        Toast.makeText(ctx, "아이디 또는 비밀번호를 입력하십시오", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ctx, Login.class));
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


        private class LoginQuery extends Main.QueryFactory{
            SQLiteOpenHelper helper;
            public LoginQuery(Context ctx) {
                super(ctx);
                helper = new Main.SqliteHelper(ctx);
            }

            @Override
            public SQLiteDatabase getDatabase() {
                return helper.getReadableDatabase();
            }
        }

        private class ItemExist extends LoginQuery{
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
                        ),null)
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



}
