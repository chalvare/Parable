package com.parable.socialNetwork;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by Altair on 18/2/16.
 */
public class SocialNetwork {

    private String texto;

    public SocialNetwork(String texto){
        this.texto=texto;
    }


    public Uri twitter(){
        String tweetUrl = "https://twitter.com/intent/tweet?text="
                + texto+ "&hashtags=ParableTFG";
        Uri uri = Uri.parse(tweetUrl);
        return uri;
    }

    public Intent correo(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Parable's Tale");
        intent.putExtra(Intent.EXTRA_TEXT, "Enviar a un amigo: " + texto);
        return intent;
    }


}
