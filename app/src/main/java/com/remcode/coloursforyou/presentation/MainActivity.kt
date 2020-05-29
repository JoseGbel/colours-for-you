package com.remcode.coloursforyou.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.remcode.coloursforyou.R
import com.remcode.coloursforyou.utils.NetworkStatusLiveData

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        navigationView.setNavigationItemSelectedListener(this)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // initialise the Network Status detector
        NetworkStatusLiveData.init(application)


        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ColourGeneratorFragment())
                .commit()
            navigationView.setCheckedItem(R.id.nav_colour_generator)
        }


    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_colour_generator -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ColourGeneratorFragment())
                    .commit()
            }

            R.id.nav_my_colours -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ColourListFragment())
                    .commit()
            }
        }

        item.isChecked = true
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}