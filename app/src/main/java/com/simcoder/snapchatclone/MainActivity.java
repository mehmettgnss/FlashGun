package com.simcoder.snapchatclone;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.simcoder.snapchatclone.fragment.CameraFragment;
import com.simcoder.snapchatclone.fragment.ChatFragment;
import com.simcoder.snapchatclone.fragment.MusicFragment;
import com.simcoder.snapchatclone.fragment.StoryFragment;
import com.simcoder.snapchatclone.music.MusicPlayerActivity;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int[] tabIcons = {R.drawable.message,R.drawable.camera,R.drawable.storyy,R.drawable.music};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserInformation userInformationListener = new UserInformation();
        userInformationListener.startFetching();


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        viewPager.setCurrentItem(1);
    }
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatFragment(),"Zero");
        adapter.addFragment(new CameraFragment(),"One");
        adapter.addFragment(new StoryFragment(), "Two");
        adapter.addFragment(new MusicFragment(), "Three");
        viewPager.setAdapter(adapter);
    }
}
