package com.example.chickens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.R.layout;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {
    Button all_contacts_btn, selected_contacts_btn;
    ListView lv_contactsList;
    ArrayAdapter contactsArrayAdapter;

    Pattern pattern = Pattern.compile("050", Pattern.CASE_INSENSITIVE);
    Matcher matcher;


    //Matcher matcher = pattern.matcher("Visit W3Schools!");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        all_contacts_btn = findViewById(R.id.all_contacts_btn);
        selected_contacts_btn = findViewById(R.id.selected_contacts_btn);
        lv_contactsList = findViewById(R.id.lv_contactsList);

        all_contacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contactsArrayAdapter = new ArrayAdapter<ContactModel>(MainActivity2.this, layout.simple_list_item_1 , getPhoneContactsList());
                lv_contactsList.setAdapter(contactsArrayAdapter);
            }
        });

        selected_contacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contactsArrayAdapter = new ArrayAdapter<ContactModel>(MainActivity2.this, layout.simple_list_item_1 , getSortedContactsList());
                lv_contactsList.setAdapter(contactsArrayAdapter);

            }
        });

        /*map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);

            }
        });*/

        lv_contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactModel contactModel = (ContactModel) adapterView.getItemAtPosition(i);
                String address = contactModel.getAddress();
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                Log.i("CONTACT_ADDRESS", address);
                intent.putExtra("CONTACT_ADDRESS", address);
                startActivity(intent);
            }
        });

    }

    private List<ContactModel> getPhoneContactsList(){

        List<ContactModel> returnList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity2.this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri uri_p = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        Cursor cursor_p = contentResolver.query(uri_p, null, null, null, null);

        //Log.i( "CONTACT_PROVIDER", "Number fo contacts: " + Integer.toString(cursor.getCount()));
        String contactName = null;
        String contactNumber = null;
        String contactAddress = null;


        while (cursor.moveToNext()&&cursor_p.moveToNext()){
            contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactAddress = cursor_p.getString(cursor_p.getColumnIndexOrThrow(String.valueOf(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
            String contactMail = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS));
            ContactModel contact = new ContactModel(contactName, contactNumber, contactAddress);
            returnList.add(contact);

            Log.i("CONTACT_PROVIDER", "Contact Name: " + contactName + " ContactPhone: " + contactNumber + " ContactAddress: " + contactAddress + " ContactMail: " + contactMail);
        }

        cursor.close();
        cursor_p.close();
        return returnList;
    }

    private List<ContactModel> getSortedContactsList(){

        List<ContactModel> returnList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity2.this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri uri_p = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        Cursor cursor_p = contentResolver.query(uri_p, null, null, null, null);

        Log.i( "CONTACT_PROVIDER", "Number fo contacts: " + Integer.toString(cursor.getCount()));
        String contactName = null;
        String contactNumber = null;
        String contactAddress = null;


        while (cursor.moveToNext() && cursor_p.moveToNext()){
            contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactAddress = cursor_p.getString(cursor_p.getColumnIndexOrThrow(String.valueOf(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
            String contactMail = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS));

            matcher = pattern.matcher(contactNumber);

            if(matcher.find()) {
                ContactModel contact = new ContactModel(contactName, contactNumber, contactAddress);
                returnList.add(contact);
            }

            Log.i("CONTACT_PROVIDER", "Contact Name: " + contactName + " ContactPhone: " + contactNumber + " ContactAddress: " + contactAddress + " ContactMail: " + contactMail);
        }

        cursor.close();
        cursor_p.close();
        return returnList;
    }
}