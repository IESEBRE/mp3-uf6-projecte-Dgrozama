package org.example.model.entities;

import java.util.Collection;
import java.util.Locale;
import java.util.TreeSet;

public class Usuari {

    private Long id;
    private String usuari;
    private int seguidors;
    private boolean verificat;

    private Collection<Pais> paisCollection;


    public Usuari(){}

    public Usuari(String usuari, int seguidors, boolean verificat, Collection<Pais> pais) {
        this.usuari = usuari;
        this.seguidors = seguidors;
        this.verificat = verificat;
        this.paisCollection= pais;
    }

    public Usuari(Long id, String usuari) {
        this.id = id;
        this.usuari = this.usuari;
    }

    public Usuari(long id, String usuari, int seguidors) {
        this.id = id;
        this.usuari = usuari;
        this.seguidors = seguidors;
    }

    public Usuari(long id, String usuari, int seguidors, boolean verificat ,TreeSet<Pais> pais) {
        this.id = id;
        this.usuari = usuari;
        this.seguidors = seguidors;
        this.verificat = verificat;
        this.paisCollection = pais;
    }

    public Collection<Pais> getPaisCollection() {
        return paisCollection;
    }

    private void setPaisCollection(Collection<Pais> matricules) {
        this.paisCollection = matricules;
    }

    public String getUsuari() {
        return usuari;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuari(String usuari) {
        this.usuari = usuari;
    }

    public int getSeguidors() {
        return seguidors;
    }

    public void setSeguidors(int seguidors) {
        this.seguidors = seguidors;
    }
    public boolean isVerificat() {
        return verificat;
    }

    public void setVerificat(boolean verificat) {
        this.verificat = verificat;
    }

    public static class Pais implements Comparable<Pais>{

        @Override
        public int compareTo(Pais o) {
            return this.paisos.compareTo(o.getPaisos());
        }

        public static enum paisos {
            ID1("Espanya"), ID2("França"), ID3("Portugal"),
            ID4("Estats Units"), ID5("Anglaterra"),
            ID6("Russia"), ID7("Alemanya"), ID8("Rumania"),
            ID9("Itàlia"), ID10("Ucraïna"), ID11("Bulgaria"),
            ID12("Brasil"), ID13("Argentina"), ID14("Mèxic");
            private String nom;

            private paisos(String nom) {
                this.nom = nom;
            }

            public String getNom() {
                return nom;
            }

            @Override
            public String toString() {
                return this.name()+" - " +nom;
            }
        }

        private paisos paisos;
        private int top;

        public Pais(paisos pais, int top) {
            this.paisos = pais;
            this.top = top;
        }

        public Pais.paisos getPaisos() {
            return paisos;
        }

        public void setPaisos(paisos paisos) {
            this.paisos = paisos;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }
    }


}

