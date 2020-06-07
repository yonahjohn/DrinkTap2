package com.example.drinktap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDrinkActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int ADD_DRINK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The set layout of the activity as defined in the file activity_add_drink.xml
        setContentView(R.layout.activity_add_drink);

        // The the title of the activity
        setTitle("Add Drink");

        // Get the button defined in the layout
        Button addDrinkButton = findViewById(R.id.add_drink_add_button);

        // Set the click event for the button
        addDrinkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // When the "ADD DRINK" button in clicked
        // Get all the fields that the user should have typed in the data.
        final EditText descriptionEdit = findViewById(R.id.add_drink_description);
        final EditText volumeEdit = findViewById(R.id.add_drink_volume);
        final EditText contentEdit = findViewById(R.id.add_drink_content);

        // If any of the fields are empty then show a toast without doing anything else.
        if(descriptionEdit.getText().toString().equals("") || volumeEdit.getText().toString().equals("") || contentEdit.getText().toString().equals("")){
            Toast.makeText(this, "Fill all the details",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // If all the fields are populated
        Intent returnIntent = new Intent();
        // Then save them to the intent which will be passed a result and received
        // by the onActivityResult method in the MainActivity
        returnIntent.putExtra("description", descriptionEdit.getText().toString());
        returnIntent.putExtra("volume", volumeEdit.getText().toString());
        returnIntent.putExtra("content", contentEdit.getText().toString());

        // Set the intent containing the data as result.
        setResult(Activity.RESULT_OK, returnIntent);
        // Close current activity and go back.
        finish();
    }
}