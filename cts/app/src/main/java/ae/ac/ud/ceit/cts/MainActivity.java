package ae.ac.ud.ceit.cts;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import ae.ac.ud.ceit.cts.tabs.feeds.ListCrimesFragment;
import ae.ac.ud.ceit.cts.tabs.manage.ManageCrimeFragment;


public class MainActivity extends AppCompatActivity {   //(extends=inherit)AppCombatActivity[Super-class] is part of Android support library

    private Toolbar toolbar;        //declaring toolbar
    private TabLayout tabLayout;    //declaring tablayout
    private ViewPager viewPager;    //declaring viewpager

    private int[] tabIcons = {
      //      R.drawable.ic_action_name,
       //     R.drawable.ic_action_name,
         //   R.drawable.ic_action_name
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   if (DEVELOPER_MODE)
        {
            //StrictMode is a developer tool which detects things
            // you might be doing by accident
            // and brings them to your attention so you can fix them.
            // StrictMode = standard block of code
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        //savedInstanceState=contains the activity's previously saved state
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //[ViewPager] = Layout manager that allows the user to flip left and right through pages of data
        //You supply an implementation of a [PagerAdapter] to generate the pages that the view shows.
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);     //viewpager is below the tab layout
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        //[TabLayout] = provides a horizontal layout to display tabs.
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);  //sliding_tabs is above the viewpager
        tabLayout.setupWithViewPager(viewPager);                            //associate tabs layout with viewpager

    }

    private void setupViewPager(ViewPager viewPager) {
        //PagerAdapter = Base class providing the adapter to populate pages inside of a ViewPager
        //getSupportFragmentManager() = found in FragmentActivity.java that works with FragmentManager object
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Tabs in the TabLayout
        adapter.addFragment(new ListCrimesFragment(), "Home");
        adapter.addFragment(new ManageCrimeFragment(), "New Crime");
       // adapter.addFragment(new CrimeSearch(), "Search");
        adapter.addFragment(new ListCrimesFragment(), "Serial Killer");

   //     adapter.addFragment(new GroupFragment(), "Group");
    //    adapter.addFragment(new CallFragment(), "Calls");
        viewPager.setAdapter(adapter);     //main page to be set as adapter chosen

    }

    // Unused method
    private void setupTabIcons() {
 /*       tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);*/
    }

}