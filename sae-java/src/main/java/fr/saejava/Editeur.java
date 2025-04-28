package fr.saejava;

import java.util.ArrayList;
import java.util.List;

public class Editeur{
    int idEdit;
    String nomEdit;
    List<Livre> livres;

    Editeur(int idEdit,String nomEdit){
        this.idEdit=idEdit;
        this.nomEdit=nomEdit;
        this.livres = new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.idEdit+" "+this.nomEdit;
    }

    public int getIdEdit() {
        return idEdit;
    }

    public void setIdEdit(int idEdit) {
        this.idEdit = idEdit;
    }

    public String getNomEdit() {
        return nomEdit;
    }

    public void setNomEdit(String nomEdit) {
        this.nomEdit = nomEdit;
    }
}