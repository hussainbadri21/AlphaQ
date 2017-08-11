package hussain.com.projectx;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import com.squareup.picasso.Target;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Drawer.OnDrawerItemClickListener {

    FrameLayout mainActivityFrameLayout;
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

        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(R.drawable.nav_home_black);
        PrimaryDrawerItem itemSos = new PrimaryDrawerItem().withIdentifier(1).withName("SOS").withIcon(R.drawable.nav_sos);
        PrimaryDrawerItem itemProfile = new PrimaryDrawerItem().withIdentifier(1).withName("Profile").withIcon(R.drawable.nav_profile);
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
        });        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.navbar_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(sharedPreferences.getString("name",""))
                                .withEmail(sharedPreferences.getString("email","")).withIcon(Uri.parse(sharedPreferences.getString("img",""))))
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        //Launch Profile Activity
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
                        itemHome,
                        itemSos,
                        itemProfile,
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
            case 2 :
                SOSFragment sosFragment = SOSFragment.newInstance("", "");
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_framelayout, sosFragment);
                transaction.commit();
                result.closeDrawer();
                break;
        }
        return true;
    }

}
