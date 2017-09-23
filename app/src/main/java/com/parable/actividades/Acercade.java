package com.parable.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parable.parablesdb.R;

/**
 * Created by Altair on 16/2/16.
 */
public class Acercade extends Activity {
    private TextView acerca;
    private int easterEgg;
    public void onCreate(Bundle savedInstanceState) {
        MainActivity.activeActivity=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);
        acerca = (TextView)findViewById(R.id.acerca);
        String texto ="Esta aplicaión ha sido desarrollada por Christian Álvarez Sánchez y Kevin Darío Arboleda Arturo como trabajo de fin de grado. Ha sido dirigido por Gonzalo Méndez y Raquel Hervás. <a href=\"http://tot.fdi.ucm.es/parable/parableWeb/quienessomos.html\">Conócenos</a>";
        acerca.setText(Html.fromHtml(texto));
        acerca.setMovementMethod(LinkMovementMethod.getInstance());
        easterEgg=0;


        acerca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // request your webservice here. Possible use of AsyncTask and ProgressDialog
                // show the result here - dialog or Toast
                easterEgg++;
                if(easterEgg==10) {
                    Toast.makeText(MainActivity.activeActivity, "Has encontrado el Easter Egg!!!", Toast.LENGTH_LONG).show();
                    easterEgg=0;
                    Intent i = new Intent(Acercade.this, Juego.class);
                    startActivity(i);

                }
            }

        });


    }
}
