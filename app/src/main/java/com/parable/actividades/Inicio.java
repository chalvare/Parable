package com.parable.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.parable.parablesdb.R;

/**
 * Created by Altair on 12/11/15.
 */
public class Inicio extends Activity {
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                Intent i = new Intent(Inicio.this, MainActivity.class);

                startActivity(i);
            }
        }, 1000);



    }
}
