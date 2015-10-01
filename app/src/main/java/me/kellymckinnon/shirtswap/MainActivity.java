package me.kellymckinnon.shirtswap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final int SIGN_IN_REQUEST = 10;

    private ImageView mChoosingIcon;
    private ImageView mMatchesIcon;
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private ImageView mSellingIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        mChoosingIcon = (ImageView) findViewById(R.id.logo_icon);
        mChoosingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        mMatchesIcon = (ImageView) findViewById(R.id.chat_icon);
        mMatchesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        mSellingIcon = (ImageView) findViewById(R.id.sell_icon);
        mSellingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });

        mChoosingIcon.setSelected(true);
        toggleColor(mChoosingIcon);
        toggleColor(mMatchesIcon);
        toggleColor(mSellingIcon);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        if (UserDataSource.getCurrentUser() == null) {
            Intent i = new Intent(this, SignInActivity.class);
            startActivityForResult(i, SIGN_IN_REQUEST);

        } else {
            updateDrawer();
        }
    }

    private void updateDrawer() {
        CircleImageView userPhoto = (CircleImageView) findViewById(R.id.user_photo);
        Picasso.with(this)
                .load(UserDataSource.getCurrentUser().getLargePictureURL())
                .noFade()
                .into(userPhoto);
        TextView userName = (TextView) findViewById(R.id.user_name);
        userName.setText(UserDataSource.getCurrentUser().getFirstName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST && resultCode == RESULT_OK) {
            updateDrawer();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mChoosingIcon.setSelected(true);
                mMatchesIcon.setSelected(false);
                mSellingIcon.setSelected(false);
                break;
            case 1:
                mChoosingIcon.setSelected(false);
                mMatchesIcon.setSelected(true);
                mSellingIcon.setSelected(false);
                break;
            case 2:
                mChoosingIcon.setSelected(false);
                mMatchesIcon.setSelected(false);
                mSellingIcon.setSelected(true);
                break;
        }

        toggleColor(mChoosingIcon);
        toggleColor(mMatchesIcon);
        toggleColor(mSellingIcon);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Makes the selected tab heading white and the others gray.
     *
     * @param v view to change
     */
    private void toggleColor(ImageView v) {
        if (v.isSelected()) {
            v.setColorFilter(Color.WHITE);
        } else {
            v.setColorFilter(getResources().getColor(R.color.primary_dark));
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChoosingFragment();
                case 1:
                    return new MatchesFragment();
                case 2:
                    return new SellingFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
