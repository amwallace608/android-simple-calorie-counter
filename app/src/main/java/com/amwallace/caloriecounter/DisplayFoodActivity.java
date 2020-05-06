package com.amwallace.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.amwallace.caloriecounter.Data.CustomListViewAdapter;
import com.amwallace.caloriecounter.Data.DatabaseHandler;
import com.amwallace.caloriecounter.Model.Food;
import com.amwallace.caloriecounter.Util.Utils;

import java.util.ArrayList;

public class DisplayFoodActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView listView;

    private Food myFood;
    private TextView totalFoods, totalCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food);

        listView = (ListView) findViewById(R.id.listView);
        totalCalories = (TextView) findViewById(R.id.totalCalsTxt);
        totalFoods = (TextView) findViewById(R.id.totalItemsTxt);

        refreshData();

    }

    private void refreshData() {
        dbFoods.clear();
        db = new DatabaseHandler(getApplicationContext());
        //get all foods from DB
        ArrayList<Food> foodsFromDB = db.getAllFoods();
        //get total cals and number of foods
        int calories = db.totalCalories();
        int foodCount = db.getCount();
        String formattedCals = Utils.formatNumber(calories);
        String formattedItems = Utils.formatNumber(foodCount);
        //set formatted values to TextViews
        totalFoods.setText("Foods: " + formattedItems);
        totalCalories.setText("Calories: " + formattedCals);
        //loop through foods in database
        for(int i = 0; i <foodsFromDB.size(); i++){
            //get current food data
            myFood = new Food(foodsFromDB.get(i).getName(),
                    foodsFromDB.get(i).getCalories(),
                    foodsFromDB.get(i).getRecordDate(),
                    foodsFromDB.get(i).getId());
            //add food to list
            dbFoods.add(myFood);
            Log.d("FOOD ID: ", String.valueOf(myFood.getId()));

        }
        db.close();

        //setup food adapter
        foodAdapter = new CustomListViewAdapter(DisplayFoodActivity.this,
                R.layout.list_item, dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();

    }
}
