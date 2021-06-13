package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    TextView mEmail, mName, mphone;
    private ProgressDialog mProgress;
    CircleImageView mProfilePic;
    FirebaseAuth fAuth;
    Button mUpdate;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final int GALLERY_REQUEST=1;
    StorageReference storageReference;
    DatabaseReference mDatabaseUser;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    Uri mImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEmail = findViewById(R.id.userEmail);
        mName = findViewById(R.id.profile_name);
        mphone = findViewById(R.id.userPhone);
        mProfilePic= findViewById(R.id.image_profile);
        mProgress = new ProgressDialog(this);
        mUpdate = findViewById(R.id.update);


        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });


        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartPosting();
            }
        });


        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileInfo userProfileInfo = snapshot.getValue(UserProfileInfo.class);
                mEmail.setText(userProfileInfo.getEmail());
                mName.setText(userProfileInfo.getUsername());
                mphone.setText(userProfileInfo.getPhone());
                Picasso.get().load(userProfileInfo.Img_url).into(mProfilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void StartPosting() {


        mProgress.setMessage("Posting....");


//checking if all the fields are occupied


        if( mImageUri != null){
            mProgress.show();


//uploading image to Firebase Storage
            StorageReference filepath = storageReference.child("Profile_Picture").child(mImageUri.getLastPathSegment());


            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();

//posting post info to database

                            rootnode = FirebaseDatabase.getInstance();
                            reference = rootnode.getReference("users");
                            reference.child(user.getUid()).child("Img_url").setValue(downloadUrl);
                            Intent intToExp = new Intent(Profile.this, Profile.class);
                            finish();
                            startActivity(intToExp);
                            mProgress.dismiss();

                        }
                    });
                }
            });
        }
    }

    //image data from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            mImageUri = data.getData();
            mProfilePic.setImageURI(mImageUri);
        }
    }
}