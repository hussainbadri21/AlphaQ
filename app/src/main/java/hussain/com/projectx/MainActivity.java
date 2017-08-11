package hussain.com.projectx;

import android.content.ClipData;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Drawer.OnDrawerItemClickListener {

    FrameLayout mainActivityFrameLayout;
    private Drawer result;
    ImageView hamburgerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityFrameLayout = (FrameLayout) findViewById(R.id.main_framelayout);
        hamburgerImageView = (ImageView) findViewById(R.id.hamburger_icon);
        hamburgerImageView.setOnClickListener(this);

        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(R.drawable.nav_home_black);
        PrimaryDrawerItem itemSos = new PrimaryDrawerItem().withIdentifier(1).withName("SOS").withIcon(R.drawable.nav_sos);
        PrimaryDrawerItem itemProfile = new PrimaryDrawerItem().withIdentifier(1).withName("Profile").withIcon(R.drawable.nav_profile);
        PrimaryDrawerItem itemAbout = new PrimaryDrawerItem().withIdentifier(1).withName("About").withIcon(R.drawable.nav_about_us_black);
        PrimaryDrawerItem itemLogout = new PrimaryDrawerItem().withIdentifier(1).withName("Log Out").withIcon(R.drawable.nav_logout);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.navbar_background)
                .addProfiles(
                        new ProfileDrawerItem().withName("Narayana Suri")
                                .withEmail("narayanasuri@chelsea.co.uk").withIcon(getResources().getDrawable(R.drawable.profile_img))
                )
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
