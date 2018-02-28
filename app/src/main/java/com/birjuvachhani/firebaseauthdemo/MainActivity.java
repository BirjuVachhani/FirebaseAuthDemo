package com.birjuvachhani.firebaseauthdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DeleteUserFragment.DeleteEventListener, NavigationView.OnNavigationItemSelectedListener, ChangeEmailFragment.EmailChangeListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mdrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mnavigationView;

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mauth = FirebaseAuth.getInstance();

        setDrawer();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Toast.makeText(MainActivity.this,"Signed out",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        mauth.addAuthStateListener(authStateListener);
        super.onStart();
    }

    private void setDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mdrawerLayout, mtoolbar, R.string.open, R.string.close);

        actionBarDrawerToggle.syncState();
        mnavigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(mnavigationView.getMenu().getItem(0));
        mnavigationView.setCheckedItem(R.id.nav_home);
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
            setNavHeader(user.getEmail());
        }

    }

    private void setNavHeader(String email) {
        View view = mnavigationView.getHeaderView(0);
        ImageView mheader = view.findViewById(R.id.header_bg);
        ImageView mavatar = view.findViewById(R.id.avatar);
        TextView memailLabel = view.findViewById(R.id.tv_user_email);

        Picasso.with(this).load(R.drawable.header).fit().into(mheader);
        Picasso.with(this).load(R.drawable.avatar).fit().into(mavatar);
        memailLabel.setText(email);
    }

    @Override
    public void onDelete() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCancel() {
        mnavigationView.setCheckedItem(R.id.nav_home);
        onNavigationItemSelected(mnavigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        getSupportActionBar().setTitle(item.getTitle());
        switch (item.getItemId()) {
            case R.id.nav_home: {
                HomeFragment fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            }
            case R.id.nav_change_email: {
                ChangeEmailFragment fragment = new ChangeEmailFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            }
            case R.id.nav_change_password: {
                ChangePasswordFragment fragment = new ChangePasswordFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            }
            case R.id.nav_remove_user: {
                DeleteUserFragment fragment = new DeleteUserFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            }
            case R.id.nav_sign_out: {
                signOut();
                break;
            }
        }
        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        mauth.signOut();

    }

    @Override
    public void onBackPressed() {

        if (mdrawerLayout != null && mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onEmailChange() {
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
            setNavHeader(user.getEmail());
        }
    }

    @Override
    protected void onStop() {
        mauth.removeAuthStateListener(authStateListener);
        super.onStop();
    }
}
