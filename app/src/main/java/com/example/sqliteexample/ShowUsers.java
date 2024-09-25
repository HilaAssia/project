package com.example.sqliteexample;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowUsers extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);


        DBHelper dbHelper = new DBHelper(this);
        //MainActivity.addUsers(dbHelper);
        displayUsers(this, findViewById(R.id.rvMain), dbHelper);

    }
    private void displayUsers(Context context, RecyclerView recyclerView, DBHelper dbHelper) {
        List<User> users = dbHelper.selectAll();
        UserAdapter adapter = new UserAdapter(users);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}