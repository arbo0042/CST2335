package com.example.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;




public class TestToolBar extends AppCompatActivity {

Toolbar tBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tool_bar);

        tBar = (Toolbar)findViewById(R.id.test_toolbar);
        //setSupportActionBar(tBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //if (getSupportActionBar() != null){
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:

            case R.id.item1:
                toaster();
                break;

            case R.id.item2:
                alertExample();
                break;

            case R.id.item3:
                Snackbar sb = Snackbar.make(tBar, "Lab #7", Snackbar.LENGTH_LONG)
                        .setAction("Would you like to Go Back?", e -> finish());
                sb.show();
                break;

            case R.id.item4:
               Toast overflowToast = Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_SHORT);
               overflowToast.show();
               break;


        }
        return true;
    }

    String overflowToast ="This is the initial message";

    public void alertExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.activity_pop_up, null);
        EditText et = (EditText)middle.findViewById(R.id.view_edit_text);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The Message")
                .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overflowToast = et.getText().toString();
                        toaster();

                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
    public void toaster() {
        Toast toast = Toast.makeText(getApplicationContext(),overflowToast,Toast.LENGTH_LONG);
        toast.show();
    }
}
