package com.samn.contactlist;


import java.io.Serializable;

public class Contact implements Serializable {
    int _id;
    String _firstname;
    String _lastname;
    String _phone_number;
    String _username;
    String _gender;
    public Contact(){}

    public Contact(String _firstname, String _lastname, String _phone_number, String _username, String _gender){
        this._firstname = _firstname;
        this._lastname = _lastname;
        this._phone_number = _phone_number;
        this._username = _username;
        this._gender = _gender;
    }

    public String get_firstname() {
        return _firstname;
    }

    public void set_firstname(String _firstname) {
        this._firstname = _firstname;
    }

    public String get_lastname() {
        return _lastname;
    }

    public void set_lastname(String _lastname) {
        this._lastname = _lastname;
    }
    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getPhoneNumber(){
        return this._phone_number;
    }

    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_gender() {
        return _gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }
}