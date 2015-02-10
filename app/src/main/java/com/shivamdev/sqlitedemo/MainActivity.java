package com.shivamdev.sqlitedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    SQLiteDatabase contactsDB = null;

    Button createDBButton, addContactButton, deleteContactButton, getContactsButton,
            deleteDBButton;


    EditText etName, etEmail, etContactsList, etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDBButton = (Button) findViewById(R.id.createDBButton);
        addContactButton = (Button) findViewById(R.id.addContactButton);
        deleteContactButton = (Button) findViewById(R.id.deleteContactButton);
        getContactsButton = (Button) findViewById(R.id.getContactsButton);
        deleteDBButton = (Button) findViewById(R.id.deleteDBButton);


        etName = (EditText) findViewById(R.id.nameEditText);
        etEmail = (EditText) findViewById(R.id.emailEditText);
        etContactsList = (EditText) findViewById(R.id.contactListEditText);
        etId = (EditText) findViewById(R.id.idEditText);
    }


    public void createDatabase(View view) {

        try {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption

            contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);

            // Execute an SQL statement that isn't select
            contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts " +
                    "(id integer primary key, name VARCHAR, email VARCHAR);");

            // The database on the file system
            File database = getApplicationContext().getDatabasePath("MyContacts.db");

            // Check if the database exists
            if (!database.exists()) {
                L.t(this, "Database Created!");
            } else {
                L.t(this, "Database Missing!");
            }
        } catch (Exception e) {
            L.l(e.getMessage());
        }
        addContactButton.setClickable(true);
        deleteContactButton.setClickable(true);
        getContactsButton.setClickable(true);
        deleteDBButton.setClickable(true);
    }

    public void addContact(View view) {

        String contactName = etName.getText().toString();
        String contactEmail = etEmail.getText().toString();

        contactsDB.execSQL("INSERT INTO contacts (name, email)" +
                " VALUES ('" + contactName + "', '" + contactEmail + "');");


    }

    public void getContacts(View view) {

        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);

        int idColumn = cursor.getColumnIndex("id");

        int nameColumn = cursor.getColumnIndex("name");

        int emailColumn = cursor.getColumnIndex("email");

        String contactsList = "";

        if (cursor != null && (cursor.getCount() > 0)) {
            do {
                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String email = cursor.getString(emailColumn);

                contactsList = contactsList + id + " : "
                        + name + " : " + email + "\n";

            } while (cursor.moveToNext());

            etContactsList.setText(contactsList);
        } else {
            L.t(this, "No Result to show.");
            etContactsList.setText("");
        }

    }

    public void deleteContact(View view) {

        String id = etId.getText().toString();

        contactsDB.execSQL("DELETE FROM contacts WHERE id = " +
                id + ";");

    }


    public void deleteDatabase(View view) {

        this.deleteDatabase("MyContacts");

    }

    @Override
    protected void onDestroy() {
        contactsDB.close();
        super.onDestroy();
    }
}
