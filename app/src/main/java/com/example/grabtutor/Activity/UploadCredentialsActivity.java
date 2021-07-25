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

import com.example.grabtutor.Model.CredentialsModel;
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

import java.util.Calendar;

public class UploadCredentialsActivity extends AppCompatActivity {

    private EditText domain, experience, description;
    private Button submit, upload, back;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private ImageView document;
    private Uri imageUri;
    private String upload_image_prompt = "https://getstamped.co.uk/wp-content/uploads/WebsiteAssets/Placeholder.jpg";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_credentials);

        back = findViewById(R.id.button_upload_credentials_back);
        domain = findViewById(R.id.editText_upload_credentials_domain);
        experience = findViewById(R.id.editText_upload_credentials_experience);
        description = findViewById(R.id.editText_upload_credentials_description);
        submit = findViewById(R.id.button_upload_credentials_submit);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Credentials");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        document = findViewById(R.id.imageView_upload_credentials_picture);
        upload = findViewById(R.id.button_upload_credentials_upload_image);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadCredentialsActivity.this, Credentials.class));
            }
        });

        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("credentials/");
                startActivityForResult(galleryIntent, 2);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                    Toast.makeText(UploadCredentialsActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadCredentialsActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(EditUserProfileActivity.this, UserProfileActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        firebaseDatabase.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Picasso.get().load(upload_image_prompt).into(document);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void submit() {
        String domain_input = domain.getText().toString().trim();
        String experience_input = experience.getText().toString().trim();
        String description_input = description.getText().toString().trim();
        DatabaseReference UIDref = (DatabaseReference) firebaseDatabase.child(firebaseUser.getUid());

        if (domain_input.isEmpty()) {
            domain.setError("Field cannot be empty");
            domain.requestFocus();
            return;
        }
        if (experience_input.isEmpty()) {
            experience.setError("Field cannot be empty");
            experience.requestFocus();
            return;
        }
        if (description_input.isEmpty()) {
            description.setError("Field cannot be empty");
            description.requestFocus();
            return;
        }

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    String currentTime = Calendar.getInstance().getTime().toString();
                    String uNumber = firebaseUser.getUid() + Long.toString(Calendar.getInstance().getTimeInMillis());
                    CredentialsModel credentialsModel = new CredentialsModel(domain_input, experience_input +" Months", description_input, uNumber, currentTime, "Pending");
                    UIDref.child(uNumber).setValue(credentialsModel);
                    Toast.makeText(UploadCredentialsActivity.this, "Credentials submitted and is under review", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadCredentialsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void uploadToFirebase(Uri uri) {
        //StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        //name of the image in the storage
        StorageReference fileRef = storageReference.child("Profile Image").child(firebaseUser.getEmail() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //    UserImage userImage = new UserImage(uri.toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                        String userImageID = firebaseDatabase.push().getKey();
                        String uNumber = firebaseUser.getUid() + Long.toString(Calendar.getInstance().getTimeInMillis());
                        firebaseDatabase.child(firebaseUser.getUid()).child("credentials").child(uNumber).setValue(uri.toString());
                        //  progressBar.setVisibility(View.GONE);
                        Toast.makeText(UploadCredentialsActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UploadCredentialsActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
            document.setImageURI(imageUri);
        }

    }

    }

