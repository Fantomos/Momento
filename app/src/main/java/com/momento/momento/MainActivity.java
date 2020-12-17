package com.momento.momento;


import android.content.Intent;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private ArrayList<Poutre> allPoutres;
    private ArrayList<Poutre> adapterListPoutres;
    private PoutreListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NouvellePoutre.class));
                overridePendingTransition(R.transition.enter_from_right, R.transition.exit_out_left);
            }
        });

        PoutresDBB db = new PoutresDBB(this);
        db.open();
        allPoutres = db.getAllPoutres();
        adapterListPoutres = new ArrayList<>(allPoutres);
        adapter = new PoutreListAdapter(this,adapterListPoutres);
        ListView listView = (ListView) findViewById(R.id.poutreListe);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        final MenuItem myMenuItem = menu.findItem( R.id.action_recherche);
        searchView = (SearchView) myMenuItem.getActionView();
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View arg0) {
               adapter.clear();
               adapter.addAll(allPoutres);
               adapter.notifyDataSetChanged();
            }
            @Override
            public void onViewAttachedToWindow(View arg0) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.clear();
                for (Poutre p : allPoutres) {
                    if (p.getNom().toLowerCase().contains(s.toLowerCase()) || String.valueOf(p.getId()).contains(s.toLowerCase())) {
                        adapter.add(p);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_recherche) {
            return true;
        }
        else if(id == R.id.action_parametre){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(R.transition.enter_from_right, R.transition.exit_out_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
