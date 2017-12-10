package com.example.cornug.sportmanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/*
##################################
#Main activity                   #
#Récupération des ID et mdp prof #
#depuis la BDD                   #
#A faire : isoler JSON           #
#Auteur : Gabin, Guillaume       #
##################################

*/

public class MainActivity extends AppCompatActivity{
    String ip="http://projetut.pe.hu/tut.php?sql=";
    EditText username,password;
    Context context;
    String dataParsed="",res="",compareString;
    Button btn;
    boolean auth;
    List<String> listeString;
    String data =""
            , singleParsed="";
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Définition de la view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assignation aux ID du XML
        username = (EditText) findViewById(R.id.input_user);
        password = (EditText) findViewById(R.id.input_password);

        username.setTextColor(Color.GRAY);
        password.setTextColor(Color.GRAY);

        //Création de la liste qui nous permettra de récupérer les valeurs de la BDD
        listeString = new ArrayList<String>();

        //Bouton valider : on envoie la requête quand on appuie dessus
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On place en $_GET la requête SQL
                new JSONTask().execute(ip+"SELECT%20*%20FROM%20PROFESSEUR");
            }
        });

    }

    //JSON à isoler
    private class JSONTask extends AsyncTask<String,String,String>
    {
        //Requêtes HTML pour se connecter au fichier PHP
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    data = data + line;
                }
                JSONArray JA = new JSONArray(data);
                //ici on récupère nos valeurs JSON, JO.get permet de récupérer
                //la valeur d'une colonne, le for parcourt toutes les colonnes
                for(int i = 0 ;i <JA.length(); i++){
                    JSONObject JO = (JSONObject) JA.get(i);
                    singleParsed =  JO.get("nom_prof") +""+JO.get("mdp_prof");
                    Log.e("",singleParsed);
                    listeString.add(singleParsed);


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        //onPostExecute est la fonction qui se lance après la récupération JSON
        @Override
        protected void onPostExecute(String result){

            //Ici on vérifie que le ID et mdp prof correspondent à une des lignes de la BDD
            super.onPostExecute(result);
            if(username.getText().toString()!=null && password.getText().toString()!=null){
                compareString=username.getText().toString()+password.getText().toString();
                for(String s : listeString){
                    Log.v("",""+s);
                    //Si les ID sont bons, alors on passe à la view suivante
                    if(s.equals(compareString)){
                        //Log.v("","Connexion autorisée");
                        auth=true;
                        ouvrirAccueil();

                    }
                }
            }
            if(!auth) {

                //Si les ID sont incorrect, on envoie un TOAST
                //et le texte devient rouge
                context = getApplicationContext();
                CharSequence text = "Identifiant ou mot de passe incorrect";
                int duration = Toast.LENGTH_SHORT;
                username.setTextColor(Color.RED);
                password.setTextColor(Color.RED);
                toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }



    }
    public void ouvrirAccueil(){
        Intent displayActivity = new Intent(this,ChoixActivity.class);
        this.startActivity(displayActivity);
    }
}