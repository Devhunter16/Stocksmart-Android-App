package com.zybooks.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final int LOGIN_SUCCESSFUL = 1;
    private EditText username;
    private EditText password;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize the SharedPreferences object
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button loginButton = findViewById(R.id.loginButton);
        Button goBackFromLoginButton = findViewById(R.id.goBackFromLoginButton);
        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting username and password from appropriate fields and turning them into strings
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();
                findUser(enteredUsername, enteredPassword);
            }
        });

        // Making the back button return the user to the home screen
        goBackFromLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    };

    private void findUser(String username, String password) {
        UserDatabase dbHelper = new UserDatabase(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                UserDatabase.UserTable.COL_USERNAME,
                UserDatabase.UserTable.COL_PASSWORD
        };

        String selection = UserDatabase.UserTable.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                UserDatabase.UserTable.TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String foundUsername = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabase.UserTable.COL_USERNAME));
            String foundPassword = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabase.UserTable.COL_PASSWORD));

            if (foundPassword.equals(password)) {
                // Correct username and password
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("login_result", true); // You can pass additional data if needed
                setResult(LOGIN_SUCCESSFUL, resultIntent);
                sharedPreferences.edit().putBoolean("isLoggedInPref", true).apply();
                finish(); // Finish the LoginActivity and return to MainActivity
            } else {
                // Incorrect password
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            };
        } else {
            // User not found
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        };

        if (cursor != null) {
            cursor.close();
        };

        db.close();
    };
};
