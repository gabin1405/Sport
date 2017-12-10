package com.example.cornug.sportmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

    /*
############################################
#Sport activity                            #
#Récupération de la liste des              #
#sport des classes, puis affichage et      #
#sélection                                 #
#                                          #
#Auteur : Gabin                            #
############################################

*/

public class SportActivity extends AppCompatActivity implements AsyncResponse {

    String ip="http://projetut.pe.hu/tut.php?sql=";
    RadioGroup rg;
    String classe;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    List<String> listeString;
    Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        rg = (RadioGroup) findViewById(R.id.rgSport);
        listeString = new ArrayList<String>();
        //On récupère le nom de la classe précédemment sélectionnée
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        classe = preferences.getString("classe", "");

        //On créé une tâche asynchrone qui va chercher les sports correspondant à la classe
        //sélectionnée
        JSONTask recupSport = new JSONTask("nom_sport");
        recupSport.delegate = this;
        recupSport.execute(ip+"SELECT%20SPORT.nom_sport%20FROM%20PRATIQUE,%20SPORT,CLASSE%20WHERE%20SPORT.id_sport=PRATIQUE." +
                "id_sport%20AND%20CLASSE.id_classe=PRATIQUE.id_classe%20AND%20CLASSE.nom_classe='" + classe + "';");

    }

    @Override
    public void processFinish(List output) {
        //Même fonctionnenement que pour la classe ChoixActivity
        listeString = output;
        for (String s : listeString) {

            RadioButton rd = new RadioButton(context);
            rd.setText(s);
            rd.setTextSize(40);
            rd.setTextColor(Color.GRAY);
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = preferences.edit();
            editor.putString("sport", "");
            rd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("sport", ((Button) v).getText().toString());
                    editor.apply();
                    Intent displayActivity = new Intent(context, TypeEval.class);
                    context.startActivity(displayActivity);


                }
            });
            //rd.setId(Integer.parseInt(s));
            rg.addView(rd);

        }
    }
}
