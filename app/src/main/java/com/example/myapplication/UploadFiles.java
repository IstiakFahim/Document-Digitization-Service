package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadFiles extends AppCompatActivity {

    Button mLockButton, mUnlockButton;

    TextView vEmail, vName;
    CircleImageView mImage;

    NavigationView nav;
    DrawerLayout drawerLayout;

    DatabaseReference mDatabaseUser;
    FirebaseAuth fAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    GoogleSignInClient mGoogleSignInClient;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);

        mLockButton = findViewById(R.id.LockBtn);
        mUnlockButton = findViewById(R.id.unlockBtn);

        mLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lock = new Intent(getApplicationContext(), UpoladLockFiles.class);
                startActivity(lock);
            }
        });

        mUnlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent unlock = new Intent(getApplicationContext(), UploadUnlockFiles.class);
                startActivity(unlock);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = (NavigationView) findViewById(R.id.navmenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        View headerView = nav.getHeaderView(0);
        vName = (TextView) headerView.findViewById(R.id.nav_name);
        vEmail = (TextView) headerView.findViewById(R.id.nav_email);
        mImage = (CircleImageView) headerView.findViewById(R.id.nav_pic);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        fAuth = FirebaseAuth.getInstance();

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileInfo userProfileInfo = snapshot.getValue(UserProfileInfo.class);

                vEmail.setText(userProfileInfo.getEmail());
                vName.setText(userProfileInfo.getUsername());


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
    }

}