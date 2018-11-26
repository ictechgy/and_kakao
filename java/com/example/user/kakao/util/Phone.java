package com.example.user.kakao.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class Phone {   //전화번호를 보여주거나 전화를 거는 버튼을 위한 클래스
    private Context ctx;            //뭘하든 위치값은 필수
    private AppCompatActivity activity;     //MemberDetail에 대한 정보 받아오기?
    private String phoneNum;

    public Phone(Context ctx, AppCompatActivity activity) {
        this.ctx = ctx;
        this.activity = activity;
        //그러면 activity를 받고 그 안에서 따로 ctx를 뽑을수는 없을까
    }

    public void dial(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNum));
        //객체에서 다른 객체로 데이터를 넘기는 것은 intent로만 가능
        //ctx를 쓰는 것은 내부 안에서의 객체끼리만 데이터를 주고받기 위해 쓰는거고, 전화는 내 폰을 벗어나서 밖으로 나가는 것이므로
        //국내가 아니라 해외를 가는 것임. 따라서 데이터가 외부로 나가야 하고, 그래서 위에 Intent.ACTION_CALL 을 썼다.
        //tel은 접두사(prefix)이다. 마치 xml에서 android:~~ 썼듯이. android 접두사는 화면을 위한 접두사.
        //맥락 Context 설정값 - Ok, Google. Open Youtube 에서 Open Youtube이 맥락값
        //기존 파일들에서 ctx를 썼던 것은 내부적으로 서버역할을 하라 했던것??
        //activity는 뭐지
        ctx.startActivity(intent);
    }

    public void directCall(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNum));
        if(ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            //이 기능을 ctx 이 안에서 해라. 저 파일로 가서 권한을 봐라.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},2);
            return;
        }
        ctx.startActivity(intent);
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
/*
    MemberDetail에서 Context ctx = MemberDetail.this; 를 했었고 해당 페이지에서 이 Phone 객체로
    ctx와 MemberDetail.this 두개를 넘겨준다.
    ctx에는 Context값만 들어있다.(MemberDetail을 모두 받은 것이 아니다.)
    이 값은 해당 페이지의 여러 값을 가지고 있을 것이므로 어디에서나 공유하는건 이해는 간다.
    그런데 activity는 잘 모르겠더라 이거다.
    Intent 는 Activity안에서만 살아있을 수 있으므로 activitiy까지 받아온 것임.

    자바도 여러가지가 두가지로 나뉜다.
    상수
    변수 - 지역변수 전역변수

    전역변수도 두가지로 나뉜다. 클래스변수, 인스턴스변수

    자바는 크게 프로젝트영역(Application 영역)
    Class 영역으로 나뉜다.
    클래스 바깥까지 돌아다닐 수 있는것 - static 변수
    클래스 안에서 도는 것 - instance 변수
    클래스 안의 area 안 - local, parameter 변수

    객체지향 언어에서 A객체 B객체가 기능과 속성을 공유하는 방법은 단 두가지이다.
    상속
    관계맺기 - 관계맺기도 두가지로 나뉜다. 연관관계, 의존관계

    A를 B의 안으로 통째로 넣어버리기 - 연관관계
    Area로 값을 직접 던져주는 것 - 의존관계

    Phone은 AppCompatActivity와 관련이 없다.
    Phone 안에 AppCompatActivity가 있으므로 연관관계이다.

    의존관계는 mbrList.setAdapter(new MemberAdapter()) 했던것과 같이 값을 전달받는 것을 의미한다.

    결국 Intent를 쓰기 위해서 Activity를 통째로 받아오는 것이다.
     */