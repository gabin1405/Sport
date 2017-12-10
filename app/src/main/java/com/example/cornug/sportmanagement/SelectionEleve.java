package com.example.cornug.sportmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SelectionEleve extends AppCompatActivity implements AsyncResponse {

    String ip = "http://projetut.pe.hu/tut.php?sql=";

    SharedPreferences preferences;
    String classe;
    SharedPreferences.Editor editor;
    Context context = this;
    List<String> listeString;
    RadioGroup listEleve;
    int test=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_eleve);

        listEleve = (RadioGroup) findViewById(R.id.RGEleve);
        listeString = new ArrayList<String>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        classe = preferences.getString("classe", "");

        JSONTask recupNomEleve = new JSONTask("nom_eleve");
        JSONTask recupPrenomEleve = new JSONTask("prenom_eleve");
        //recupNomEleve.delegate = this;
        //recupNomEleve.execute(ip + "SELECT%20*%20FROM%20ELEVE,CLASSE%20WHERE%20ELEVE.id_classe=CLASSE.id_classe%20AND%20nom_classe='" + classe + "'");
        try {
            Log.e("list?",recupNomEleve.get()+"");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        recupPrenomEleve.delegate = this;
        recupPrenomEleve.execute(ip + "SELECT%20*%20FROM%20ELEVE,CLASSE%20WHERE%20ELEVE.id_classe=CLASSE.id_classe%20AND%20nom_classe='" + classe + "'");
    }

    public void processFinish(List output) {


        listeString = output;
        Log.e("val",test+"");

        for (String s : listeString) {

            //Ici pour chaque nom de classe on va créer un RadioButton portant le nom de cette classe
            //Pour conserver le nom de la classe au travers de l'appli, on l'enregistre dans
            //un fichier préférences
            RadioButton rd = new RadioButton(context);
            rd.setText(s);
            rd.setTextSize(40);
            rd.setTextColor(Color.GRAY);

            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = preferences.edit();
            rd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Si le RB est pressé, on enregistre sa valeur et on passe à la
                    //view suivante
                    editor.putString("eleve", ((Button) v).getText().toString());
                    editor.apply();
                    Intent displayActivity = new Intent(context, SportActivity.class);
                    context.startActivity(displayActivity);

                }
            });
            //On ajoute le RB au RadioGroup
            listEleve.addView(rd);


        }

    }
}
