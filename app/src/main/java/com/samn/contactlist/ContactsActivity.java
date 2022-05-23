package com.samn.contactlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ContactsActivity extends AppCompatActivity {
    public static final int ONLY_FNAME = 0;
    public static final int ONLY_LNAME = 1;
    public static final int BOTH = 2;
    ListView simpleList;
    FloatingActionButton add;
    String currentUsername;
    ContactsDatabaseHandler db;
    List<Contact> contacts;
    cAdapter cAdapter;
    int checkedItem;
    int tcheckedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        db = new ContactsDatabaseHandler(this);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        currentUsername = user.getUsername();
        checkedItem = BOTH;

        contacts = db.getAllContacts(currentUsername);

        simpleList = (ListView)findViewById(R.id.simpleListView);
        cAdapter = new cAdapter(getApplicationContext(), sortContacts(contacts), checkedItem);
        simpleList.setAdapter(cAdapter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        add = (FloatingActionButton) findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, EditContactActivity.class);
                intent.putExtra("username", currentUsername);
                intent.putExtra("type", "add");
                startActivityForResult(intent, 1);

            }
        });

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent = new Intent(ContactsActivity.this, ShowActivity.class);
                intent.putExtra("contact", contacts.get(position));
                startActivityForResult(intent, 2);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Settings");
        menu.add("Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Settings"))
            openSettings();
        else if (item.getTitle().equals("Logout"))
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        final CharSequence[] items = {"Only firstname", "Only lastname", "Both"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
        builder.setTitle("How do you want your contacts to be shown ?");
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                tcheckedItem = item;
            }
        });

        builder.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkedItem = tcheckedItem;
                        Toast.makeText(ContactsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        refreshActivity();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public List<Contact> sortContacts(List<Contact> contacts){
        if (checkedItem == BOTH){
            Collections.sort(contacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact p1, Contact p2) {
                    int sizeCmp = p1._lastname.compareToIgnoreCase(p2._lastname);
                    if (sizeCmp != 0) {
                        return sizeCmp;
                    }
                    return p1._firstname.compareToIgnoreCase(p2._firstname);
                }
            });
        }
        else if (checkedItem == ONLY_FNAME){
            Collections.sort(contacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact p1, Contact p2) {
                    return p1._firstname.compareToIgnoreCase(p2._firstname);
                }
            });
        }

        else{
            Collections.sort(contacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact p1, Contact p2) {
                    return p1._lastname.compareToIgnoreCase(p2._lastname);
                }
            });
        }
        return contacts;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Contact newContact = (Contact)data.getExtras().getSerializable("newContact");
                db.addContact(newContact);
                refreshActivity();
            }

            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }

        if (requestCode == 2) {
            if(resultCode == RESULT_OK){
                String type = data.getStringExtra("type");
                if (type.equals("update")){
                    Contact prevContact = (Contact)data.getExtras().getSerializable("initContact");
                    Contact newContact = (Contact)data.getExtras().getSerializable("newContact");
                    db.updateContact(prevContact, newContact);
                }
                else{
                    Contact prevContact = (Contact)data.getExtras().getSerializable("initContact");
                    db.deleteContact(prevContact);
                }
                refreshActivity();
            }

            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }

    }

    public void refreshActivity(){
        contacts.clear();
        contacts = db.getAllContacts(currentUsername);
        cAdapter = new cAdapter(getApplicationContext(), sortContacts(contacts), checkedItem);
        simpleList.setAdapter(cAdapter);
    }
}
