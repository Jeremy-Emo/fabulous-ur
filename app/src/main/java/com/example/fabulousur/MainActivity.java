package com.example.fabulousur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button j1;
    private Button j2;
    static private int totalJetsJ1;
    static private int totalJetsJ2;
    static private Map<Integer, Integer> scoresJ1;
    static private Map<Integer, Integer> scoresJ2;
    static private TextView score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        j1 = findViewById(R.id.buttonJ1);
        j2 = findViewById(R.id.buttonJ2);
        scoresJ1 = new HashMap<>();
        scoresJ2 = new HashMap<>();
        totalJetsJ1 = 0;
        totalJetsJ2 = 0;
        int count = 0;
        while(count <= 4){
            scoresJ1.put(count, 0);
            scoresJ2.put(count, 0);
            count += 1;
        }

        score = findViewById(R.id.textView);

        j1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchDices(true);
                actualiserAffichage(false);
            }
        });
        j2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchDices(false);
                actualiserAffichage(false);
            }
        });
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                reset();
            }
        });
    }

    private void launchDices(boolean j){
        Random r = new Random();
        int value =  r.nextInt(5);
        score.setText(String.valueOf(value));
        if(j) {
            scoresJ1.put(value, scoresJ1.get(value) + 1 );
            totalJetsJ1 += 1;
        } else {
            scoresJ2.put(value, scoresJ2.get(value) + 1  );
            totalJetsJ2 += 1;
        }
    }

    private void reset(){
        totalJetsJ1 = 0;
        totalJetsJ2 = 0;
        int count = 0;
        while(count <= 4){
            scoresJ1.put(count, 0);
            scoresJ2.put(count, 0);
            count += 1;
        }
        actualiserAffichage(true);
    }

    private void actualiserAffichage(boolean reset){
        TextView tirages = findViewById(R.id.tirages);
        android.support.v7.widget.GridLayout grille = findViewById(R.id.grille);

        int colonnes = grille.getColumnCount();
        int totalTiragesGlobal = totalJetsJ1 + totalJetsJ2;

        for(int i = 1; i < colonnes; i++){
            TextView textView = (TextView) grille.getChildAt(i + colonnes);
            textView.setText(String.valueOf( 100 * (scoresJ1.get(i - 1) + scoresJ2.get(i - 1)) / totalTiragesGlobal ) + "%");
            if(totalJetsJ1 > 0){
                TextView textViewj1 = (TextView) grille.getChildAt(i + (colonnes * 2));
                textViewj1.setText(String.valueOf( 100 * scoresJ1.get(i - 1) / totalJetsJ1 ) + "%");
            }
            if(totalJetsJ2 > 0 ){
                TextView textViewj2 = (TextView) grille.getChildAt(i + (colonnes * 3));
                textViewj2.setText(String.valueOf( 100 * scoresJ2.get(i - 1) / totalJetsJ2 ) + "%");
            }
        }

        tirages.setText(String.valueOf(totalTiragesGlobal) + " tirages");

    }
}
