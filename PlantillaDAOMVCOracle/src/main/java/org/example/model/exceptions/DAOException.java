package org.example.model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class DAOException extends Exception{

    private static final Map<Integer, String> missatges = new HashMap<>();
    //num i retorna string, el map
    static {
        missatges.put(1, "Ha petat la base de dades!!");
        missatges.put(2, "Falta omplir alguna dada o hi ha informació no vàlida!!");
        missatges.put(3, "Aquest nom d'usuari ja està en ús. Si us plau, tria'n un altre.");
        missatges.put(4, "Has d'introduir un número de seguidors correcte (>=0!!)");
        missatges.put(5, "Has d'introduir uns ingréssos correctes (>=0.0€!!)");
        missatges.put(6, "Has d'introduir un nom d'usuari amb més de 4 caràcters i no ha de ser repetit!!");
        missatges.put(7, "Per borrar una fila l'has de seleccionar a la taula");
        missatges.put(8, "Per modificar una fila l'has de seleccionar a la taula");
        missatges.put(9, "Has d'introduir un top local vàlid (>0!!)");
        missatges.put(10, "Hi ha hagut algun problema a l'intentar guardar les dades");
        missatges.put(11, "No s'ha pogut guardar l'usuari a la base de dades.");
        missatges.put(12, "No s'ha pogut actualitzar l'usuari a la base de dades.");
        missatges.put(13, "No s'ha pogut esborrar l'usuari de la base de dades.");
        missatges.put(14, "No es pot repetir el nom d'usuari.");

    }

    //atribut
    private int tipo;

    //constructor al q pasem tipo
    public DAOException(int tipo){
        this.tipo=tipo;
    }

    //sobreescrivim el get message
        @Override
    public String getMessage(){
        return missatges.get(this.tipo); //el missatge del tipo
    }

    public int getTipo() {
        return tipo;
    }
}
