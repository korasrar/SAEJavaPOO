package fr.saejava.model;
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

    @Override
    public boolean equals(Object obj){
        if(obj==null){return false;}
        if(obj==this){return true;}
        if(!(obj instanceof Classification)){return false;}
        Classification classification=(Classification) obj;
        return this.idDewey==classification.getIdDewey();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idDewey);
    }
}
