package fr.saejava;
public class Editeur{
    int idEdit;
    String nomEdit;

    Editeur(int idEdit,String nomEdit){
        this.idEdit=idEdit;
        this.nomEdit=nomEdit;
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