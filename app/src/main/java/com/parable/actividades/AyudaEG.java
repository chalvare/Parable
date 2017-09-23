package com.parable.actividades;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.parable.parablesdb.R;

/**
 * Created by Altair on 16/2/16.
 */
public class AyudaEG extends Activity {
    private TextView ayuda;
    private int easterEgg;
    public void onCreate(Bundle savedInstanceState) {
        MainActivity.activeActivity=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayudaeg);
        ayuda = (TextView)findViewById(R.id.ayuda);


    }
}
