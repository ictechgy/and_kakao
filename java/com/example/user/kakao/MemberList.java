package com.example.user.kakao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context ctx = MemberList.this;

        final ListView mbrList = findViewById(R.id.mbrList);

        final ItemList query = new ItemList(ctx);


        mbrList.setAdapter(new MemberAdapter(ctx, (ArrayList<Member>)new Main.ListService(){
            @Override
            public List<?> perform() {
                return query.execute();
            }
        }.perform()));


        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberAdd.class));
            }
        });


        mbrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int i, long l) {
                Member m = (Member)mbrList.getItemAtPosition(i);
                Log.d("선택한 id값", m.getSeq()+"");
                Intent intent = new Intent(ctx, MemberDetail.class);
                intent.putExtra("seq",m.seq+"");
                startActivity(intent);
            }
        });

        //삭제처리
        mbrList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> p, View v, int i, long l) {
                final Member m = (Member)mbrList.getItemAtPosition(i);

                new AlertDialog.Builder(ctx)
                        .setTitle("삭 제")
                        .setMessage("정말 삭제?")
                        .setPositiveButton(
                                android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //삭제 쿼리 실행
                                        final ItemDelete query = new ItemDelete(ctx);
                                        query.seq = m.seq+"";
                                        new Main.ExecuteService(){
                                            @Override
                                            public void perform() {
                                                query.execute();
                                            }
                                        }.perform();

                                        Toast.makeText(ctx, "삭제 완료", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ctx, MemberList.class));
                                    }
                                }
                        ).setNegativeButton(
                        android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ctx, "삭제 취소", Toast.LENGTH_LONG).show();
                            }
                        }
                ).show();  //alert도 show()주자.

                return true;  //true로 해야 반영이 된다.
            }
        });

    } //onCreateEnd

    //모든 멤버정보 가져오기
    private class ListQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public ListQuery(Context ctx){
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }



    private class ItemList extends ListQuery{
        public ItemList(Context ctx){
            super(ctx);
        }
        public ArrayList<Member> execute(){
            ArrayList<Member> ls = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(
                " SELECT * FROM MEMBER ", null
            );

            Member m = null;
            if(c!=null){
                while(c.moveToNext()){
                    m = new Member();

                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));

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
    //모든 멤버정보 가져오기 끝


    //mbr_item.xml 아이템 관련 파트
    private class MemberAdapter extends BaseAdapter{
        Context ctx;
        ArrayList<Member> ls;
        LayoutInflater inflater;

        public MemberAdapter(Context ctx, ArrayList<Member> ls) {
            this.ctx = ctx;
            this.ls = ls;
            this.inflater = LayoutInflater.from(ctx);
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


        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v = inflater.inflate(R.layout.mbr_item, null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            holder.name.setText(ls.get(i).getName());
            holder.phone.setText(ls.get(i).getPhone());

            //포토불러오는 코드
            final ItemPhoto query = new ItemPhoto(ctx);
            query.seq = ls.get(i).getSeq()+"";
            String s = ((String)new Main.ObjectService() {
                @Override
                public Object perform() {
                    return query.execute();
                }
            }.perform()).toLowerCase();   //ArrayList<Member> ls 에 이미 포토이름값이 있는데 또 가져올 이유가 있을까
            //String ImageName = ls.get(i).getPhoto().toLowerCase();  그냥 이렇게 가져와도 될텐데.
            Log.d("파일명",s);

            try {
                holder.photo.setImageDrawable(getResources().getDrawable(
                        getResources().getIdentifier(ctx.getPackageName() + ":drawable/" + s, null, null), ctx.getTheme()
                ));   //res에 있는 drawable에 있는 파일로 세팅하기
            }catch(Exception e){
                e.printStackTrace();
                holder.photo.setImageDrawable(getResources().getDrawable(
                        getResources().getIdentifier(ctx.getPackageName()+":drawable/blank", null, null), ctx.getTheme()
                ));   //사진이름을 못가져오는 경우 빈 화면 띄우기
            }

            return v;
        }

    }
    static class ViewHolder{
        ImageView photo;
        TextView name, phone;
    }
    //아이템 생성관련 끝


    //사진을 불러오기 위한 클래스 시작
    private class PhotoQuery extends Main.QueryFactory{
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

    private class ItemPhoto extends PhotoQuery{
        String seq;
        public ItemPhoto(Context ctx) {
            super(ctx);
        }
        public String execute(){
            Cursor c = getDatabase().rawQuery(
                    String.format(
                            " SELECT %s FROM %s WHERE %s LIKE '%s' ",  //따옴표에 주의하도록 한다.
                            DBInfo.MBR_PHOTO,DBInfo.MBR_TABLE,DBInfo.MBR_SEQ, seq),null
            );
            String result = "";
            if(c!=null){
                if(c.moveToNext()){
                    result = c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO));
                }
            }
            return result;
        }
    }
    //실제로는 이미지를 URL로 가져온다? - 서버에 있는 것을 가져온다는 건가
    //사진 불러오기부분 끝


    private class DeleteQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public DeleteQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private class ItemDelete extends DeleteQuery{
        String seq;
        public ItemDelete(Context ctx) {
            super(ctx);
        }

        public void execute(){
            getDatabase().execSQL(
                    String.format(
                            " DELETE FROM %s WHERE %s LIKE '%s' ", DBInfo.MBR_TABLE, DBInfo.MBR_SEQ, seq
                    )   //null값은 rawQuery에만 추가적으로 넣어주는거구나
            );
        }


    }





}
