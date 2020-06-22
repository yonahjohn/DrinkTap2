package com.example.drinktap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.drinktap.AddDrinkActivity.ADD_DRINK_REQUEST;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrinksLog log = new DrinksLog();


    // Classes required to construct a table.

    // Table
    private RecyclerView recyclerView;
    // Manager the data of the table
    private RecyclerView.Adapter mAdapter;
    // Type of layout of the table.
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The set layout of the activity as defined in the file activity_main.xml
        setContentView(R.layout.activity_main);

        // Setup the table
        recyclerView = findViewById(R.id.drinks_table);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Get the drinks saved from the file.
        ArrayList<Drink> list = readDataFromFile();
        // If there are no drinks save on the file.
        if(list.size() == 0){
            // Then get the list of drinks from the log (which is empty)
            list = log.getList();
        } else {
            // Otherwise set the log list to the list retrieved from the file.
            log.setList(list);
        }

        // Pass the data to the class the manages who the list is displayed on the table.
        mAdapter = new DrinkAdapter(list);
        // Set the data manager to the actual table.
        recyclerView.setAdapter(mAdapter);

        // Update the BAC shown in the home screen.
        updateBac();


        // Get the "ADD DRINK" button
        Button addDrinkButton = findViewById(R.id.button_add_drink);
        // Add a listener to that button which will allow to perform an action (onclick in this case).
        // "this" refers to the current class MainActivity which implements View.OnClickListener
        // When the "ADD DRINK" button is clicked the onClick() method will be called that is present
        // in this class.
        addDrinkButton.setOnClickListener(this);

        Button clearLogButton = findViewById(R.id.button_clear_log);
        clearLogButton.setOnClickListener(this);

        Button updateBacButton = findViewById(R.id.button_update_bac);
        updateBacButton.setOnClickListener(this);
    }

    private void saveDataToFile(){
        FileOutputStream fos;
        try {
            fos = openFileOutput("CalEvents", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(log.getList());
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Drink> readDataFromFile() {
        FileInputStream fis;
        ArrayList<Drink> list = new ArrayList<>();
        try {
            fis = openFileInput("CalEvents");
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<Drink>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void updateBac(){
        // Find the BAC label present in the home screen.
        TextView bacText = findViewById(R.id.bac);

        // Call the caculateBac() function to calculate the BAC.
        Double bac = caculateBac();
        // Set the text of the BAC label to the actual BAC.
        DecimalFormat format = new DecimalFormat("###.#####");
        //Sets the progress bar
        newProgress(bac);
        //Creates toasts for symptoms
        symptoms(bac);

        bacText.setText(format.format(bac));


    }

    private void symptoms(Double bac) {
        if( bac >= 0.001 && bac <= 0.029){
            Toast.makeText(this, "You may start to experience very subtle effects",
                    Toast.LENGTH_LONG).show();
        } else if(bac >= 0.03 && bac <= 0.059){
            Toast.makeText(this, "Watch out! You may be extra talkative!",
                    Toast.LENGTH_LONG).show();

        } else if(bac >= 0.06 && bac <= 0.099){
            Toast.makeText(this, "Your reflexes may be slowed, do NOT drive!",
                    Toast.LENGTH_LONG).show();

        } else if(bac >= 0.100 && bac <= 0.199){
            Toast.makeText(this, "You may feel nausea and have a slurred speech",
                    Toast.LENGTH_LONG).show();

        } else if(bac >= 0.200 && bac <= 0.299){
            Toast.makeText(this, "You may start to experience severe motor impairment and memory blackouts! Be careful!",
                    Toast.LENGTH_LONG).show();

        } else if(bac >= 0.300 && bac <= 0.399){
            Toast.makeText(this, "WARNING - your heart rate may get affected!",
                    Toast.LENGTH_LONG).show();

        } else if(bac >= 0.4){
            Toast.makeText(this, "WARNING - STOP DRINKING! PLEASE VISIT THE EMERGENCY ROOM",
                    Toast.LENGTH_LONG).show();

        }
    }

    private void newProgress(Double bac) {
        //Get the progress bar and set max values
        ProgressBar progressBar = findViewById(R.id.progressBar);
        int progress = (int) (bac*100);
        progressBar.setMax(25);
        progressBar.setProgress(progress);

    }

    private Double caculateBac(){
        // Get the default preferences manager.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Get the saved preferences of the weight and gender.
        String weightPref = preferences.getString("weight_preference", "");
        String genderPref = preferences.getString("gender_preference", "");

        // If the weight or the gender are not set, then show a toast and return the BAC as 0.0.
        if( weightPref == "" || genderPref == ""){
            Toast.makeText(this, "Fill all the details",
                    Toast.LENGTH_LONG).show();
            return 0.0;
        }

        // Set the distribution ratio depending on the gender.
        Double r = genderPref == "Male" ? 0.73 : 0.66;
        // Convert the weight retrieved as String to Double
        Double weight = Double.parseDouble(weightPref);

        // Get the list saved on the log, which will be the same as the one retrieved in onCreate method.
        ArrayList<Drink> list = log.getList();
        // Contains all the total amount of BAC calculated for each drink.
        Double bac = 0.0;

        // Formula % BAC = (A x 5.14 / W x r) – .015 x H
        // Ref: https://www.teamdui.com/bac-widmarks-formula/

        // Get the current date.
        Date date = new Date();

        // For each drink
        for (Drink drink: list) {
            // Gets the difference between the time the drink was drunk and the current time.
            long diff = date.getTime() - drink.getDate().getTime();

            Double A = drink.getVolume() * drink.getContent()/100;
            Double W = weight * 2.20462;
            // Then calculate the partial bac without considering the minutes passed.
            Double partial = (A * 5.14) / (W * r);
            // Calculate BAC considering the minutes passed.
            // 0.015 average hourly elimination rate.
            // 0.015/60 = 0.00025 average elimination rate in a minute.
            Double full = partial - 0.00025 * TimeUnit.MILLISECONDS.toMinutes(diff);

            // If the BAC is less then zero, it means that the drink's effects are gone.
            if (full > 0){
                // Otherwise, add the BAC to the bac variable.
                bac += full;
            }

        }

        return bac;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Then we return back from after adding a drink, this method is called.

        // Check that the result is from the AddDrinkActivity.
        if(requestCode == ADD_DRINK_REQUEST){
            // If the type of the result is RESULT_OK
            if(resultCode == Activity.RESULT_OK) {
                // We get the drink info from the result.
                String description = data.getStringExtra("description");
                String volume = data.getStringExtra("volume");
                String content = data.getStringExtra("content");

                // Add them to the log
                log.addDrink(description, Double.parseDouble(volume), Double.parseDouble(content));

                // Notify the table that the that is changed (the mAdapter has a reference to the data
                // which was set in onCreate method "mAdapter = new DrinkAdapter(list);" line)
                mAdapter.notifyDataSetChanged();

                // Update the BAC considering the new added drink.
                updateBac();

                // Save the log to the file.
                saveDataToFile();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_add_drink){
            // When the "ADD DRINK" button is start a new Activity AddDrinkActivity to add a drink.
            // The Activity is started with startActivityForResult() instead of startActivity()
            // because it will return a result (the result is the drink added.
            // We need to pass ADD_DRINK_REQUEST (which is defined in the AddDrinkActivity), so when the result
            // is returned and the method onActivityResult is called we can check the requestCode variable
            // of whether or not the result is return from a different activity or the AddDrinkActivity.
            startActivityForResult(new Intent(this, AddDrinkActivity.class), ADD_DRINK_REQUEST);
        }
        else if(v.getId() == R.id.button_clear_log){
            // Clear the log
            log.clearLog();
            // Notify to the table data manager that the data has been updated.
            mAdapter.notifyDataSetChanged();
            // Update the BAC.
            updateBac();
        }
        else if(v.getId() == R.id.button_update_bac){
            // Update the BAC.
            updateBac();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Set the layout of the Settings icon present in the top bar.
        MenuInflater inflater = getMenuInflater();
        // The layout of the settings is defined in the main_menu.xml file
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // If any menu option is selected, then check which option it was using the id for each
        // element of the menu.
        switch (item.getItemId()){
            // If it was the settings option
            case R.id.main_menu_settings:
                // Then start a new Activity SettingsActivity were we can set the weight and the gender.
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}



