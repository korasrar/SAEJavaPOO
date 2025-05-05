package fr.saejava;
public class Classification {
    int idDewey;
    String nomClass;

    Classification(int idDewey,String nomClass){
        this.idDewey=idDewey;
        this.nomClass=nomClass;
    }

    public int getIdDewey() {
        return idDewey;
    }

    public String getNomClass() {
        return nomClass;
    }

    public void setIdDewey(int idDewey) {
        this.idDewey = idDewey;
    }

    public void setNomClass(String nomClass) {
        this.nomClass = nomClass;
    }
    @Override
    public String toString() {
        return this.idDewey+" "+this.nomClass;
    }
}
