package com.momento.momento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.momento.momento.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private double abscisseV = 0;
    private Poutre p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poutre_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final int id = intent.getIntExtra("id",-1);
        if(id == -1){
            startActivity(new Intent(PoutreDetails.this,MainActivity.class));
        }
        final PoutresDBB db = new PoutresDBB(getBaseContext());
        db.open();
        p = db.getPoutresAvecID(id);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                }
                            })
                            .setNegativeButton("Non", null)
                            .show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageType = findViewById(R.id.imageType);
        TextView nom = findViewById(R.id.Nom);
        TextView idT = findViewById(R.id.Id);
        TextView type = findViewById(R.id.type);
        TextView longueur = findViewById(R.id.longueur);
        final TextView force = findViewById(R.id.force);
        TextView forceReaction = findViewById(R.id.forceReaction);
        final TextView momentReaction = findViewById(R.id.momentReaction);
        TextView tranchantMax = findViewById(R.id.tranchantMax);
        TextView flechissantMax = findViewById(R.id.flechissantMax);
        final TextView abscisse = findViewById(R.id.abscisse);
        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView tranchant= findViewById(R.id.tranchant);
        final TextView flechissant = findViewById(R.id.flechissant);

        final String pattern = "##.##";
        nom.setText(p.getNom());
        idT.setText(String.format(Locale.FRANCE,"# %d",p.getId()));
        longueur.setText(String.format(Locale.FRANCE,"%s m",new DecimalFormat(pattern).format(p.getLongueur())));
        force.setText(String.format(Locale.FRANCE,"%s kN",new DecimalFormat(pattern).format(p.getForce())));

        switch (p.getType()){
            case 1:
                type.setText("Deux appuis avec une charge ponctuelle au centre");
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_launcher_background));
                forceReactionV = p.getForce()/2;
                momentReactionV = 0;
                tranchantMaxV = p.getForce()/2;
                flechissantMaxV = p.getForce()*p.getLongueur()*4;
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
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_launcher_background));
                forceReactionV = p.getForce()*p.getLongueur()/2;
                momentReactionV = 0;
                tranchantMaxV = p.getForce()*p.getLongueur()/2;
                flechissantMaxV = Math.pow(p.getLongueur(),2)*p.getForce()/8;
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
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_launcher_background));
                forceReactionV = p.getForce()*p.getLongueur()/2;
                momentReactionV = -p.getForce()*Math.pow(p.getLongueur(),2)/12;
                tranchantMaxV = p.getForce()*p.getLongueur()/2;
                flechissantMaxV =  Math.pow(p.getLongueur(),2)*p.getForce()/24;
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
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_launcher_background));
                forceReactionV = p.getForce()*p.getLongueur();
                momentReactionV = -p.getForce()*Math.pow(p.getLongueur(),2)/2;
                tranchantMaxV = p.getForce()*p.getLongueur();
                flechissantMaxV =  -p.getForce()*Math.pow(p.getLongueur(),2)/2;
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
                imageType.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_launcher_background));
                forceReactionV = p.getForce();
                momentReactionV = -p.getForce()*p.getLongueur();
                tranchantMaxV = -p.getForce();
                flechissantMaxV =  -p.getForce()*Math.pow(p.getLongueur(),2)/2;
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

        seekBar.setProgress(5);


    }
}
