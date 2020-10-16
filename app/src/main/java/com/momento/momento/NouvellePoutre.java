package com.momento.momento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class NouvellePoutre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_poutre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nouvelle_poutre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.valide) {
            EditText champNom = findViewById(R.id.editNom);
            EditText champLongueur = findViewById(R.id.editLongueur);
            EditText champForce = findViewById(R.id.editForce);

            boolean valide = true;
            if (TextUtils.isEmpty(champNom.getText().toString())) {
                champNom.setError("Champ obligatoire");
                valide = false;
            }
            if (TextUtils.isEmpty(champLongueur.getText().toString())) {
                champLongueur.setError("Champ obligatoire");
                valide = false;
            }
            if (TextUtils.isEmpty(champForce.getText().toString())) {
                champForce.setError("Champ obligatoire");
                valide = false;
            }

            if (valide) {
                String nom = champNom.getText().toString();
                double longueur = Double.valueOf(champLongueur.getText().toString());
                double force = Double.valueOf(champForce.getText().toString());
                int type = 1;
                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioType1:
                        type = 1;
                        break;
                    case R.id.radioType2:
                        type = 2;
                        break;
                    case R.id.radioType3:
                        type = 3;
                        break;
                    case R.id.radioType4:
                        type = 4;
                        break;
                    case R.id.radioType5:
                        type = 5;
                        break;
                }
                PoutresDBB db = new PoutresDBB(getBaseContext());
                db.open();
                int id2 = (int) db.insertPoutre(new Poutre(nom, type, longueur, force));
                db.close();
                Intent intent = new Intent(NouvellePoutre.this, PoutreDetails.class);
                intent.putExtra("id", id2);
                startActivity(intent);
                overridePendingTransition(R.transition.enter_from_right, R.transition.exit_out_left);
                return true;
            }
        }
        else if(id == android.R.id.home){
            startActivity(new Intent(NouvellePoutre.this,PoutreDetails.class));
            overridePendingTransition(R.transition.enter_from_left, R.transition.exit_out_right);
        }
        return super.onOptionsItemSelected(item);
    }
}
