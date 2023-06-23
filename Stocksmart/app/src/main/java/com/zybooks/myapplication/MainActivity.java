package com.zybooks.myapplication;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// MainActivity extends the AppCompatActivity class which displays the UI and processes user input
public class MainActivity extends AppCompatActivity {
    private static final String PREF_KEY_IS_LOGGED_IN = "isLoggedInPref";
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(PREF_KEY_IS_LOGGED_IN, false);
    };

    // onCreate() is called when MainActivity first starts, and setContentView() sets MainActivity's
    // content to the layout in activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initially setting the view to the home page
        setContentView(R.layout.activity_main);

        fetchItemsAndUpdateUI();

        // Defining some button variables
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button addNewEntryButton = findViewById(R.id.addNewEntryButton);
        Button textNotificationButton = findViewById(R.id.textNotificationButton);

        // Check if the user is logged in
        if (isLoggedIn()) {
            // User is logged in, show "Logout" button only
            loginButton.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            // User is not logged in, show "Login" and "Register" buttons
            loginButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        };

        // Making different buttons take the user to different screens and start different activities
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            };
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            };
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_KEY_IS_LOGGED_IN, false);
                editor.apply();

                // Restart the MainActivity to reflect the changes
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        addNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoggedIn()) {
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to use this feature.", Toast.LENGTH_SHORT).show();
                };
            };
        });

        textNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificationPrompt();
            };
        });
    };

    public void showNotificationPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Enable Text Notifications");
        builder.setMessage("Do you want to receive text notifications?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Disable text notifications here
            };
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Enable text notifications here
            };
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.LOGIN_SUCCESSFUL) {
            if (resultCode == RESULT_OK) {
                boolean loginResult = data.getBooleanExtra("login_result", false);
                if (loginResult) {
                    // Handle successful login
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    // Perform any other actions for a successful login
                };
            };
        };
    };

    private void fetchItemsAndUpdateUI() {
        InventoryDatabase dbHelper = new InventoryDatabase(this);
        // Using getAllItem() here
        ArrayList<Item> items = dbHelper.getAllItems();

        Log.d(TAG, "fetchItemsAndUpdateUI: ");

        ListView listView = findViewById(R.id.listView);
        ItemsList adapter = new ItemsList(this, items, dbHelper, isLoggedIn());
        listView.setAdapter(adapter);
    };
};
