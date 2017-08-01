package com.coolfire.application.blogit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
//import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    //FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerView mBloglist;
    private DatabaseReference mDatabase;
    private static int SIGN_IN_REQUEST_CODE = 1;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mBloglist = (RecyclerView)findViewById(R.id.blog_list);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));
        if(FirebaseAuth.getInstance().getCurrentUser() ==null)
        {

            startActivityForResult( AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);
        }
        else
        {
            if(savedInstanceState == null) {
                Toast.makeText(MainActivity.this, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            }

        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaserecycleradapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(Blog.class,R.layout.blog_row,BlogViewHolder.class,mDatabase) {

            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, final Blog model, int position) {
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDescription());
                if(model.getImage()!=null) {
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                }
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(MainActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,showActivity.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("desc",model.getDescription());
                        if(model.getImage()!=null)
                        {
                            intent.putExtra("image",model.getImage());
                        }
                        startActivity(intent);
                    }
                });


            }
        };
        mBloglist.setAdapter(firebaserecycleradapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder  {
        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
             mView = itemView;
        }
        public void setTitle(String Title)
        {
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(Title);
        }
        public void setDesc(String desc)
        {
            TextView post_desc = (TextView)mView.findViewById(R.id.post_description);
            post_desc.setText(desc);
        }
        public void setImage(Context ctx,String image)
        {
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(MainActivity.this,"Signed in",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this,"Sorry",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add)
        {
            startActivity(new Intent(this,PostActivity.class));
        }
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
