package com.example.prapti.birthdays;

public class Birthday_Model {
    String id;
    String name;
    String birthday;
    String title;
    String message;
    String phone;
    String alarm_id;

    public Birthday_Model(String id, String name, String birthday, String title, String message, String phone, String alarm_id) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.title = title;
        this.message = message;
        this.phone = phone;
        this.alarm_id = alarm_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }
}
