package com.example.user.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context ctx = MemberList.this;

        ListView mbrList = findViewById(R.id.mbrList);

        //Log.d("친구목록 ") - 이름들만 나오도록 작성
        final ItemList query = new ItemList(ctx);

        /*ArrayList<Member> s = (ArrayList<Member>)new Main.ListService(){
            @Override
            public List<?> perform() {
                return query.execute();
            }
        }.perform();*/

        //Log.d("친구목록",s.toString());
        //List에 대해 toString 하면 각각의 요소객체에 대해 toString()한 것과 동일한 듯


        //mbrList.setAdapter(new MemberAdapter(ctx, s));  //mbrList에다가 어댑터를 먼저 붙인 다음에 이 어댑터이 ls 리스트를 줄 것



        mbrList.setAdapter(new MemberAdapter(ctx, (ArrayList<Member>)new Main.ListService(){
            @Override
            public List<?> perform() {
                return query.execute();
            }
        }.perform()));




        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberDetail.class));
            }
        });
    } //onCreateEnd


    private class ListQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public ListQuery(Context ctx){
            super(ctx);
            helper = new Main.SqliteHelper(ctx);  //데이터베이스 접근 문지기
            //ctx를 던지는 이유는, 해당 데이터베이스 문지기에 대해 여기로 오라고 하려고 하기 위함
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    //지금 보면 각 java파일마다 객체생성하면서 데이터베이스에 접근을 매번 하는데,
    //데이터베이스에 접근하는 것도 원래는 싱글톤패턴이라고 해서 객체 하나만 가지고 작동하도록 한다. - getInstance 등

    private class ItemList extends ListQuery{
        public ItemList(Context ctx){
            super(ctx);
        }
        public ArrayList<Member> execute(){
            ArrayList<Member> ls = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(    //Cursor 는 ResultSet처럼 생각해도 된다.
                " SELECT * FROM MEMBER ", null   //null 은 내가 만든 쿼리를 실행하겠다.
            );

            Member m = null;
            if(c!=null){   //데이터가 아무것도 존재하지 않을수도 있으므로 검증하기
                while(c.moveToNext()){
                    m = new Member();
                    //m을 밖에서 null로 둔 이유. 데이터가 하나도 없는 경우 m도 null로 두도록 만들기 위해서이다.

                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    //getColumnIndex는 칼럼명을 가지고 해당 칼럼의 숫자값을 가져온다. 그 값을 가지고 getString을 해서 seq값을 가져온다.
                    //DB는 전부 String으로 되어있다. String으로 통신함

                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));

                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));

                    ls.add(m);
                    Log.d("등록된 회원수는", ls.size()+"");
                }
            }else{
                Log.d("등록된 회원은 ", "없음");
            }

            return ls;
        }
    }
    //두 클래스를 합쳐도 되기는 하는데 클래스 하나당 메소드 하나씩만 배치해주자. -> 람다식용?


    //mbr_item.xml 아이템 관련 파트
    private class MemberAdapter extends BaseAdapter{  //android studio의 클래스 상속
        //에릭 감마가 디자인패턴을 만들었다. 이 중 어댑터 패턴을 쓸 것이다.
        //출력할 ListView 사이에 어댑터를 하나 두고, 이 어댑터에 데이터를 넘겨주면 어댑터가 데이터를 써다준다.
        Context ctx;
        ArrayList<Member> ls;
        LayoutInflater inflater;  //자동차 공기압축기. 글자새긴 풍선을 리스트에 붙인다고 생각하자. 그 다음에 풍선 부풀리기?
        //데이터 이동할 때 용량 줄이기 위해서?
        public MemberAdapter(Context ctx, ArrayList<Member> ls) {
            this.ctx = ctx;   //어댑터 또한 이곳으로 끌고오기
            this.ls = ls;
            this.inflater = LayoutInflater.from(ctx);  //이 자리에 와서 바람을 넣기
        }

        @Override
        public int getCount() {   //리스트가 몇개의 아이템을 가지고 있는지
            return ls.size();
        }

        @Override
        public Object getItem(int i) {
            return ls.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        //i는 인덱스를 의미

        @Override
        public View getView(int i, View v, ViewGroup g) {
            //v를 풍선처럼 부풀린다음에 데이터를 붙여서 반환시키기
            ViewHolder holder;
            if(v==null){
                v = inflater.inflate(R.layout.mbr_item, null);  //해당 xml을 가져와서 v에 담기
                holder = new ViewHolder();
                //holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);   //위젯의 id를 위젯객체와 연결
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            /*setTag는 바코드를 다는 것이라고 보면 된다. 태그달기
            setTag는 인자로서 Object형을 받으며 getTag는 Object형을 반환한다.
            원하는 view에 태그를 달아서
            */
            holder.name.setText(ls.get(i).getName());
            holder.phone.setText(ls.get(i).getPhone());
            //포토 불러오는 코드


            return v;
        }
        //getView부분이 반복되면서 하나하나의 Item을 만들어준다고 생각하면 된다.
    }
    static class ViewHolder{
        ImageView photo;
        TextView name, phone;
    }  //값 부착할 클래스

    private class PhotoQuery extends Main.QueryFactory{   //사진을 불러오기 위한 클래스
        SQLiteOpenHelper helper;
        public PhotoQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class PhotoItem extends PhotoQuery{
        int seq;
        public PhotoItem(Context ctx) {
            super(ctx);
        }
        public String execute(){
            super.getDatabase().rawQuery(
                    String.format(
                            "SELECT %s WHERE %s LIKE %s",
                            DBInfo.MBR_PHOTO, DBInfo.MBR_SEQ, seq
                    ),null
            );

            return null;
        }
    }




}
