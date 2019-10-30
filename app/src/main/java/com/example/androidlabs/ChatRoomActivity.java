package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidlabs.ProfileActivity;
import com.example.androidlabs.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.media.CamcorderProfile.get;


public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<MessageEntered> myEntryArrayList = new ArrayList<>();
    BaseAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // get the feilds from the screen
        Button sendButton = (Button) findViewById(R.id.sendButton);
        Button receiveButton = (Button) findViewById(R.id.receiveButton);
        EditText currentEntry = findViewById(R.id.textEntered);
        ListView myList = findViewById(R.id.theList);

        //get a database:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_ISSENT};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int isSentColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ISSENT);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        // go through all the results, return true if there is a next item.
        while (results.moveToNext()) {
            String message = results.getString(messageColumnIndex);
            Boolean isSentMessage;
            if (results.getInt(isSentColIndex) == 0) {
                isSentMessage = false;
            } else {
                isSentMessage = true;
            }
            long id = results.getLong(idColIndex);

            //add the new Message to the array list:
            myEntryArrayList.add(new MessageEntered(isSentMessage, message));
        }

        // create an adapter object and send it to the listView
        myList.setAdapter(myAdapter = new MyListAdapter());

        //Listen for the button being clicked
        sendButton.setOnClickListener(click -> {

            // add the content of the text entered to the array
            //call the array . add( new MessageEntered Object ( true, entry(from xml).getText.toString)
            //clear the field (entry(from xml) manually mad it " ")
            myEntryArrayList.add(new MessageEntered(true, currentEntry.getText().toString()));

            String message = currentEntry.getText().toString();
            int isSent = 1;

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
            newRowValues.put(MyDatabaseOpenHelper.COL_ISSENT, isSent);
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            currentEntry.setText("");
            myAdapter.notifyDataSetChanged();
        });

        receiveButton.setOnClickListener(v -> {
            myEntryArrayList.add(new MessageEntered(false, currentEntry.getText().toString()));

            String message = currentEntry.getText().toString();
            int isSent = 0;

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
            newRowValues.put(MyDatabaseOpenHelper.COL_ISSENT, isSent);
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            currentEntry.setText("");
            myAdapter.notifyDataSetChanged();
        });

        printCursor(results);

    }

    private class MyListAdapter extends BaseAdapter {
        // An inner class that only needs to be used here.

        @Override
        public int getCount() {
            return myEntryArrayList.size();
        }

        // This shows what is being shown
        @Override
        public MessageEntered getItem(int p) {
            return myEntryArrayList.get(p);
        }

        // This returns the database ID
        //Since we are not using a database it will just return itself.
        //example database return of 0 is 0, 1 is 1 etc.

        @Override
        public long getItemId(int p) {
            return p;
        }


        // This shows how it's being shown, text view, button etc.
        @Override
        public View getView(int p, View view, ViewGroup parent) {

            //

            LayoutInflater inflater = getLayoutInflater();
            //View thisRow = getLayoutInflater().inflate(R.layout.activity_list_view, null);
            myEntryArrayList.get(p).isSendButtonPressed();
            myEntryArrayList.get(p).messageEntered();


            if (myEntryArrayList.get(p).isSendButtonPressed()) {

                view = inflater.inflate(R.layout.activity_table_row_send, parent, false);
                TextView sndView = view.findViewById(R.id.sendField);
                sndView.setText(myEntryArrayList.get(p).messageEntered());
                return view;
                //TextView itemText = new TextView();
                //itemText.setText("Array at:" + getItem(p));
            } else {
                view = inflater.inflate(R.layout.activity_table_row_receive, parent, false);
                TextView rcdView = view.findViewById(R.id.receiveField);
                rcdView.setText(myEntryArrayList.get(p).messageEntered());
                return view;
            }
        }// End GetView
    }// End of BaseAdpatar

        private void printCursor(Cursor c) {

            Log.i("Version Number",  String.valueOf(MyDatabaseOpenHelper.VERSION_NUM));
            Log.i("Number of Coluns", String.valueOf(c.getColumnCount()));
            Log.i("Name of Colmns", Arrays.toString(c.getColumnNames()));
            Log.i("Number of Results", String.valueOf(c.getCount()));
            Log.i( "Results from cursor", DatabaseUtils.dumpCursorToString(c));

            //while (c.moveToNext()) {
             //   for (int count = 0; count <= c.getCount(); count++) {
              //      Log.i("Information Log",  c.getString(count));
              //  }

            //}

        }// End of printCursor

}// End of Class




