/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.vqeg.viqet.R;
import org.vqeg.viqet.adapters.CustomDrawerListAdapter;
import org.vqeg.viqet.asynctasks.VersionUpdateTask;
import org.vqeg.viqet.data.RemoteInfo;
import org.vqeg.viqet.data.RemoteInfoProvider;
import org.vqeg.viqet.fragments.AboutFragment;
import org.vqeg.viqet.fragments.HelpFragment;
import org.vqeg.viqet.fragments.HomeFragment;
import org.vqeg.viqet.fragments.ResultsListFragment;
import org.vqeg.viqet.fragments.SettingsFragment;
import org.vqeg.viqet.services.PhotoInspectorService;
import org.vqeg.viqet.utilities.SystemInfo;

public class MainActivity extends ActionBarActivity {

    public static int lastClicked = 0;
    public static LruCache<String, Bitmap> mMemoryCache;
    public DrawerLayout mDrawerLayout;
    public static ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private View downloadScreen;
    private View internetRequiredScreen;
    private View mainPage;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOptionsTitles;
    private int[] mImageResources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set orientation potrait if its phone
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };


        this.mainPage = findViewById(R.id.drawer_layout);
        this.downloadScreen = findViewById(R.id.downloading_screen);
        this.internetRequiredScreen = findViewById(R.id.internet_required_screen);

        mTitle = mDrawerTitle = "VIQET";
        mOptionsTitles = getResources().getStringArray(R.array.options_array);
        mImageResources = new int[]{R.drawable.home,R.drawable.result,R.drawable.help,R.drawable.about,R.drawable.setting};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new CustomDrawerListAdapter(this,
                 mOptionsTitles,mImageResources));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            //todo check
            selectItem(0);
            lastClicked=0;
        }else{
            int pos=savedInstanceState.getInt("FRAGMENT");
            selectItem(pos);
            lastClicked=pos;
        }

        Button retryDownloadButton = (Button) findViewById(R.id.retryDownload);
        retryDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemInfo.isNetworkPresent(getApplicationContext())) {
                    downloadScreen.setVisibility(View.VISIBLE);
                    internetRequiredScreen.setVisibility(View.INVISIBLE);
                    downloadRemoteInfo(PhotoInspectorService.FETCH_VERSION_ACTION);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.noInternetError), Toast.LENGTH_LONG).show();
                }

            }
        });

        //Check remote info
        RemoteInfo remoteInfo = RemoteInfoProvider.getRemoteInfo();

        if(remoteInfo == null)
        {
            mainPage.setVisibility(View.INVISIBLE);
            if(SystemInfo.isNetworkPresent(getApplicationContext())){
                downloadScreen.setVisibility(View.VISIBLE);
                internetRequiredScreen.setVisibility(View.INVISIBLE);
                downloadRemoteInfo(PhotoInspectorService.FETCH_VERSION_ACTION);
            }else{
                downloadScreen.setVisibility(View.INVISIBLE);
                internetRequiredScreen.setVisibility(View.VISIBLE);
            }
        }else{
            //downloadRemoteInfo(PhotoInspectorService.FETCH_VERSION_MODEL_CHANGED_ACTION);
            downloadRemoteInfo(PhotoInspectorService.INSPECT_ALL_PENDING_PHOTOS_ACTION);
        }
        checkFirstRun();
    }

    public void updateAccess(int accepted){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.licenseAcceptance), accepted);
        editor.apply();
    }

    public int readAccess(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int license = sharedPref.getInt(getString(R.string.licenseAcceptance), -1);
        return license;
    }

    private void showCustomDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.license_alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.text_switch).setView(dialogView);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateAccess(1);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateAccess(0);
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void checkFirstRun() {
        final String PREFS_NAME = "LicensePrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }
        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
            new VersionUpdateTask(MainActivity.this).execute();
            if(readAccess()==0||readAccess()==-1){
                showCustomDialog();
            }
            return;
        } else if (savedVersionCode == DOESNT_EXIST) {
            updateAccess(0);
            showCustomDialog();
        } else if (currentVersionCode > savedVersionCode) {
            new VersionUpdateTask(MainActivity.this).execute();
        }
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void downloadRemoteInfo(String action)
    {
        try
        {
            //Make a service request to fetch version and methodology
            Intent startServiceIntent = new Intent();
            startServiceIntent.setClass(this.getApplicationContext(), PhotoInspectorService.class);
            startServiceIntent.setAction(action);
            this.getApplicationContext().startService(startServiceIntent);
        }
        catch(Exception e){}
    }

    @Override
    protected void onResume()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PhotoInspectorService.BROADCAST_METHODOLOGY_FETCHED);
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean success = intent.getBooleanExtra(PhotoInspectorService.SUCCESS, false);

            if(success){
                mainPage.setVisibility(View.VISIBLE);
                downloadScreen.setVisibility(View.INVISIBLE);
                Log.i("TAG", "RemoteInfoFetched broadcast event received");
                selectItem(0); lastClicked=0;
            }else {
                downloadScreen.setVisibility(View.INVISIBLE);
                internetRequiredScreen.setVisibility(View.VISIBLE);
            }
        }
    };
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(lastClicked != position){
                selectItem(position);
                lastClicked = position;
            }
            else{
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        }
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        // update the main content by replacing fragments
        switch (position){
            case 0:
                    fragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "Home Fragment").commit();
                    break;
            case 1:
                    fragment = new ResultsListFragment();
                    FragmentManager fragmentManager1 = getFragmentManager();
                    fragmentManager1.beginTransaction().replace(R.id.content_frame, fragment, "Results List Fragment").commit();
                    break;
            case 2:
                fragment = new HelpFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.content_frame, fragment, "Help Fragment").commit();
                break;
            case 3:
                fragment = new AboutFragment();
                FragmentManager fragmentManager3 = getFragmentManager();
                fragmentManager3.beginTransaction().replace(R.id.content_frame, fragment, "About Fragment").commit();
                break;
            case 4:
                fragment = new SettingsFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.content_frame, fragment, "Settings Fragment").commit();
                break;
        }

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mOptionsTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static void setupUI(View view, final Activity activity) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    return false;
                }

            });
    }

    @Override
    public void onBackPressed() {
        if(lastClicked != 0) {
            selectItem(0);
            lastClicked=0;
        }else{
            super.onBackPressed();
        }
    }
}