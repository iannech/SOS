package com.example.sos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sos.data.DbSOS;
import com.example.sos.data.bean.Contact;
import com.example.sos.data.table.TbContact;
import com.example.sos.layout.ContactItem;
import com.example.sos.layout.ContactItemAdapter;
import com.example.sos.layout.Selectable;

import java.util.ArrayList;

public class FragContacts extends Fragment implements Selectable{


    //Views
    private RecyclerView recyclerView;
    private ArrayList<ContactItem> contactItems;
    private ContactItemAdapter contactItemAdapter;
    private TextView tvMessage;

    private Button btnAddContact;

    private final static int REQ_PICK_CONTACT = 1;
    private final static int MAX_CONTACTS_COUNT = 5;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_contacts, container,
                false);
        bindViews(rootView);
        initRecyclerView();
        setListener();
        return rootView;
    }

    private void bindViews(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        btnAddContact = (Button) rootView.findViewById(R.id.btnAddContact);
        tvMessage = (TextView) rootView.findViewById(R.id.tvMessage);
    }

    /**
     * initilise the recycler view and set its properties
     */
    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        contactItems = new ArrayList<>();
        loadContactsFromDb();
        contactItemAdapter = new ContactItemAdapter(contactItems, this);
        recyclerView.setAdapter(contactItemAdapter);
        updateVisibility();
    }

    /**
     * Loads contacts item from
     */
    private void loadContactsFromDb() {
        DbSOS db = new DbSOS(getActivity());
        ArrayList<Contact> contacts = db.contact.getAll();
        for (Contact contact : contacts)
            contactItems.add(new ContactItem(contact.getId(), contact.getFullName(), contact.getPhone()));
        db.close();
    }

    private void setListener() {
        btnAddContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });
    }

    /**
     * Update the visibility of the views depending on the number of views
     */
    private void updateVisibility() {
        if (contactItems.size() > 0) {
            tvMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    /**
     * Confirm remove contact
     */
    private void confirmRemoveContact(final int index) {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage("Are sure you want to remove \"" + contactItems.get(index).getTitle() + "\" from list?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeContact(index);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    /**
     * Removes selected contact
     * @param index
     */
    private void removeContact(int index){
        DbSOS db = new DbSOS(getActivity());
        db.contact.delete(contactItems.get(index).getId());
        db.close();
        contactItems.remove(index);
        contactItemAdapter.notifyDataSetChanged();
        updateVisibility();
    }

    /**
     * Open up a contact picker
     */
    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, REQ_PICK_CONTACT);
    }

    /**
     * After the contact has been picked
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_CONTACT && resultCode == getActivity().RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String phoneNo = cursor.getString(phoneIndex);
            String name = cursor.getString(nameIndex);
            addContact(name, phoneNo);
        }
    }

    /**
     * Adds selected contact to db
     *
     * @param contactName   name of contact
     * @param contactNumber number of contact
     */
    private void addContact(String contactName, String contactNumber) {
        if (contactNumberExists(contactNumber)) {
            showMessage("Contact Number already exists");
        } else {
            DbSOS db = new DbSOS(getActivity());
            int contactsCount = db.contact.getCount();
            if (contactsCount >= MAX_CONTACTS_COUNT) {
                showMessage("Contact List is full");
            } else {
                db.contact.add(new Contact(String.valueOf(contactsCount + 1), contactName, contactNumber));
                contactItems.add(new ContactItem(contactItems.size() + "", contactName, contactNumber));
                contactItemAdapter.notifyDataSetChanged();
                updateVisibility();
            }
            db.close();
        }
    }

    /**
     * Checks if a certain phone number already exists
     *
     * @param contactNumber the contact number to be checked
     * @return return true if contact number already exists
     */
    private boolean contactNumberExists(String contactNumber) {
        DbSOS db = new DbSOS(getActivity());
        boolean numberExists = db.contact.exists(contactNumber, TbContact.COLUMN_NAME_PHONE);
        db.close();
        return numberExists;
    }


    /**
     * Helper method to show message using Toast
     *
     * @param message the string message to be shown
     */
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void selected(int index) {
        confirmRemoveContact(index);
    }
}
