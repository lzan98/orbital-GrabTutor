package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grabtutor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class EditUserProfileActivity extends AppCompatActivity {

    private EditText et_first_name, et_last_name, et_country, et_phone, et_interest;
    private Button update, back, upload;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView profile_picture;
    private StorageReference storageReference;
    private Uri imageUri;
    private String upload_image_prompt = "https://getstamped.co.uk/wp-content/uploads/WebsiteAssets/Placeholder.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        et_first_name = findViewById(R.id.editText_edit_user_profile_first_name);
        et_last_name = findViewById(R.id.editText_edit_user_profile_last_name);
        et_country = findViewById(R.id.editText_edit_user_profile_country);
        et_phone = findViewById(R.id.editText_edit_user_profile_phone);
        et_interest = findViewById(R.id.editText_edit_user_profile_interest);
        update = findViewById(R.id.button_edit_user_profile_update);
        back = findViewById(R.id.button_edit_user_profile_back);
        storageReference = FirebaseStorage.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String UID = user.getUid();
        profile_picture = findViewById(R.id.imageView_edit_user_profile_picture);
        upload = findViewById(R.id.button_edit_user_profile_upload_image);

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                startActivityForResult(galleryIntent, 2);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                    Toast.makeText(EditUserProfileActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditUserProfileActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(EditUserProfileActivity.this, UserProfileActivity.class);
                  startActivity(intent);
                  finish();
            }
        });

        ref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Picasso.get().load(upload_image_prompt).into(profile_picture);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void update() {
        String first_name = et_first_name.getText().toString().trim();
        String last_name = et_last_name.getText().toString().trim();
        String country = et_country.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String interest = et_interest.getText().toString().trim();
        DatabaseReference UIDref = (DatabaseReference) ref.child(user.getUid());

        if (first_name.isEmpty()) {
            et_first_name.setError("Field cannot be empty");
            et_first_name.requestFocus();
            return;
        }
        if (last_name.isEmpty()) {
            et_last_name.setError("Field cannot be empty");
            et_last_name.requestFocus();
            return;
        }
        if (country.isEmpty()) {
            et_country.setError("Field cannot be empty");
            et_country.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            et_phone.setError("Field cannot be empty");
            et_phone.requestFocus();
            return;
        }
        if (interest.isEmpty()) {
            et_interest.setError("Field cannot be empty");
            et_interest.requestFocus();
            return;
        }

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        UIDref.child("first_name").setValue(first_name);
                        UIDref.child("last_name").setValue(last_name);
                        UIDref.child("country").setValue(country);
                        UIDref.child("phone").setValue(phone);
                        UIDref.child("interest").setValue(interest);
                        Toast.makeText(EditUserProfileActivity.this, "Information updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditUserProfileActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditUserProfileActivity.this, "Error" , Toast.LENGTH_SHORT).show();
                    }
                }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void uploadToFirebase(Uri uri) {
        //StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        //name of the image in the storage
        StorageReference fileRef = storageReference.child(mAuth.getCurrentUser().getEmail() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                    //    UserImage userImage = new UserImage(uri.toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                        String userImageID = ref.push().getKey();
                        ref.child(mAuth.getCurrentUser().getUid()).child("profile_picture").setValue(uri.toString());
                     //  progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditUserProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
               // progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
              //  progressBar.setVisibility(View.GONE);
                Toast.makeText(EditUserProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profile_picture.setImageURI(imageUri);
        }

    }
}