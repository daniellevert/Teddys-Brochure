package com.group17.teddysbrochure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;


import com.group17.teddysbrochure.ui.explore.ExploreFragment;
import com.group17.teddysbrochure.ui.history.HistoryFragment;
import com.group17.teddysbrochure.ui.home.HomeFragment;
import com.group17.teddysbrochure.ui.map.MapsFragment;
import com.group17.teddysbrochure.ui.reviews.ReviewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
     * Global variables
     */
    public static String  parkNameGlobal = "DeathValley"; // Death Valley is the default park
    public static String parkNameGlobalUserFacing = "Death Valley National Park";

    // the lists of strings used for the search feature.
    // searchKeyDbFields will contain the name of the National Park as it appears in the database
    // searchKeyUserFacing will contain the name of the National Park as it will be displayed to the user
    // there must be a 1 - 1 relationship between the indices of these two lists
    // NOTE: We should consider making a class that contains both of these items to avoid potential errors
    private List<String> searchKeyDbFields = new ArrayList<>(); // will hold the keys that the database can recognize
    private List<String> searchKeyUserFacing = new ArrayList<>(); // will hold the keys that are displayed to the user

    private ArrayAdapter<String> arrayAdapter;
    private ListView parkListViews;
    private LinearLayout searchParkWrapper;
    private FrameLayout fragmentLayout;

    /*
     * Initializes variables when the main activity view is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creates view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets the app's displayed title as the current park
        ActionBar ab = getSupportActionBar();
        ab.setTitle(parkNameGlobalUserFacing);

        // Used to display the list of parks for search
        parkListViews = findViewById(R.id.my_list);
        searchParkWrapper = findViewById(R.id.searchParkWrapper);

        // Load the two data arrays with the parks
        searchKeyUserFacing.add("Death Valley National Park");
        searchKeyDbFields.add("DeathValley");
        searchKeyUserFacing.add("Grand Canyon National Park");
        searchKeyDbFields.add("GrandCanyon");

        // Array adapter to display the parks list
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchKeyUserFacing);
        parkListViews.setAdapter(arrayAdapter);

        // Sets up bottom navigation listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        fragmentLayout = findViewById(R.id.fragment_container);
        // Opens view with Home page fragment if no other fragment selected
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }

    /*
     * Sets up search menu and the action listeners for the search
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflates a menu with the search icon and a SearchView
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nav_menu, menu);
        MenuItem searchIcon = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) searchIcon.getActionView();
        searchView.setQueryHint("Search parks");

        // SearchView listener for typing text into the search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Submit the change to the search query
            @Override
            public boolean onQueryTextSubmit(String s) {
                parkNameGlobal = s; // change the global search key
                return false;
            }

            // Change the search query
            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });

        // AdapterView listener for each park in the park ListView
        parkListViews.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> plv, View v, int pos, long id){
                // change the search query and submit
                searchView.setQuery(searchKeyDbFields.get(pos), true);
                parkNameGlobalUserFacing = searchKeyUserFacing.get(pos);

                // restart MainActivity to reflect new National Park
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // MenuItem expansion listener to switch between search list and the fragment layout
        // when the search is expanded or collapsed
        searchIcon.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchParkWrapper.bringToFront();
                searchParkWrapper.invalidate();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                fragmentLayout.bringToFront();
                fragmentLayout.invalidate();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Changes which page's fragment is shown based on which bottom navigation item is selected
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Changes selected fragment based on the bottom nav icon that's clicked
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_maps:
                        selectedFragment = new MapsFragment();
                        break;
                    case R.id.nav_explore:
                        selectedFragment = new ExploreFragment();
                        break;
                    case R.id.nav_reviews:
                        selectedFragment = new ReviewsFragment();
                        break;
                    case R.id.nav_history:
                        selectedFragment = new HistoryFragment();
                        break;
                }

                // Replaces current fragment with the selected fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

                return true;
            }
        };

}