package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPublicFile extends AppCompatActivity {

    TextView vEmail, vName;
    CircleImageView mImage;

    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mDatabaseUser;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    DatabaseReference RDatabase;
    FirebaseAuth fAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    NavigationView nav;
    DrawerLayout drawerLayout;

    ActionBarDrawerToggle toggle;

    private AppBarConfiguration mAppBarConfiguration;

    //the listview
    ListView listView;

    //database reference to get uploads data
    Query mDatabaseReference;

    //list to store uploads data
    List<Upload> uploadList;

    ImageButton mLockFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_public_file);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = (NavigationView) findViewById(R.id.navmenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        View headerView = nav.getHeaderView(0);
        vName = (TextView) headerView.findViewById(R.id.nav_name);
        vEmail = (TextView) headerView.findViewById(R.id.nav_email);
        mImage = (CircleImageView) headerView.findViewById(R.id.nav_pic);

        mLockFiles = findViewById(R.id.lockButton);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        fAuth = FirebaseAuth.getInstance();

        RDatabase = FirebaseDatabase.getInstance().getReference().child("upload").child("public");

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileInfo userProfileInfo = snapshot.getValue(UserProfileInfo.class);

                vName.setText(userProfileInfo.getUsername());
                vEmail.setText(userProfileInfo.getEmail());
                Picasso.get().load(userProfileInfo.Img_url).into(mImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mLockFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent privatefiles = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(privatefiles);
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.my_files:
                        Toast.makeText(getApplicationContext(), "My Files is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.upload_files:
                        Toast.makeText(getApplicationContext(), "Upload Files is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), UploadFiles.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.profile:
                        Toast.makeText(getApplicationContext(), "All Files is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.all_files:
                        Toast.makeText(getApplicationContext(), "All Files is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), AllFileActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.trash:
                        Toast.makeText(getApplicationContext(), "Trash Files is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), TrashFiles.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.recent:
                        Toast.makeText(getApplicationContext(), "Recent File is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), RecentFiles.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.stared:
                        Toast.makeText(getApplicationContext(), "Starred Files is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Starred.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.shared:
                        Toast.makeText(getApplicationContext(), "Shared Files is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), SharedFiles.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.logout:
                        Toast.makeText(getApplicationContext(), "SLogging Out", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        mGoogleSignInClient.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.chatbot:
                        Toast.makeText(getApplicationContext(), "Chat Bot is Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        uploadList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        //adding a clicklistener on listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Upload upload = uploadList.get(i);
                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(upload.getUrl()));
                startActivity(intent);
            }
        });


        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads").child("public").orderByChild("uid").equalTo(user.getUid());
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /*DELETE METHOD*/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //   Upload upload = uploadList.get(position);
                final String post_key = (String) listView.getItemAtPosition(position);

                Query query = RDatabase.child(post_key);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String mpostKey = snapshot.getRef().getKey();
                        new AlertDialog.Builder(MyPublicFile.this)
                                .setIcon(android.R.drawable.ic_delete)
                                .setTitle("Are you sure ?")
                                .setMessage("Do you want to delete this file")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), mpostKey, Toast.LENGTH_LONG).show();
                                        /*RDatabase.child("name").child(mpostKey).removeValue();*/


                                        Query applesQuery = ref.child("uploads").child("private").orderByChild("name").equalTo(mpostKey);
                                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                }
                                                Intent newclass = new Intent(MyPublicFile.this, MyPublicFile.class);

                                                startActivity(newclass);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                return true;

            }
        });


    }
}