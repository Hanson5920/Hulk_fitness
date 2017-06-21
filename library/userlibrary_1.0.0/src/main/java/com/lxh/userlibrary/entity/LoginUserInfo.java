package com.lxh.userlibrary.entity;


//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wbb on 2016/10/24.
 */

//@Entity
public class LoginUserInfo {

//    @Id
    private String uid;
    private String mail;
    private String birthday;
    private String logo;
    private String sex;
    private String desc;
    private String login_key;
    private String name;
    private long id;

//    @Generated(hash = 1399162429)
    public LoginUserInfo(String uid, String mail, String birthday, String logo,
                         String sex, String desc, String login_key, String name, long id) {
        this.uid = uid;
        this.mail = mail;
        this.birthday = birthday;
        this.logo = logo;
        this.sex = sex;
        this.desc = desc;
        this.login_key = login_key;
        this.name = name;
        this.id = id;
    }

//    @Generated(hash = 436417725)
    public LoginUserInfo() {
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogin_key() {
        return login_key;
    }

    public void setLogin_key(String login_key) {
        this.login_key = login_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
