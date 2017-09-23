package com.parable.WPS;

/**
 * Created by Altair on 15/11/15.
 */
public class Coordenada {
    private String mac;
    private double x;
    private double y;
    private double z;
    private int nivel;

    private double distancia;

    public Coordenada() {
        x = 0;
        y = 0;
        z = 0;
        mac = "";
        nivel = 0;
        //distancia=Double.MAX_VALUE;
    }

    public Coordenada(String mac, double x, double y, double z, int nivel) {
        this.mac = mac;
        this.x = x;
        this.y = y;
        this.z = z;
        this.nivel = nivel;

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getDistancia() {
        return distancia;
    }

    public boolean equals(Coordenada o) {
        Coordenada c = (Coordenada) o;
        if (this.x == c.getX() && y == c.getY() && z == c.getZ()) {
            return true;
        } else {
            return false;
        }
    }

    public Coordenada clone() {
        Coordenada c = new Coordenada();
        c.setDistancia(distancia);
        c.setMac(new String(mac));
        c.setNivel(nivel);
        c.setX(x);
        c.setY(y);
        c.setZ(z);
        return c;
    }


}
