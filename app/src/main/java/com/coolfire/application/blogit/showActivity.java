package com.coolfire.application.blogit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class showActivity extends AppCompatActivity {
private TextView showTitle;
    private TextView showDescription;
    private ImageView showImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        showTitle = (TextView)findViewById(R.id.show_title);
        showDescription = (TextView)findViewById(R.id.show_description);
        showImage = (ImageView)findViewById(R.id.show_image);
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title").toString();
        String desc = bundle.getString("desc").toString();
        if(bundle.getString("image")!=null)
        {
            String image = bundle.getString("image").toString();
            Picasso.with(getApplicationContext()).load(image).into(showImage);
        }
        showTitle.setText(title);
        showDescription.setText(desc);
    }
}
