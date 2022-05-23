package com.samn.contactlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class EditContactActivity extends AppCompatActivity {
    String username, type;
    Button btnSave;
    EditText et_firstname, et_lastname, et_number;
    String firstname, lastname, number, gender;
    Contact contact;
    TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcontact);

        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_number = (EditText) findViewById(R.id.et_number);
        err = (TextView) findViewById(R.id.err);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("add"))
            username = intent.getStringExtra("username");
        else {
            contact = (Contact)intent.getSerializableExtra("contact");
            username = contact.get_username();
            et_firstname.setText(contact.get_firstname());
            et_lastname.setText(contact.get_lastname());
            et_number.setText(contact.getPhoneNumber());
        }


        btnSave = (Button) findViewById(R.id.button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = et_firstname.getText().toString();
                lastname = et_lastname.getText().toString();
                number = et_number.getText().toString();
                try {
                    gender = getGender(firstname);
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }

                err.setVisibility(View.GONE);

                if (!checkFields())
                    err.setVisibility(View.VISIBLE);
                else{
                    Intent resultIntent = new Intent();
                    Contact newContact = new Contact(firstname, lastname, number, username, gender);
                    resultIntent.putExtra("newContact", (Serializable) newContact);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }


            }
        });


    }

    private String getGender(String firstname) throws ExecutionException, InterruptedException, JSONException {
        String gender = "";
        String myUrl = "https://api.genderize.io/?name="+firstname;
        String result;
        HttpGetRequest getRequest = new HttpGetRequest();
        result = getRequest.execute(myUrl).get();
        JSONObject jObject = new JSONObject(result);
        gender = jObject.getString("gender");

        return gender;
    }

    public boolean checkFields() {
        if (username.equals("") || lastname.equals("")){
            String errorMessage = "You need to fill Firstname and Lastname";
            err.setText(errorMessage);
            return false;
        }
        return true;
    }
}
