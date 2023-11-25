package com.example.cosc341firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = FirebaseDatabase.getInstance().getReference();

        CheckBox useDB = findViewById(R.id.writeOnDBCheck);

        Button readDataButton = findViewById(R.id.readDataButton);
        readDataButton.setOnClickListener(v -> {
            boolean isChecked = useDB.isChecked(); // we must always get the latest state
            Intent intent = new Intent(MainActivity.this, ReadData.class);
            intent.putExtra("WRITE_ON_DATABASE", isChecked);

            // right before we start the activity, we need to check for data
            checkForData(isChecked, hasData -> { // the asynchronous nature of the listener requires us to use callbacks
                if (hasData) {
                    startActivity(intent);
                } else {
                    Toast.makeText(
                            this,
                            "No data yet. Please write some data first.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });

        Button writeDataButton = findViewById(R.id.writeDataButton);
        writeDataButton.setOnClickListener(v -> {
            boolean isChecked = useDB.isChecked();
            Intent intent = new Intent(MainActivity.this, WriteData.class);
            intent.putExtra("WRITE_ON_DATABASE", isChecked);
            startActivity(intent);
        });
    }

    public interface DataCheckCallback { // we need to define an interface in order to handle the event that we have data
        void onDataCheckResult(boolean hasData);
    }

    private void checkForData(boolean useDB, DataCheckCallback callback) {
        if (useDB) {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    callback.onDataCheckResult(dataSnapshot.getChildren().iterator().hasNext()); // if we simply have some data present
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("firebase", "Error fetching data", databaseError.toException());
                    callback.onDataCheckResult(false);
                }
            });
        } else {
            try (
                    FileInputStream fis = openFileInput("data.txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis))
            ) {
                callback.onDataCheckResult(br.readLine() != null);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onDataCheckResult(false);
            }
        }
    }
}