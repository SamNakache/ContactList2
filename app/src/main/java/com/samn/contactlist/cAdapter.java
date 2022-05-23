package com.samn.contactlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class cAdapter extends BaseAdapter {
    public static final int ONLY_FNAME = 0;
    public static final int ONLY_LNAME = 1;
    public static final int BOTH = 2;

    Context context;
    LayoutInflater inflater;
    Contact[] namesList;
    int filter;


    public cAdapter(Context applicationContext, List<Contact> contacts, int filter) {
        this.context = context;
        inflater = (LayoutInflater.from(applicationContext));
        namesList = new Contact[contacts.size()];
        contacts.toArray(namesList);
        this.filter = filter;

    }

    @Override
    public int getCount() {
        return namesList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_listview, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        String str = "";
        switch (filter){
            case ONLY_FNAME:
                str = namesList[i].get_firstname();
                break;
            case ONLY_LNAME:
                str = namesList[i].get_lastname();
                break;
            case BOTH:
                str = namesList[i].get_firstname()+" "+namesList[i].get_lastname();
        }
        names.setText(str);
        return view;
    }
}