package org.x.tongnews.activity;

import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.x.tongnews.R;
import org.x.tongnews.adapter.FragmentAdapter;
import org.x.tongnews.fragment.HomeFragment_;
import org.x.tongnews.fragment.PhotographerFragment_;
import org.x.tongnews.global.MApplication;

import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private Drawer drawerRoot;
    private AccountHeader accountHeaderRoot;
    private HomeFragment_ mHomeFragment;
    private FragmentManager mFragmentManager;

    @App
    MApplication mApplication;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.main_activity_tabs)
    TabLayout mTabLayout;
    @ViewById(R.id.main_activity_viewpager)
    ViewPager mViewPager;

    private int[] startPoint = new int[]{0,0};

    @AfterViews
    void init(){

        UmengUpdateAgent.update(this);

        setSupportActionBar(mToolbar);

        initTabs();

        mFragmentManager = getFragmentManager();

        accountHeaderRoot = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Ayaseruri").withEmail("ayaseruri@foxmail.com").withIcon(getResources().getDrawable(R.drawable.drawer_avatar))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        drawerRoot = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeaderRoot)
                .withTranslucentStatusBar(true)
                .withFullscreen(true)
                .withSelectedItem(1)
                .withShowDrawerOnFirstLaunch(true)
                .withCloseOnClick(true)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_square)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int position, long id, IDrawerItem iDrawerItem) {
                        return true;
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @UiThread
    void initTabs(){
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        String[] tabstitle = getResources().getStringArray(R.array.main_tabs_title);
        for(int i=0; i < tabstitle.length; i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(tabstitle[i]));
        }
        ArrayList<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomeFragment_.builder().arg("title", tabstitle[0]).build());
        fragmentList.add(PhotographerFragment_.builder().arg("title", tabstitle[1]).build());

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, tabstitle);
        mViewPager.setAdapter(fragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        startPoint[0] = (int)ev.getX();
        startPoint[1] = (int)ev.getY();
        return super.dispatchTouchEvent(ev);
    }

    public int[] getStartPoint(){
        return startPoint;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
