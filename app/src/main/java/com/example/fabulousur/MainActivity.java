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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    private Button j1;
    private Button j2;
    static private int totalJetsJ1;
    static private int totalJetsJ2;
    static private Map<Integer, Integer> scoresJ1;
    static private Map<Integer, Integer> scoresJ2;
    static private TextView score;
    static private int result;


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
            }
        });
        j2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchDices(false);
            }
        });
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                reset();
            }
        });
    }

    private void launchDices(final boolean j){
        Random r = new Random();
        result = 0;
        for(int i = 0; i < 4; i++){
            int lancer =  r.nextInt(2);
            if(lancer > 0){
                result ++;
            }
        }
        new CompositeDisposable().add(Observable.interval(50, TimeUnit.MILLISECONDS)
            .take(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    Random r2 = new Random();
                    score.setText("Résultat : " + String.valueOf(r2.nextInt(5)));
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    score.setText("Résultat : " + String.valueOf(result));
                    if(j) {
                        scoresJ1.put(result, scoresJ1.get(result) + 1 );
                        totalJetsJ1 += 1;
                    } else {
                        scoresJ2.put(result, scoresJ2.get(result) + 1  );
                        totalJetsJ2 += 1;
                    }
                    actualiserAffichage(false);
                }
            })
        );

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

        if(reset){
            for(int i = 1; i < colonnes; i++){
                TextView textView = (TextView) grille.getChildAt(i + colonnes);
                textView.setText("0%");
                TextView textViewj1 = (TextView) grille.getChildAt(i + (colonnes * 2));
                textViewj1.setText("0%");
                TextView textViewj2 = (TextView) grille.getChildAt(i + (colonnes * 3));
                textViewj2.setText("0%");
            }
            tirages.setText("Tirages");
            score.setText("Lancer les dés");
        } else {
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
}
