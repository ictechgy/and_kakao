package com.example.user.kakao;

public class Member {   //
    int seq;
    String name, pass, email, phone, addr, photo;
    //private로 만들지 않았는데 그 이유는 이미 인증이 끝난 이후의 상태에서 데이터 값들을 불러올 것이기 때문이다.

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    @Override
    public String toString() {
        return getName()+getAddr()+getEmail()+getPass()+getPhone()+getPhoto()+getSeq();
    }
    //member객체에 대해서 toString()했을 경우를 위한 오버라이딩
}
