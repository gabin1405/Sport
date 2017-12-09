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
##################################
#Choix activity                  #
#Récupération de la liste des    #
#classes, puis affichage et      #
#sélection                       #
#                                #
#Auteur : Gabin,                  #
##################################

*/

public class ChoixActivity extends AppCompatActivity implements AsyncResponse {
    //IP à changer en fonction de l'endroit
    String ip="http://172.20.10.3:8888/tut.php?sql=";
    RadioGroup listSport, listClasse;
    List<String> listeString;
    Context context = this;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    /* TRES IMPORTANT

    Procédure à suivre pour la récupération de données :
    1)Implémenter l'interface AsyncResponse

    2)Créer une tâche JSON prenant en paramètre la colonne où l'on
    souhaite récupérer la valeur :

     JSONTASK task = new JSONTASK("colonne");

    3) task.delegate = this;
    4)Récupérer la liste dans la fonction processFinish :

    public void processFinish(List output) {
        listeRécupérée=output;
    }
     */




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);

        listeString = new ArrayList<String>();
        listClasse = (RadioGroup) findViewById(R.id.rgClasse);

        //Création de la tâche Asynchrone qui va récupérer la liste des noms de classe
        JSONTask recupClasse = new JSONTask("nom_classe");
        recupClasse.delegate = this;
        recupClasse.execute(ip+"SELECT%20nom_classe%20FROM%20classe");

        int i = 0;

    }
    //Les actions à effectuer après la tâche asynchrone
    public void processFinish(List output) {


        listeString=output;
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
            editor.putString("classe","5emeA");
            rd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Si le RB est pressé, on enregistre sa valeur et on passe à la
                    //view suivante
                    editor.putString("classe",((Button) v).getText().toString());
                    editor.apply();
                    Intent displayActivity = new Intent(context,SportActivity.class);
                    context.startActivity(displayActivity);

                }
            });
            //On ajoute le RB au RadioGroup
            listClasse.addView(rd);


        }


    }

}
