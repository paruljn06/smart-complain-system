package com.example.database;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etName, etAge;
    private Button btnInsert, btnView, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String ageStr = etAge.getText().toString();

            if (name.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertData(name, Integer.parseInt(ageStr));
            if (inserted) {
                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnView.setOnClickListener(v -> {
            Cursor cursor = dbHelper.getAllData();
            if (cursor.getCount() == 0) {
                showMessage("Data", "Nothing found");
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (cursor.moveToNext()) {
                buffer.append("ID: ").append(cursor.getString(0)).append("\n");
                buffer.append("Name: ").append(cursor.getString(1)).append("\n");
                buffer.append("Age: ").append(cursor.getString(2)).append("\n\n");
            }

            showMessage("Data", buffer.toString());
        });

        btnUpdate.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String ageStr = etAge.getText().toString();

            if (name.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = dbHelper.updateData(name, Integer.parseInt(ageStr));
            if (updated) {
                Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            String name = etName.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter name to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean deleted = dbHelper.deleteData(name);
            if (deleted) {
                Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void clearFields() {
        etName.setText("");
        etAge.setText("");
    }
}
