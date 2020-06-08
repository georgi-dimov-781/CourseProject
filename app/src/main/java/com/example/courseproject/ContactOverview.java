package com.example.courseproject;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ContactOverview extends Fragment {
    public ContactOverview() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_overview, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        //add all of the contacts names to the listView
        final AddressBookDatabase addressBookDatabase = new AddressBookDatabase(getContext());

        final int userId = getArguments().getInt("userId");
        final Contact contactArray[] = addressBookDatabase.getUsersAddressBook(userId);
        final String contactInfoArray[] = new String[contactArray.length];

        for (int i = 0; i < contactArray.length; i++) {
            contactInfoArray[i] = contactArray[i].getName();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.layout_listview, contactInfoArray);
        final ListView listViewContacts = getView().findViewById(R.id.listView);
        listViewContacts.setAdapter(adapter);
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //will bring up the detailed info screen of the contact
                Bundle args = new Bundle();
                args.putString("contactType","View");
                args.putInt("userId",userId);
                args.putInt("contactId",contactArray[position].getContactId());
                args.putString("name", contactArray[position].getName());
                args.putString("address", contactArray[position].getAddress());
                args.putString("city", contactArray[position].getCity());
                args.putString("province", contactArray[position].getProvince());
                args.putString("postalCode", contactArray[position].getPostalCode());
                args.putString("phone1", contactArray[position].getPhone1());
                args.putString("phone2", contactArray[position].getPhone2());
                args.putString("phone3", contactArray[position].getPhone3());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment newContactFragment = new NewContact();
                newContactFragment.setArguments(args);

                fragmentTransaction.replace(R.id.linearLayoutRoot, newContactFragment);
                fragmentTransaction.commit();
            }
        });

        listViewContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putInt("contactId", contactArray[position].getContactId());
                DialogFragment deleteDialog = new DeleteContact();
                deleteDialog.setArguments(args);
                deleteDialog.show(getFragmentManager(), "Delete Dialog");

                return true;
            }
        });

        final FloatingActionButton floatingActionButton = getView().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("contactType","New");
                args.putInt("userId",userId);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment newContactFragment = new NewContact();
                newContactFragment.setArguments(args);

                fragmentTransaction.replace(R.id.linearLayoutRoot, newContactFragment);
                fragmentTransaction.commit();
            }
        });
    }
}
