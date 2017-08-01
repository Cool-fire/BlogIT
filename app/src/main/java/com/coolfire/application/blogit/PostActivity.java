package com.coolfire.application.blogit;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class PostActivity extends AppCompatActivity {

    private ImageButton mImageButton;
    private static final int GALLERY_REQUESTCODE = 2;
    private EditText mPosttitle;
    private EditText mDescription;
    private Button mPostButton;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgressBar;
    private DatabaseReference mDatabase;
    private Bitmap mBitmap;
    private static final int PERMISSIONS_REQUEST_READ_STORAGE = 100;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUESTCODE && resultCode == RESULT_OK)
        {
            mImageUri = data.getData();
            mImageButton.setImageURI(mImageUri);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageButton = (ImageButton)findViewById(R.id.imageSelect);
        mPostButton =(Button)findViewById(R.id.Postbutton);
        mPosttitle = (EditText)findViewById(R.id.Post_title);
        mDescription = (EditText)findViewById(R.id.Description);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mProgressBar = new ProgressDialog(this);

       ActivityCompat.requestPermissions(PostActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_REQUEST_READ_STORAGE);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartPosting();
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUESTCODE);
            }
        });
    }

    private void StartPosting() {

        mProgressBar.setMessage("Posting...");

       final String Title_val = mPosttitle.getText().toString().trim();
       final String desc_val = mDescription.getText().toString().trim();
        if(!TextUtils.isEmpty(Title_val)&&!TextUtils.isEmpty(desc_val)) {
            mProgressBar.show();


            if ( mImageUri== null) {
                DatabaseReference Post = mDatabase.push();
                Post.child("Title").setValue(Title_val);
                Post.child("Description").setValue(desc_val);
                mProgressBar.dismiss();
                Toast.makeText(this,"Posted Succesfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostActivity.this,MainActivity.class));
            } else {
                StorageReference path = mStorage.child("Blog_images").child(Random()+".png");
               /* path.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadURL = taskSnapshot.getDownloadUrl();
                        DatabaseReference Post = mDatabase.push();
                        Post.child("Title").setValue(Title_val);
                        Post.child("Description").setValue(desc_val);
                        Post.child("Image").setValue(downloadURL.toString());
                        mProgressBar.dismiss();
                        startActivity(new Intent(PostActivity.this,MainActivity.class));
                        });*/
                InputStream input = null;
                try {
                    input = this.getContentResolver().openInputStream(mImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                UploadTask upladtask = path.putStream(input);
                upladtask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadURL = taskSnapshot.getDownloadUrl();
                        DatabaseReference Post = mDatabase.push();
                        Post.child("Title").setValue(Title_val);
                        Post.child("Description").setValue(desc_val);
                        Post.child("Image").setValue(downloadURL.toString());
                        mProgressBar.dismiss();
                        startActivity(new Intent(PostActivity.this,MainActivity.class));
                    }
                });

            }

            }
        }

    public static String Random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}



