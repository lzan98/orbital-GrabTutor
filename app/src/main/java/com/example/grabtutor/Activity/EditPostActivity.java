package com.example.grabtutor.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImage;
import com.example.grabtutor.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl, categoryName, postId;

    private ImageView close;
    private ImageView imageAdded;
    private TextView post;
    private EditText description;
    private EditText title;
    private EditText price;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        postId = getIntent().getStringExtra("postId");
        categoryName = getIntent().getStringExtra("categoryName");
        imageUrl = getIntent().getStringExtra("imageUrl");

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        price.setFilters(new InputFilter[]{filter});


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        FirebaseDatabase.getInstance().getReference("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String editTitle = "" + snapshot.child("title").getValue();
                String editDescription= "" + snapshot.child("description").getValue();
                String editPrice = "" + snapshot.child("price").getValue();
                Picasso.get().load(imageUrl).into(imageAdded);
                title.setText(editTitle);
                description.setText(editDescription);
                price.setText(editPrice);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void upload() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

                String newDesc = description.getText().toString().trim();
                String newTitle = title.getText().toString().trim();
                String newPrice = price.getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);

        ref.child("descrption").setValue(newDesc);
        ref.child("title").setValue(newTitle);
        ref.child("price").setValue(newPrice);

        pd.dismiss();
        finish();

    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUriContent();
            imageAdded.setImageURI(imageUri);
        } else {
            finish();
        }
    }*/


    @Override
    protected void onStart() {
        super.onStart();
    }
}