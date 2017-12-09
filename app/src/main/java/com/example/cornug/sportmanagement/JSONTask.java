package com.example.cornug.sportmanagement;

import android.os.AsyncTask;

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
######################################
#JSON TASK                           #
#Connexion au fichier php renvoyant  #
#le JSON puis récupération des infos #
#sous forme d'une liste              #
#                                    #
#Auteur : Gabin,Guillaume            #
######################################

*/

class JSONTask extends AsyncTask<String, String, String> {
    String data="",singleParsed="",valcherch="";


    public JSONTask (String valcherch) {

        this.valcherch=valcherch;
    }
    List<String> listeString = new ArrayList<String>();
    public AsyncResponse delegate = null;

    //Requêtes HTML pour se connecter au fichier php
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
            //On récupère les valeurs sous forme d'une liste
            JSONArray JA = new JSONArray(data);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                singleParsed = JO.get(valcherch) + "";
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

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        delegate.processFinish(listeString);




    }


}


