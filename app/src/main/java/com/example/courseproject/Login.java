package com.example.courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final AddressBookDatabase addressBookDatabase = new AddressBookDatabase(this);

        final Toolbar toolbarCustom = findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbarCustom);

        final EditText editTextUsername = findViewById(R.id.editTextUsername);
        final EditText editTextPassword = findViewById(R.id.editTextPassword);

        final Button buttonLogin = findViewById(R.id.buttonLogin);
        final Button buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "A valid username and password is required to login", Toast.LENGTH_SHORT).show();
                }
                else {
                    int userId = addressBookDatabase.validLogin(username, password);

                    if (userId != -1) {
                        Toast.makeText(Login.this, "Signed in as " + username, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(v.getContext(), AddressBook.class);
                        intent.putExtra("username", username);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "A valid username and password is required to sign up", Toast.LENGTH_SHORT).show();
                }
                else {
                    int userId = addressBookDatabase.validSignUp(username, password);

                    if (userId != -1) {
                        Toast.makeText(Login.this, "Sign up successfull", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(v.getContext(), AddressBook.class);
                        intent.putExtra("username", username);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Username already taken", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_extras, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuExit:
                Intent myIntent = new Intent(Intent.ACTION_MAIN);
                myIntent.addCategory(Intent.CATEGORY_HOME);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                System.exit(0);
                return true;
            default:
                return false;
        }
    }
}