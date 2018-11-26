package com.example.user.kakao.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.user.kakao.R;


public class Album extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        final Context ctx = Album.this;
        GridView myAlbum = findViewById(R.id.myAlbum);
        String[] arr = {"photo_1","photo_2", "photo_3", "photo_4", "photo_5"};  //DB에서 들고왔다고 가정
        myAlbum.setAdapter(new Picture(ctx,  arr));
    }

    public class Picture extends BaseAdapter{
        private Context ctx;
        private String[] pictures;  //일단 폰의 사진 읽게는 불가능하므로 외부사진 보여주기

        public Picture(Context ctx, String[] pictures) {
            this.ctx = ctx;
            this.pictures = pictures;
        }

        @Override
        public int getCount() {
            return pictures.length;
        }

        @Override
        public Object getItem(int i) {
            return pictures[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
            View gridView;
            if(v==null) {
                gridView = new GridView(ctx);
                gridView = inflater.inflate(R.layout.picture, null);
                ImageView iv = gridView.findViewById(R.id.photo);
                String ph = pictures[i];
                switch (ph) {
                    case "photo_1":
                        iv.setImageResource(R.drawable.photo_1);
                        break;
                    case "photo_2":
                        iv.setImageResource(R.drawable.photo_2);
                        break;
                    case "photo_3":
                        iv.setImageResource(R.drawable.photo_3);
                        break;
                    case "photo_4":
                        iv.setImageResource(R.drawable.photo_4);
                        break;
                    case "photo_5":
                        iv.setImageResource(R.drawable.photo_5);
                        break;
                    default:
                        iv.setImageResource(R.drawable.blank);
                        break;
                }
            }else{
                gridView = v;
            }
            /*
            내가 작성해본 getView()메소드. MemberList.java에서 작성했던 것과 유사하게 작성하였다.
            선생님이 작성하신게 작동이 안되고 내가 작성한건 되는데?
            LayoutInflater inflater = LayoutInflater.from(ctx);
            ImageView iv;
            if(v==null) {
                v = inflater.inflate(R.layout.picture, null);
                iv = v.findViewById(R.id.photo);
                String ph = pictures[i];
                switch (ph) {
                    case "photo_1":
                        iv.setImageResource(R.drawable.photo_1);
                        break;
                    case "photo_2":
                        iv.setImageResource(R.drawable.photo_2);
                        break;
                    case "photo_3":
                        iv.setImageResource(R.drawable.photo_3);
                        break;
                    case "photo_4":
                        iv.setImageResource(R.drawable.photo_4);
                        break;
                    case "photo_5":
                        iv.setImageResource(R.drawable.photo_5);
                        break;
                    default:
                        iv.setImageResource(R.drawable.blank);
                        break;
                }
                v.setTag(iv);
            }else{
                iv = (ImageView)v.getTag();
            }
*/


            return v;
        }
    }
}
