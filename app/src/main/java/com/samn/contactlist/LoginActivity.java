package com.samn.contactlist;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    EditText et_username, et_password;
    String username, password;
    Button btn_login, btn_signup;
    TextView err;
    UsersDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login();
    }

    void Login(){
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        err = (TextView) findViewById(R.id.err);

        db = new UsersDatabaseHandler(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();

                if (!checkFields())
                    err.setVisibility(View.VISIBLE);
                else{
                    err.setVisibility(View.GONE);
                    et_password.setText("");
                    Intent intent = new Intent(LoginActivity.this,ContactsActivity.class);
                    User user = new User(username, password);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                List<User> users = db.getAllUsers();
                intent.putExtra("users", (Serializable) users);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                User newUser = (User) data.getExtras().getSerializable("newUser");
                db.addUser(newUser);
                Intent intent = new Intent(LoginActivity.this,ContactsActivity.class);
                intent.putExtra("user", (Serializable) newUser);
                startActivity(intent);

            }

            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }

    }

    public boolean checkFields() {
        if (username.equals("") || password.equals("")){
            String errorMessage = "You need to fill all of the fields.";
            err.setText(errorMessage);
            return false;
        }


        if(!db.confirmed(username, password)) {
            String errorMessage = "Username or password is wrong.";
            err.setText(errorMessage);
            return false;
        }

        return true;
    }
}