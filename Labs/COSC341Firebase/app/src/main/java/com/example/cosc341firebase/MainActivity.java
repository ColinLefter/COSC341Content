package com.example.cosc341firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox useDB = findViewById(R.id.writeOnDBCheck);
        boolean isChecked = useDB.isChecked();

        Button readDataButton = findViewById(R.id.readDataButton);
        readDataButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReadData.class);
            intent.putExtra("WRITE_ON_DATABASE", isChecked);
            startActivity(intent);
        });

        Button writeDataButton = findViewById(R.id.writeDataButton);
        writeDataButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WriteData.class);
            intent.putExtra("WRITE_ON_DATABASE", isChecked);
            startActivity(intent);
        });
    }













//    private OnCompleteListener<DataSnapshot> onValuesFetched = new OnCompleteListener<DataSnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<DataSnapshot> task) {
//            if (!task.isSuccessful()) {
//                DataSnapshot snap = task.getResult();
//                String value = snap.getValue().toString();
//                Log.e("firebase", "Error getting data", task.getException());
//            }
//        }
//    };
//    public void onUploadPressed(View view) {
//
//    }
//
//    private ValueEventListener listener = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            String value = snapshot.getValue().toString();
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    };

}