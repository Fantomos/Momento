package com.momento.momento;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

public class PoutreDetails extends AppCompatActivity {

    private double forceReactionV = 0;
    private double momentReactionV = 0;
    private double tranchantMaxV = 0;
    private double flechissantMaxV = 0;
    private double tranchantV = 0;
    private double flechissantV = 0;
    private double flecheMaxV = 0;
    private double abscisseV = 0;
    private Poutre p;
    private int id;
    private PoutresDBB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poutre_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
        if(id == -1){
            startActivity(new Intent(PoutreDetails.this,MainActivity.class));
            overridePendingTransition(R.transition.enter_from_left, R.transition.exit_out_right);
        }
        db = new PoutresDBB(getBaseContext());
        db.open();
        p = db.getPoutresAvecID(id);


        final ImageView imageType = findViewById(R.id.imageType);
        final TextView nom = findViewById(R.id.Nom);
        final TextView idT = findViewById(R.id.Id);
        final TextView type = findViewById(R.id.type);
        final TextView longueur = findViewById(R.id.longueur);
        final TextView force = findViewById(R.id.force);
        final TextView young = findViewById(R.id.young);
        final TextView inertie = findViewById(R.id.inertie);
        final TextView forceReaction = findViewById(R.id.forceReaction);
        final TextView momentReaction = findViewById(R.id.momentReaction);
        final TextView tranchantMax = findViewById(R.id.tranchantMax);
        final TextView flechissantMax = findViewById(R.id.flechissantMax);
        final TextView abscisse = findViewById(R.id.abscisse);
        final SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView tranchant= findViewById(R.id.tranchant);
        final TextView flechissant = findViewById(R.id.flechissant);
        final TextView flecheMax = findViewById(R.id.fleche);

        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText nomEdit = findViewById(R.id.nomEdit);
                final EditText forceEdit = findViewById(R.id.forceEdit);
                final EditText longueurEdit = findViewById(R.id.longueurEdit);
                final EditText youngEdit = findViewById(R.id.youngEdit);
                final EditText inertieEdit = findViewById(R.id.inertieEdit);

                nom.setVisibility(View.INVISIBLE);
                force.setVisibility(View.INVISIBLE);
                longueur.setVisibility(View.INVISIBLE);
                young.setVisibility(View.INVISIBLE);
                inertie.setVisibility(View.INVISIBLE);

                nomEdit.setVisibility(View.VISIBLE);
                forceEdit.setVisibility(View.VISIBLE);
                longueurEdit.setVisibility(View.VISIBLE);
                youngEdit.setVisibility(View.VISIBLE);
                inertieEdit.setVisibility(View.VISIBLE);

                nomEdit.setText(p.getNom());
                forceEdit.setText(String.valueOf(p.getForce()));
                longueurEdit.setText(String.valueOf(p.getLongueur()));
                youngEdit.setText(String.valueOf(p.getYoung()));
                inertieEdit.setText(String.valueOf(p.getInertie()));

                fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.done));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean valide = true;
                        if (TextUtils.isEmpty(nomEdit.getText().toString())) {
                            nomEdit.setError("Champ obligatoire");
                            valide = false;
                        }
                        if (TextUtils.isEmpty(longueurEdit.getText().toString())) {
                            longueurEdit.setError("Champ obligatoire");
                            valide = false;
                        }
                        if (TextUtils.isEmpty(forceEdit.getText().toString())) {
                            forceEdit.setError("Champ obligatoire");
                            valide = false;
                        }
                        if (TextUtils.isEmpty(youngEdit.getText().toString())) {
                            youngEdit.setError("Champ obligatoire");
                            valide = false;
                        }
                        if (TextUtils.isEmpty(inertieEdit.getText().toString())) {
                            inertieEdit.setError("Champ obligatoire");
                            valide = false;
                        }
                        if (valide) {
                            p.setNom(nomEdit.getText().toString());
                            p.setForce(Double.parseDouble(forceEdit.getText().toString()));
                            p.setLongueur(Double.parseDouble(longueurEdit.getText().toString()));
                            p.setYoung(Double.parseDouble(youngEdit.getText().toString()));
                            p.setInertie(Double.parseDouble(inertieEdit.getText().toString()));
                            db.updatePoutre(p);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    }
                });
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        int nbDecimales = sharedPreferences.getInt("precision", 2);
        String pat = "##.";
        for (int i=0;i<nbDecimales;i++){
            pat = pat.concat("#");
        }
        final String pattern = pat;

        nom.setText(p.getNom());
        idT.setText(String.format(Locale.FRANCE,"# %d",p.getId()));
        longueur.setText(String.format(Locale.FRANCE,"%s m",new DecimalFormat(pattern).format(p.getLongueur())));
        force.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(p.getForce())));
        young.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(p.getYoung())));
        inertie.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(p.getInertie())));

        switch (p.getType()){
            case 1:
                type.setText("Deux appuis avec une charge ponctuelle au centre");
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.type1));
                forceReactionV = p.getForce()/2;
                momentReactionV = 0;
                tranchantMaxV = p.getForce()/2;
                flechissantMaxV = p.getForce()*p.getLongueur()/4;
                flecheMaxV = (p.getForce()*Math.pow(p.getLongueur(),3))/(48*p.getYoung()*p.getInertie());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        abscisseV = progress*(p.getLongueur()/10);
                        if(progress <= 5){
                            tranchantV = forceReactionV;
                            flechissantV = abscisseV*forceReactionV;
                        }else if(progress > 5){
                            tranchantV = -forceReactionV;
                            flechissantV = -abscisseV*forceReactionV + (p.getForce()*p.getLongueur()/2);
                        }
                        abscisse.setText(String.format(Locale.FRANCE,"X = %s m",new DecimalFormat(pattern).format(abscisseV)));
                        tranchant.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(tranchantV)));
                        flechissant.setText(String.format(Locale.FRANCE,"%s kN.m",new DecimalFormat(pattern).format(flechissantV)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                break;
            case 2:
                type.setText("Deux appuis avec une charge répartie");
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.type2));
                forceReactionV = p.getForce()*p.getLongueur()/2;
                momentReactionV = 0;
                tranchantMaxV = p.getForce()*p.getLongueur()/2;
                flechissantMaxV = Math.pow(p.getLongueur(),2)*p.getForce()/8;
                flecheMaxV = (5*p.getForce()*Math.pow(p.getLongueur(),4))/(384*p.getYoung()*p.getInertie());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        abscisseV = progress*(p.getLongueur()/10);
                        tranchantV = -abscisseV*p.getForce() + (p.getForce()*p.getLongueur()/2);
                        flechissantV = -Math.pow(abscisseV,2) * p.getForce()/2 + (p.getForce()*p.getLongueur()/2)*abscisseV;
                        abscisse.setText(String.format(Locale.FRANCE,"X = %s m",new DecimalFormat(pattern).format(abscisseV)));
                        tranchant.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(tranchantV)));
                        flechissant.setText(String.format(Locale.FRANCE,"%s kN.m",new DecimalFormat(pattern).format(flechissantV)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            case 3:
                type.setText("Bi-encastrée avec une charge répartie");
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.type3));
                forceReactionV = p.getForce()*p.getLongueur()/2;
                momentReactionV = -p.getForce()*Math.pow(p.getLongueur(),2)/12;
                tranchantMaxV = p.getForce()*p.getLongueur()/2;
                flechissantMaxV =  Math.pow(p.getLongueur(),2)*p.getForce()/24;
                flecheMaxV = (p.getForce()*Math.pow(p.getLongueur(),4))/(384*p.getYoung()*p.getInertie());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        abscisseV = progress*(p.getLongueur()/10);
                        tranchantV = -abscisseV*p.getForce() + (p.getForce()*p.getLongueur()/2);
                        flechissantV = -Math.pow(abscisseV,2) * p.getForce()/2 + (p.getForce()*p.getLongueur()/2)*abscisseV - p.getForce()*Math.pow(p.getLongueur(),2)/12;
                        abscisse.setText(String.format(Locale.FRANCE,"X = %s m",new DecimalFormat(pattern).format(abscisseV)));
                        tranchant.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(tranchantV)));
                        flechissant.setText(String.format(Locale.FRANCE,"%s kN.m",new DecimalFormat(pattern).format(flechissantV)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                break;
            case 4:
                type.setText("Encastrée avec une charge répartie");
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.type4));
                forceReactionV = p.getForce()*p.getLongueur();
                momentReactionV = -p.getForce()*Math.pow(p.getLongueur(),2)/2;
                tranchantMaxV = p.getForce()*p.getLongueur();
                flechissantMaxV =  -p.getForce()*Math.pow(p.getLongueur(),2)/2;
                flecheMaxV = (p.getForce()*Math.pow(p.getLongueur(),4))/(8*p.getYoung()*p.getInertie());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        abscisseV = progress*(p.getLongueur()/10);
                        tranchantV = p.getForce()*p.getLongueur() - abscisseV*p.getForce();
                        flechissantV = -p.getForce()*Math.pow(p.getLongueur()-abscisseV,2)/2;
                        abscisse.setText(String.format(Locale.FRANCE,"X = %s m",new DecimalFormat(pattern).format(abscisseV)));
                        tranchant.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(tranchantV)));
                        flechissant.setText(String.format(Locale.FRANCE,"%s kN.m",new DecimalFormat(pattern).format(flechissantV)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            case 5:
                type.setText("Encastrée avec une charge poncutelle au bout");
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.type5));
                forceReactionV = p.getForce();
                momentReactionV = -p.getForce()*p.getLongueur();
                tranchantMaxV = -p.getForce();
                flechissantMaxV =  -p.getForce()*Math.pow(p.getLongueur(),2)/2;
                flecheMaxV = (p.getForce()*Math.pow(p.getLongueur(),3))/(3*p.getYoung()*p.getInertie());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        abscisseV = progress*(p.getLongueur()/10);
                        tranchantV = tranchantMaxV;
                        flechissantV = -p.getForce()*(p.getLongueur()-abscisseV);
                        abscisse.setText(String.format(Locale.FRANCE,"X = %s m",new DecimalFormat(pattern).format(abscisseV)));
                        tranchant.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(tranchantV)));
                        flechissant.setText(String.format(Locale.FRANCE,"%s kN.m",new DecimalFormat(pattern).format(flechissantV)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
        }

        forceReaction.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(forceReactionV)));
        momentReaction.setText(String.format(Locale.FRANCE,"%s kN.m",new DecimalFormat(pattern).format(momentReactionV)));
        tranchantMax.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(tranchantMaxV)));
        flechissantMax.setText(String.format(Locale.FRANCE,"%s kN.m",new DecimalFormat(pattern).format(flechissantMaxV)));
        flecheMax.setText(String.format(Locale.FRANCE,"%s mm",new DecimalFormat(pattern).format(flecheMaxV*Math.pow(10,8))));

        seekBar.setProgress(5);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_poutre_details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id2 = item.getItemId();
        switch (id2){
            case android.R.id.home:
                startActivity(new Intent(PoutreDetails.this, MainActivity.class));
                overridePendingTransition(R.transition.enter_from_left, R.transition.exit_out_right);
                break;
            case R.id.action_delete:
                new AlertDialog.Builder(PoutreDetails.this)
                        .setIcon(R.drawable.delete)
                        .setTitle("Supprimer la poutre")
                        .setMessage("Etes-vous sûr de vouloir supprimer cette poutre ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.removePoutreAvecID(id);
                                db.close();
                                startActivity(new Intent(PoutreDetails.this,MainActivity.class));
                                overridePendingTransition(R.transition.enter_from_left, R.transition.exit_out_right);
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();
                break;
            case R.id.action_duplicate:
                new AlertDialog.Builder(PoutreDetails.this)
                        .setIcon(R.drawable.delete)
                        .setTitle("Dupliquer la poutre")
                        .setMessage("Etes-vous sûr de vouloir dupliquer cette poutre ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.insertPoutre(p);
                                db.close();
                                startActivity(new Intent(PoutreDetails.this,MainActivity.class));
                                overridePendingTransition(R.transition.enter_from_left, R.transition.exit_out_right);
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();

                break;
        }
        return true;
    }
}
