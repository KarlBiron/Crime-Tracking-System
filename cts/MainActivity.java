package ae.ac.ud.ceit.cts;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import ae.ac.ud.ceit.cts.tabs.feeds.ListCrimesFragment;
import ae.ac.ud.ceit.cts.tabs.manage.ManageCrimeFragment;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
      //      R.drawable.ic_action_name,
       //     R.drawable.ic_action_name,
         //   R.drawable.ic_action_name
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   if (DEVELOPER_MODE)
        {
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListCrimesFragment(), "Home");
        adapter.addFragment(new ManageCrimeFragment(), "New Crime");
       // adapter.addFragment(new CrimeSearch(), "Search");
        adapter.addFragment(new ListCrimesFragment(), "Serial Killer");

   //     adapter.addFragment(new GroupFragment(), "Group");
    //    adapter.addFragment(new CallFragment(), "Calls");
        viewPager.setAdapter(adapter);

    }

    private void setupTabIcons() {
 /*       tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);*/
    }

}