package com.android.timesheet.shared.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.timesheet.App;
import com.android.timesheet.R;
import com.android.timesheet.shared.presenters.BasePresenter;
import com.android.timesheet.shared.presenters.Presenter;
import com.android.timesheet.shared.widget.CircularProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vamsikonanki on 8/18/2017.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements Presenter {


    @Nullable
    @BindView(R.id.app_bar)
    protected AppBarLayout appBarLayout;

    @Nullable
    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    @Nullable
    @BindView(R.id.toolbar_layout)
    protected CollapsingToolbarLayout toolbarLayout;

    /*@Nullable
    @BindView(R.id.toolbar_image)
    protected ImageView toolbarImageView;
    @Nullable
    @BindView(R.id.progressBarProfile)
    protected CircularProgressBar progressBarProfile;

    @Nullable
    @BindView(R.id.toolbar_content)
    protected ViewGroup toolbarContent;

    @Nullable
    @BindView(R.id.toolbar_scrim)
    protected View toolbarScrim;*/

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    private T mPresenter;

    protected Menu mMenu;

    protected ActionBar actionBar;

    protected boolean isBackButtonFromMain = false;

    protected int layoutRestID() {
        return -1;
    }

    protected int menuResID() {
        return -1;
    }

    protected boolean isBackButtonFromMain() {
        return isBackButtonFromMain;
    }

    protected String title() {
        return null;
    }

    protected boolean isSubscriber() {
        return false;
    }

    protected boolean showBackButton() {
        return false;
    }

    protected int resourceForUpIndicator() {
        return R.drawable.icon_white_back;
    }

    protected boolean isToolBarTransparent() {
        return false;
    }

    @Override
    public T presenter() {
        if (mPresenter == null) {
            mPresenter = providePresenter();

            if (mPresenter == null) {
                throw new RuntimeException("Presenter must be initialized first");
            }
        }

        return mPresenter;
    }

    protected T providePresenter() {
        return null;
    }


    //region Lifecycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutResID = layoutRestID();
        if (layoutResID > 0) {
            setContentView(layoutResID);
            ButterKnife.bind(this);
        }

        String title = title();
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (isToolBarTransparent()) {
                //toolbar.getBackground().setAlpha(0);
                toolbar.setBackgroundColor(Color.TRANSPARENT);
                toolbar.setTitleTextColor(Color.TRANSPARENT);
            }

        }

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (showBackButton()) {
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(resourceForUpIndicator());
            }
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

//    protected MenuItem menuItemSwitch = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuResID = menuResID();
        boolean hasOptionMenu = (menuResID > 0);

        if (hasOptionMenu) {
            getMenuInflater().inflate(menuResID, menu);
        }

        mMenu = menu;

//        if(mMenu!=null){
//            menuItemSwitch = mMenu.findItem(R.id.action_menu_home);
//        }

        return hasOptionMenu || showBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !isBackButtonFromMain()) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        supportInvalidateOptionsMenu();

        if (mMenu != null) {
            onPrepareOptionsMenu(mMenu);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (isSubscriber()) {
            App.getInstance().getBus().register(this);
        }*/
    }

    @Override
    protected void onPause() {
        /*if (isSubscriber()) {
            App.getInstance().getBus().unregister(this);
        }*/

        if (mPresenter != null) {
            presenter().onPause();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        /*if (layoutRestID() > 0) {
            ButterKnife.bind(this);
        }*/

        super.onDestroy();
    }

    protected void clearToolbarMenu() {
        Menu menu = toolbar.getMenu();
        if (menu != null && menu.size() > 0) {
            menu.clear();
        }

    }

    //endregion









}
