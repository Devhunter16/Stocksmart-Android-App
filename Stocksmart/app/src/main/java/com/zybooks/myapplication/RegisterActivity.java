package com.zybooks.myapplication;

import android.content.ContentValues;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Button registerButton = findViewById(R.id.registerButton);
        Button goBackFromSignupButton = findViewById(R.id.goBackFromSignupButton);
        edtUsername = findViewById(R.id.usernameField);
        edtPassword = findViewById(R.id.passwordField);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            };
        });

        goBackFromSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            };
        });
    };

    private void addUser() {
        // Retrieving input
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        // Some validation
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            // Some kind of error here
            return;
        };

        UserDatabase dbHelper = new UserDatabase(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDatabase.UserTable.COL_USERNAME, username);
        values.put(UserDatabase.UserTable.COL_PASSWORD, password);
        long newRowId = db.insert(UserDatabase.UserTable.TABLE, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
            // Start the MainActivity after successful registration
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish the RegisterActivity to prevent going back to it with the back button
        } else {
            Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
        };

        db.close();
    };
};
