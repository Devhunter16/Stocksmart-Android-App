package com.zybooks.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class AddItemActivity extends AppCompatActivity {

    private EditText itemDescription;
    private EditText itemQuantity;
    private EditText itemUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        // Finding all of our components and assigning them to variables
        Button submitNewItemButton = findViewById(R.id.submitNewItemButton);
        Button goBackFromAddItemButton = findViewById(R.id.goBackFromAddItemButton);
        itemDescription = findViewById(R.id.description);
        itemQuantity = findViewById(R.id.qty);
        itemUnit = findViewById(R.id.unit);

        // Logic for submitting the form, saving the info in the database, and returning to home
        submitNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
                Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Making the back button return the user to the home screen
        goBackFromAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(intent);
            };
        });
    };

    private void addItem() {
        // Retrieving input
        String description = itemDescription.getText().toString();
        String quantityText = itemQuantity.getText().toString();
        String unit = itemUnit.getText().toString();

        // Some validation
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(quantityText) || TextUtils.isEmpty(unit)) {
            Toast.makeText(this, "No item entry submitted. Please enter text into all fields to add item.", Toast.LENGTH_SHORT).show();
            return;
        };

        // Trying to turn quantity from a string into an integer
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
            return;
        };

        InventoryDatabase dbHelper = new InventoryDatabase(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryDatabase.InventoryTable.COL_DESCRIPTION, description);
        values.put(InventoryDatabase.InventoryTable.COL_QUANTITY, quantity);
        values.put(InventoryDatabase.InventoryTable.COL_UNIT, unit);
        long newRowId = db.insert(InventoryDatabase.InventoryTable.TABLE, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Inventory item(s) created successfully", Toast.LENGTH_SHORT).show();

            // Start the MainActivity
            Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish the RegisterActivity to prevent going back to it with the back button
        } else {
            Toast.makeText(this, "Failed to create item(s)", Toast.LENGTH_SHORT).show();
        };

        db.close();
    };
};

