package com.example.grabtutor.CopynPasteTemplate;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.grabtutor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class UploadImageFireBaseTemplate extends AppCompatActivity {

    private ImageView image;
    private Button upload;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Uri imageUri;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String path, fileType;

    public UploadImageFireBaseTemplate(String path, ImageView image, String fileType) {
        this.path = path;
        this.image = image;
        this.fileType = fileType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_fire_base_template);

        reference = FirebaseDatabase.getInstance().getReference(path);
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadOnClick();
//            }
//        });
    }

    public void thisFunction() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOnClick();
                uploadOnClick();
            }
        });
    }

    private void uploadToFirebase(Uri uri) {
        //StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        //name of the image in the storage
        StorageReference fileRef = storageReference.child(fileType + mAuth.getCurrentUser().getUid() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String userImageID = reference.push().getKey();
                        reference.child(mAuth.getCurrentUser().getUid()).setValue(imageUri);
                        Toast.makeText(UploadImageFireBaseTemplate.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UploadImageFireBaseTemplate.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
            image.setImageURI(imageUri);
        }
    }

    public void imageOnClick() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/");
        startActivityForResult(galleryIntent, 2);
    }

    public void uploadOnClick() {
        if (imageUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            uploadToFirebase(imageUri);
        } else{
            Toast.makeText(UploadImageFireBaseTemplate.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }
}