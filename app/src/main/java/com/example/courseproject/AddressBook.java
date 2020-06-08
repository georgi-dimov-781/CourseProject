package com.example.courseproject;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AddressBook extends AppCompatActivity implements NewContact.OnCompleteListener, DeleteContact.OnCompleteListener {
    private AddressBookDatabase addressBookDatabase;
    private int userId;
    private static MenuItem deleteMenuOption;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        final Bundle bundleExtras = getIntent().getExtras();
        final String username = bundleExtras.getString("username");
        userId = bundleExtras.getInt("userId");
        addressBookDatabase = new AddressBookDatabase(this);

        Toolbar toolbarCustom = findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbarCustom);
        getSupportActionBar().setTitle(username + "'s Address Book");

        showContactOverview();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_loggedin_extras, menu);
        deleteMenuOption = menu.findItem(R.id.menuDelete);
        deleteMenuOption.setVisible(false);
        return true;
    }

    public static MenuItem getDeleteOption() {
        return deleteMenuOption;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuExit:
                Intent myIntent = new Intent(Intent.ACTION_MAIN);
                myIntent.addCategory(Intent.CATEGORY_HOME);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                System.exit(0);
                return true;
            case android.R.id.home:
                //android UI back button in the tab bar
                showContactOverview();
                return true;
            case R.id.menuDelete:
                Bundle args = new Bundle();
                args.putInt("contactId", NewContact.getContactId());
                DialogFragment deleteDialog = new DeleteContact();
                deleteDialog.setArguments(args);
                deleteDialog.show(getFragmentManager(), "Delete Dialog");
                return true;
            default:
                return false;
        }
    }

    public void onBackPressed() {
        //android back button has been pressed
        Fragment contactOverview = getFragmentManager().findFragmentByTag("Contact Overview");

        if(contactOverview == null) {
            //only load the overview contact fragment if its not showing
            showContactOverview();
        }
    }

    public void onComplete() {
        //once the newContact fragment closes, display the contact overview fragment
        showContactOverview();
    }

    public void showContactOverview() {
        //hide the tab bars back button, only show in newContact
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        if(deleteMenuOption != null) {
            deleteMenuOption.setVisible(false);
        }

        Bundle args = new Bundle();
        args.putInt("userId",userId);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment contactOverview = new ContactOverview();
        contactOverview.setArguments(args);

        fragmentTransaction.replace(R.id.linearLayoutRoot, contactOverview, "Contact Overview");
        fragmentTransaction.commit();
    }

    public void onCompleteDeleteDialog(Bundle callbackData) {
        switch (callbackData.getString("response")){
            case "Yes":
                if(addressBookDatabase.deleteContact(callbackData.getInt("contactId"))){
                    Toast.makeText(this, "Contact successfully deleted", Toast.LENGTH_SHORT).show();
                    showContactOverview();//refresh the list since a contact was deleted
                }
                else {
                    Toast.makeText(this, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }
}