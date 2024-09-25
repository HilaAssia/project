package com.example.sqliteexample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);


        DBHelper dbHelper = new DBHelper(this);
        //MainActivity.addUsers(dbHelper);
        displayUsers(this, findViewById(R.id.rvMain), dbHelper);

    }
    private void displayUsers(Context context, RecyclerView recyclerView, DBHelper dbHelper) {
        users = dbHelper.selectAll();
        UserAdapter adapter = new UserAdapter(users);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        // add click listener
        ItemClickListener itemClickListener = new ItemClickListener(); // nested class below

        RecyclerItemClickListener gridListener = new RecyclerItemClickListener(this, recyclerView, itemClickListener);
        recyclerView.addOnItemTouchListener(gridListener);
    }

    private class ItemClickListener implements RecyclerItemClickListener.OnItemClickListener
    {
        @Override
        public void onItemClick(View view, int position)
        {
            Toast.makeText(getApplicationContext(), "selected: " + users.get(position).getEmail(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLongItemClick(View view, int position)
        {
            Toast.makeText(getApplicationContext(), "long click: " + users.get(position).getEmail(), Toast.LENGTH_LONG).show();
        }
    }
}