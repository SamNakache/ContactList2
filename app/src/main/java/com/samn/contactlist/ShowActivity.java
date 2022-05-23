package com.samn.contactlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Arrays;

public class ShowActivity extends AppCompatActivity {

    TextView tv_firstName, tv_lastname, tv_number, gender_icon;
    Contact contact, initContact;
    Button edit, delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        tv_firstName = (TextView) findViewById(R.id.tv_firstName);
        tv_lastname = (TextView) findViewById(R.id.tv_lastname);
        tv_number = (TextView) findViewById(R.id.tv_number);
        gender_icon = (TextView) findViewById(R.id.gender_icon);

        Intent intent = getIntent();
        contact = (Contact)intent.getSerializableExtra("contact");
        setContact(contact);
        initContact = contact;

        edit = (Button) findViewById(R.id.edit_button);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, EditContactActivity.class);
                intent.putExtra("contact", contact);
                intent.putExtra("type", "update");
                startActivityForResult(intent, 1);

            }
        });

        delete = (Button) findViewById(R.id.delete_button);
        delete.setBackgroundColor(Color.RED);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ShowActivity.this);
                builder1.setMessage("Are you sure you want to delete this contact ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete();

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

    }

    public void delete(){
        int resultCode = RESULT_OK;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("type", "delete");
        resultIntent.putExtra("initContact", (Serializable) initContact);
        setResult(resultCode, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                contact = (Contact)data.getExtras().getSerializable("newContact");
                setContact(contact);
            }

            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    public void setContact(Contact contact){
        tv_firstName.setText(contact.get_firstname());
        tv_lastname.setText(contact.get_lastname());
        tv_number.setText(fixNumber(contact.getPhoneNumber()));
        if (contact.get_gender().equals("female"))
            gender_icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.female, 0);

    }

    @Override
    public void onBackPressed() {
        int resultCode = RESULT_OK;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("type", "update");
        resultIntent.putExtra("newContact", (Serializable) contact);
        resultIntent.putExtra("initContact", (Serializable) initContact);
        setResult(resultCode, resultIntent);
        finish();
    }

    public String fixNumber(String number){
        String result = "";
        String[] splitted = number.split("");
        splitted = Arrays.copyOfRange(splitted, 1, splitted.length);
        if (splitted.length == 10){
            for (int i = 0; i < splitted.length; i++){
                if (i == 3 || i == 6)
                    result += "-";
                result += splitted[i];
            }
        }
        else
            result = number;
        return result;
    }


}
