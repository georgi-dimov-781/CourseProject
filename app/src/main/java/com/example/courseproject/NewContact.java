package com.example.courseproject;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class NewContact extends Fragment {
    private NewContact.OnCompleteListener onCompleteListener;
    private String contactType;//to allow access in anon classes
    private static int contactId;

    private TextView textViewName;
    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextProvince;
    private EditText editTextPostalCode;
    private EditText editTextPhone1;
    private EditText editTextPhone2;
    private EditText editTextPhone3;
    private FloatingActionButton floatingActionButton;

    public interface OnCompleteListener {
        void onComplete();
    }

    public NewContact() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_contact, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        //display the back button in the tab bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        onCompleteListener = (NewContact.OnCompleteListener) getActivity();

        textViewName = getView().findViewById(R.id.textViewName);
        editTextName = getView().findViewById(R.id.editTextName);
        editTextAddress = getView().findViewById(R.id.editTextAddress);
        editTextCity = getView().findViewById(R.id.editTextCity);
        editTextProvince = getView().findViewById(R.id.editTextProvince);
        editTextPostalCode = getView().findViewById(R.id.editTextPostalCode);
        editTextPhone1 = getView().findViewById(R.id.editTextPhone1);
        editTextPhone2 = getView().findViewById(R.id.editTextPhone2);
        editTextPhone3 = getView().findViewById(R.id.editTextPhone3);
        floatingActionButton = getView().findViewById(R.id.floatingActionButton);

        editTextName.setTextColor(Color.WHITE);
        editTextAddress.setTextColor(Color.WHITE);
        editTextCity.setTextColor(Color.WHITE);
        editTextProvince.setTextColor(Color.WHITE);
        editTextPostalCode.setTextColor(Color.WHITE);
        editTextPhone1.setTextColor(Color.WHITE);
        editTextPhone2.setTextColor(Color.WHITE);
        editTextPhone3.setTextColor(Color.WHITE);

        //fill in the contact data if the user is editing an existing contact
        contactType = getArguments().getString("contactType");
        final int userId = getArguments().getInt("userId");

        switch (contactType) {
            case "New":
                contactId = -1;//no database entry yet, so no contactId

                toggleViewSettings(true, "New", "Name (Required)", "New Contact");
            break;
            case "View":
                contactId = getArguments().getInt("contactId");

                //set existing contact arguments
                editTextName.setText(getArguments().getString("name"));
                editTextAddress.setText(getArguments().getString("address"));
                editTextCity.setText(getArguments().getString("city"));
                editTextProvince.setText(getArguments().getString("province"));
                editTextPostalCode.setText(getArguments().getString("postalCode"));
                editTextPhone1.setText(getArguments().getString("phone1"));
                editTextPhone2.setText(getArguments().getString("phone2"));
                editTextPhone3.setText(getArguments().getString("phone3"));

                toggleViewSettings(false, "View", "Name", "");
            break;
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editTextName.getText().toString().isEmpty()) {
                    //invalid contact form submission
                    Toast.makeText(getContext(), "Contact name is a required field", Toast.LENGTH_SHORT).show();
                }
                else {
                    //valid contact form submission
                    final AddressBookDatabase addressBookDatabase = new AddressBookDatabase(getContext());

                    String name = editTextName.getText().toString();
                    String address = editTextAddress.getText().toString();
                    String city = editTextCity.getText().toString();
                    String province = editTextProvince.getText().toString();
                    String postalCode = editTextPostalCode.getText().toString();
                    String phoneOne = editTextPhone1.getText().toString();
                    String phoneTwo = editTextPhone2.getText().toString();
                    String phoneThree = editTextPhone3.getText().toString();

                    Contact contact = new Contact(contactId, name, address, city, province, postalCode, phoneOne, phoneTwo, phoneThree);

                    switch (contactType) {
                        case "New":
                            //add the new contact to the database
                            contactId = addressBookDatabase.addNewContact(userId, contact);

                            if(contactId != -1){
                                Toast.makeText(getContext(), "Contact created successfully", Toast.LENGTH_SHORT).show();

                                //New -> View
                                contactType = "View";
                                toggleViewSettings(false, "View", "Name", "");
                            }
                            else {
                                Toast.makeText(getContext(), "Failed to create contact", Toast.LENGTH_SHORT).show();
                            }
                       break;
                        case "Edit":
                            //update the already existing contact in the database
                            if(addressBookDatabase.updateExistingContact(contact)){
                                Toast.makeText(getContext(), "Contact updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Failed to update contact", Toast.LENGTH_SHORT).show();
                            }

                            //Edit -> View
                            contactType = "View";
                            toggleViewSettings(false, "View", "Name", "");
                        break;
                        case "View":
                            //View -> Edit
                            contactType = "Edit";
                            toggleViewSettings(true, "Edit", "Name (Required)", "Edit Contact");
                        break;
                    }
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            //android UI back button in the tab bar
            onCompleteListener.onComplete();
        }

        return true;
    }

    public void toggleViewSettings(boolean enabledFlag, String fabIcon, String nameString, String tabBarTitle) {
        MenuItem menuDelete = AddressBook.getDeleteOption();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(tabBarTitle);

        switch(fabIcon) {
            case "New":
                menuDelete.setVisible(false);
               floatingActionButton.setImageResource(R.drawable.ic_save_black_24dp);
            break;
            case "Edit":
                menuDelete.setVisible(true);
                floatingActionButton.setImageResource(R.drawable.ic_save_black_24dp);
            break;
            case "View":
                menuDelete.setVisible(true);
                floatingActionButton.setImageResource(R.drawable.ic_edit_black_24dp);
            break;
            default:
        }

        textViewName.setText(nameString);

        editTextName.setEnabled(enabledFlag);
        editTextAddress.setEnabled(enabledFlag);
        editTextCity.setEnabled(enabledFlag);
        editTextProvince.setEnabled(enabledFlag);
        editTextPostalCode.setEnabled(enabledFlag);
        editTextPhone1.setEnabled(enabledFlag);
        editTextPhone2.setEnabled(enabledFlag);
        editTextPhone3.setEnabled(enabledFlag);
    }

    public static int getContactId() {
        return contactId;
    }
}
