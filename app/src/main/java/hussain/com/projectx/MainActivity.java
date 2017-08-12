package hussain.com.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, Drawer.OnDrawerItemClickListener, GoogleApiClient.OnConnectionFailedListener {

    FrameLayout mainActivityFrameLayout;
    GoogleApiClient mGoogleApiClient;
    private Drawer result;
    ImageView hamburgerImageView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        mainActivityFrameLayout = (FrameLayout) findViewById(R.id.main_framelayout);
        hamburgerImageView = (ImageView) findViewById(R.id.hamburger_icon);
        hamburgerImageView.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        PrimaryDrawerItem itemSos = new PrimaryDrawerItem().withIdentifier(1).withName("SOS").withIcon(R.drawable.nav_sos);
        PrimaryDrawerItem itemProfile = new PrimaryDrawerItem().withIdentifier(1).withName("Profile").withIcon(R.drawable.nav_profile);
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(R.drawable.nav_home_black);
        PrimaryDrawerItem itemAbout = new PrimaryDrawerItem().withIdentifier(1).withName("About").withIcon(R.drawable.nav_about_us_black);
        PrimaryDrawerItem itemLogout = new PrimaryDrawerItem().withIdentifier(1).withName("Log Out").withIcon(R.drawable.nav_logout);
        //  Picasso.with(getApplicationContext()).load(sharedPreferences.getString("img","")).into(dp);
        // BitmapDrawable bp=(BitmapDrawable)dp.getDrawable();
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.navbar_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(sharedPreferences.getString("name", ""))
                                .withEmail(sharedPreferences.getString("email", "")).withIcon(Uri.parse(sharedPreferences.getString("img", ""))))
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        //Launch ProfileFragment Activity
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                }).build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        itemSos,
                        itemProfile,
                        itemHome,
                        new DividerDrawerItem(),
                        itemAbout,
                        itemLogout
                )
                .withOnDrawerItemClickListener(this)
                .build();

        result.closeDrawer();
        result.setSelection(1);

    }

    @Override
    public void onClick(View v) {
        if (v == hamburgerImageView) {
            if (result.isDrawerOpen())
                result.closeDrawer();
            else
                result.openDrawer();
        }
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        FragmentTransaction transaction;
        switch (position) {
            case 1:
                SOSFragment sosFragment = SOSFragment.newInstance("", "");
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_framelayout, sosFragment);
                transaction.commit();
                result.closeDrawer();
                break;
            case 2:
                ProfileFragment pf = ProfileFragment.newInstance("", "");
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_framelayout, pf);
                transaction.commit();
                result.closeDrawer();
                break;
            case 3:
                final Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case 6:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent1);
                            }
                        });
                break;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("tag", "onConnectionFailed:" + connectionResult);
    }
}
