package com.nhom31.tbdd.bt3_nhom31.BT4;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.nhom31.tbdd.bt3_nhom31.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivityBT4 extends AppCompatActivity {
    Button btnImport, btnExport;

    SharedPreferences sharedPreferences;
    ArrayList<Contact> listSame, listDiff;

    private String TAG = "BT3_4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bt4);
        getSupportActionBar().hide();

        // sharedPreferences
        sharedPreferences = getSharedPreferences("FileExport", MODE_PRIVATE);
        listSame = new ArrayList<Contact>();
        listDiff = new ArrayList<Contact>();

        btnImport = (Button) findViewById(R.id.btnImport);
        btnExport = (Button) findViewById(R.id.btnExport);

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImport();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberFileExport = sharedPreferences.getInt("NumberFileExport", -1);
                String nameFile = createNameFileExport((numberFileExport == -1) ? 1 : (numberFileExport + 1));
                handleExport(nameFile);
            }
        });
    }

    private void handleImport(){
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        FilePickerDialog dialog = new FilePickerDialog(MainActivityBT4.this,properties);
        dialog.setTitle("Select a File");

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                String pathSelected = files[0];
                ArrayList<Contact> list = readFile(pathSelected);
                ArrayList<Contact> listAndroidContact = getAndroidContact();
                for(Contact c : list){
                    if(listAndroidContact.contains(c)){
                        listSame.add(c);
                    }else{
                        if(!checkSame(c, listAndroidContact)){
                            listDiff.add(c);
                        }

                    }
                }

                // import into android contact
                for(Contact c : listDiff) {
                    importContacts(c);
                }

                Toast.makeText(MainActivityBT4.this, listDiff.size() + "/" + list.size() + " imported", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private boolean checkSame(Contact contact, ArrayList<Contact> listAndroidContact) {
        for(Contact tmp : listAndroidContact) {
            if(contact.equals(tmp)){
                return true;
            }
        }
        return false;
    }

    /**
     * Read data in file import
     * @param pathSelected
     * @return
     */
    private ArrayList<Contact> readFile(String pathSelected) {
        File fileSelected = new File(pathSelected);

        ArrayList<Contact> listContact = new ArrayList<Contact>();

        if(fileSelected.exists()){
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(fileSelected));
                String line;
                while ((line = br.readLine()) != null) {
                    Log.d(TAG, line);
                    Contact contact = new Contact();

                    // name
                    String[] infoName = line.split(":");
                    if ((infoName[0]).equals("Name")) {
                        contact.setDisplayName(infoName[1]);
                    }

                    // phone
                    String[] infoPhone = br.readLine().split(":");
                    if((infoPhone[0]).equals("Phone")) {
                        int counterPhone = Integer.parseInt(infoPhone[1]);
                        Phone[] listPhone = new Phone[counterPhone];
                        for(int j = 0; j < counterPhone; j++){
                            Phone phone = new Phone();
                            line = br.readLine();
                            String[] phoneArr = line.split(":");

                            //assign info detail phone
                            phone.setTypePhone(phoneArr[0]);
                            if(phone.getTypePhone().equals("0")){
                                phone.setNameLabel(phoneArr[1]);
                                phone.setPhoneNumber(phoneArr[2]);
                            }else{
                                phone.setPhoneNumber(phoneArr[1]);
                            }
                            listPhone[j] = phone;
                        }
                        contact.setListPhone(listPhone);
                    }
                    listContact.add(contact);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listContact;
    }

    public void importContacts(Contact contact) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getDisplayName())
                .build());
        for(Phone phone : contact.getListPhone()) {
            ops.add(assignPhoneNumber(phone, rawContactInsertIndex));
        }

        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // error
        } catch (OperationApplicationException e) {
            // error
        }
    }

    private void handleExport(final String nameFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Export contacts?");
        builder.setMessage("Your contact list will be exported to file: /storage/emulated/0/" + nameFile);
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Contact> list = getAndroidContact();
                // write file export
                writerFile(createContentOutput(list), nameFile);
            }
        });

        builder.create().show();
    }

    /**
     * Get all contacts in android contact
     * @return
     */
    public ArrayList<Contact> getAndroidContact() {
        ArrayList<Contact> list = new ArrayList<Contact>();
        ContentResolver contentResolver = getContentResolver();

        // get all contacts
        // ContactsContract.Contacts.CONTENT_URI: content://com.android.contacts/contacts
        Cursor cursorContacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        // check has contact
        if(cursorContacts.getCount() > 0) {
            while(cursorContacts.moveToNext()){
                String contact_id = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // get phone number
                int hasPhoneNumber = Integer.parseInt(cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                Contact contact = new Contact();
                if(hasPhoneNumber > 0){
                    contact.setDisplayName(contact_display_name);
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);
                    Phone[] listPhone = new Phone[phoneCursor.getCount()];
                    int tmp = 0;
                    while(phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int type = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        Phone phone = new Phone();
                        phone.setTypePhone(type + "");
                        if(phone.getTypePhone().equals("0")){
                            phone.setNameLabel(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)));
                        }
                        phone.setPhoneNumber(phoneNumber);
                        listPhone[tmp] = phone;
                        tmp++;
                    }
                    contact.setListPhone(listPhone);
                    phoneCursor.close();
                }
                list.add(contact);
            }
        }
        return list;
    }

    private StringBuffer createContentOutput(ArrayList<Contact> list) {
        StringBuffer output = new StringBuffer();
        for(Contact contact : list){
            output.append("Name:" + contact.getDisplayName());
            Phone[] listPhones = contact.getListPhone();
            output.append("\nPhone:" + listPhones.length);
            for(Phone phone : listPhones){
                output.append("\n" + convertTypeToString(phone));
            }
            output.append("\n");
        }
        Log.d(TAG, output.toString());
        return output;
    }

    /**
     * Handle write file
     * @param content
     * @param nameFile
     */
    private void writerFile(StringBuffer content, String nameFile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int numberFileExport = sharedPreferences.getInt("NumberFileExport", -1);
        FileWriter fw = null;
        try {
            File txtFile = new File(Environment.getExternalStorageDirectory(), nameFile);

            fw = new FileWriter(txtFile);
            fw.append(content);
            fw.flush();
            fw.close();
            Toast.makeText(this, "File " + nameFile + " saved", Toast.LENGTH_SHORT).show();
            editor.putInt("NumberFileExport", (numberFileExport == -1) ? 1 : (numberFileExport + 1));
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Convert Phone.TYPE_... to string
     * @param phone
     * @return
     */
    private String convertTypeToString(Phone phone){
        String result = "";
        String type = phone.getTypePhone();
        switch (type){
            case "0":
                result = type + ":" + phone.getNameLabel() + ":" + phone.getPhoneNumber();
                break;
            default:
                result = type + ":" + phone.getPhoneNumber();
                break;
        };
        return result;
    }

    private ContentProviderOperation assignPhoneNumber(Phone phone, int rawContactInsertIndex){
        ContentProviderOperation result = null;
        switch (phone.getTypePhone()){
            case "0":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM)
                        .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, phone.getNameLabel())
                        .build();
                break;
            case "1":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        .build();
                break;
            case "2":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build();
                break;
            case "3":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .build();
                break;
            case "4":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK)
                        .build();
                break;
            case "5":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME)
                        .build();
                break;
            case "6":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_PAGER)
                        .build();
                break;
            case "7":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
                        .build();
                break;
            case "12":
                result = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MAIN)
                        .build();
                break;
        };
        return result;
    }

    /**
     * Create name file export
     * @param number
     * @return NameFileExport
     */
    private String createNameFileExport(int number) {
        if(number == -1) {
            return "Contacts_0001.txt";
        }
        return "Contacts_" + ((number < 10)? ("000" + number) : ((number < 100)? ("00" + number) : ((number < 1000) ? ("0" + number): number))) + ".txt";
    }
}

