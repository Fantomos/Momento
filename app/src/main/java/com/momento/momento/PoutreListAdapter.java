package com.momento.momento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class PoutreListAdapter extends ArrayAdapter<Poutre> {
    private ArrayList<Poutre> poutreList;
    private Activity context;
    public PoutreListAdapter(Activity context, ArrayList<Poutre> poutreList) {
        super(context, R.layout.item_poutre);
        this.poutreList = poutreList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return poutreList.size();
    }

    @Override
    public View getView(int position, final View viewf, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_poutre, null, true);
        TextView textId = rowView.findViewById(R.id.textId);
        TextView textNom = rowView.findViewById(R.id.textNom);
        TextView textType = rowView.findViewById(R.id.textType);
        TextView textLongueur = rowView.findViewById(R.id.textLongueur);
        TextView textForce = rowView.findViewById(R.id.textForce);
        ImageView imageType = rowView.findViewById(R.id.imageType);
        final Poutre p = poutreList.get(position);
        textId.setText(String.format("#%d",p.getId()));
        textNom.setText(p.getNom());
        switch (p.getType()){
            case 1:
                textType.setText("Deux appuis avec une charge ponctuelle au centre");
                imageType.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.type1));
                break;
            case 2:
                textType.setText("Deux appuis avec une charge répartie");
                imageType.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.type2));
                break;
            case 3:
                textType.setText("Bi-encastrée avec une charge répartie");
                imageType.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.type3));
                break;
            case 4:
                textType.setText("Encastrée avec une charge répartie");
                imageType.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.type4));
                break;
            case 5:
                textType.setText("Encastrée avec une charge poncutelle au bout");
                imageType.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.type5));
                break;
        }
        final String pattern = "##.##";
        textLongueur.setText(String.format(Locale.FRANCE,"L = %s m",new DecimalFormat(pattern).format(p.getLongueur())));
        textForce.setText(String.format(Locale.FRANCE,"F = %s kN",new DecimalFormat(pattern).format(p.getForce())));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PoutreDetails.class);
                intent.putExtra("id",p.getId());
                getContext().startActivity(intent);
            }
        });
        return rowView;
    }

    @Override
    public void add(@Nullable Poutre object) {
        poutreList.add(object);
        super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Poutre> collection) {
        poutreList.addAll(collection);
        super.addAll(collection);
    }

    @Override
    public void clear() {
        poutreList.clear();
        super.clear();
    }
}