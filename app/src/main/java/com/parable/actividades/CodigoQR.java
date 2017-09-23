package com.parable.actividades;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.parable.parablesdb.R;

/**
 * Created by Altair on 19/2/16.
 */
public class CodigoQR extends ActionBarActivity {


    private Button crearQR;
    private EditText entradaQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codigoqr);

        // Scanner
        crearQR = (Button) findViewById(R.id.crearqr);
        entradaQR = (EditText) findViewById(R.id.entradaQR);


        crearQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(CodigoQR.this);


                //Aqui es donde le metemos nuestro codigo para que no se pueda falsear.
                int numRandom = 123344;

                String prueba = "Parable" + numRandom + "_" + entradaQR.getText().toString();
                integrator.shareText(prueba);

            }
        });

    }


}