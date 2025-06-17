package fr.saejava.model;

import java.util.ArrayList;
import java.util.List;

public class Editeur{
    int idEdit;
    String nomEdit;
    List<Livre> livres;

    public Editeur(int idEdit,String nomEdit){
        this.idEdit=idEdit;
        this.nomEdit=nomEdit;
        this.livres = new ArrayList<>();
    }

    public int getIdEdit() {
        return idEdit;
    }

    public String getNomEdit() {
        return nomEdit;
    }

    public void setIdEdit(int idEdit) {
        this.idEdit = idEdit;
    }

    public void setNomEdit(String nomEdit) {
        this.nomEdit = nomEdit;
    }

    @Override
    public String toString() {
        return this.idEdit+" "+this.nomEdit;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj==null){return false;}
        if(obj==this){return true;}
        if(!(obj instanceof Editeur)){return false;}
        Editeur editeur=(Editeur) obj;
        return this.idEdit==editeur.getIdEdit();
    } 

    @Override
    public int hashCode() {
        return Integer.hashCode(idEdit);
    }
            
}