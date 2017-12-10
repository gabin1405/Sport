package com.example.cornug.sportmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class TypeEval extends AppCompatActivity {

    RadioButton evalInd,evalColl;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_eval);

        evalInd = (RadioButton) findViewById(R.id.evalInd);
        evalColl = (RadioButton) findViewById(R.id.evalColl);

        evalInd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayActivity = new Intent(context,SelectionEleve.class);
                context.startActivity(displayActivity);

            }
        });

        evalColl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}
