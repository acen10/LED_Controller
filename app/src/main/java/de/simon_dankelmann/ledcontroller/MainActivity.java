package de.simon_dankelmann.ledcontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

// IMPORTING THE COLORPICKER
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private LedController ledController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GET SETTINGS
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ledController = new LedController(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // CONNECT COLORPICKER AND OPACITYBAR
        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        picker.addOpacityBar(opacityBar);

        //SET COLORPICKER LISTENER
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                ledController.changeColor(color);
            }
        });

        Switch onOffSwitch = (Switch)findViewById(R.id.onOffSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
                    ledController.changeColor(picker.getColor());
                } else {
                    ledController.switchOff();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // CLICKHANDLES ON NAVIGATIONITEMS WILL BE HANDLED HERE
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // OPEN SETTINGS ACTIVITY
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_exit) {
            // SWITCH OFF LED'S IF NOT DEATIVATED
            boolean bSwitchOff = sharedPreferences.getBoolean("PREF_TURNOFFONEXIT", true);
            if(bSwitchOff){
                ledController.switchOff();
            }
            // EXIT APP
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

