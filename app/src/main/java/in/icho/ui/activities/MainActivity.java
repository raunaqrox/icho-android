package in.icho.ui.activities;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import in.icho.R;
import in.icho.ui.fragments.HomeFragment;
import in.icho.ui.fragments.PlayerFragment;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private PlayerFragment playerFragment;
    private SlidingUpPanelLayout slidingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        slidingPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player);

        slidingPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
                playerFragment.setState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }

            @Override
            public void onPanelExpanded(View view) {
                playerFragment.setState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(slidingPanel.getPanelState() == PanelState.EXPANDED ||
                slidingPanel.getPanelState() == PanelState.ANCHORED){
            slidingPanel.setPanelState(PanelState.COLLAPSED);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class MainPagerAdapter extends FragmentStatePagerAdapter {
        private String tabNames[] = {"New", "Popular", "Featured"};
        public MainPagerAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int position) {
            HomeFragment fr;
            switch (position) {
                case 0:
                    fr = new HomeFragment();
                    fr.setType(this.tabNames[position]);
                    return fr;
                case 1:
                    fr = new HomeFragment();
                    fr.setType(this.tabNames[position]);
                    return fr;
                case 2:
                    fr = new HomeFragment();
                    fr.setType(this.tabNames[position]);
                    return fr;
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.tabNames[position];
        }
    }

    public PlayerFragment getPlayerFragment() {
        return playerFragment;
    }
}
