package com.example.sqliteexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE =200 ;
    private ImageView ivProfile;
    private EditText etEmail;
    private EditText etPassword;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        dbHelper = new DBHelper(this);
        
        //addUsers(dbHelper);
    }

    public void showUsers(View view){
        Intent intent = new Intent(this, ShowUsers.class);
        startActivity(intent);
    }

    private void initViews()
    {
        ivProfile = findViewById(R.id.ivProfilePic);
        etEmail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
    }

    public void takePicture(View view)
    {
        // 1 create intent
        Intent intent = new Intent();
        // 2 Camera
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // Trigger the activity
        startActivityForResult(intent,MY_CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==MY_CAMERA_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                ivProfile.setImageBitmap(imageBitmap);
            }
        }
    }



    public void saveToDB(View view)
    {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        User u = new User(email,password);

        dbHelper.insert(u);

        User textUser = dbHelper.selectById(u.getId());

        ArrayList<User> testMail = dbHelper.genericSelectByEmail(u.getEmail());
    }

    public void saveToDBWithImage(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        Bitmap bitmap = ((BitmapDrawable) ivProfile.getDrawable()).getBitmap();

        // create user
        User u = new User(bitmap, email, password);
        User resultUser = dbHelper.insert(u);
        User testUser = dbHelper.selectById(resultUser.getId());

        ivProfile.setImageBitmap(testUser.getBitmap());
    }

    public static void addUsers(DBHelper dbHelper1) {
        dbHelper1.insert(new User("aJohn1 Doe", "john1.doe@example.com"));
        dbHelper1.insert(new User("aJohn2 Doe", "john2.doe@example.com"));
        dbHelper1.insert(new User("aJohn3 Doe", "john3.doe@example.com"));
        dbHelper1.insert(new User("aJohn4 Doe", "john4.doe@example.com"));
        dbHelper1.insert(new User("aJohn5 Doe", "john5.doe@example.com"));
        dbHelper1.insert(new User("aJohn6 Doe", "john6.doe@example.com"));
    }
}