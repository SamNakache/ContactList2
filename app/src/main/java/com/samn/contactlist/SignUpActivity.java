package com.samn.contactlist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText et_username, et_password, et_cpassword;
    TextView err;
    String username, password, cpassword;
    Button button;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_cpassword = (EditText) findViewById(R.id.et_cpassword);
        err = (TextView) findViewById(R.id.err);

        Intent intent = getIntent();
        users = (List<User>) intent.getSerializableExtra("users");

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                cpassword = et_cpassword.getText().toString();
                err.setVisibility(View.GONE);

                if (!checkFields())
                    err.setVisibility(View.VISIBLE);
                else{
                    User newUser = new User(username,password);
                    int resultCode = RESULT_OK;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newUser", (Serializable) newUser);
                    setResult(resultCode, resultIntent);
                    finish();
                }

            }
        });
    }

    public boolean userExist(String username) {
        for (User element : users) {
            if (element.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public boolean checkFields() {
        if (username.equals("") || password.equals("") || cpassword.equals("")){
            String errorMessage = "You need to fill all of the fields.";
            err.setText(errorMessage);
            return false;
        }


        if (userExist(username)) {
            String errorMessage = "Username already exist.";
            err.setText(errorMessage);
            return false;
        }

        if (!password.equals(cpassword)) {
            String errorMessage = "Password and Confirm Password are different.";
            err.setText(errorMessage);
            return false;
        }

        return true;
    }
}
