package jv.serve_me_clientside;

import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Objects;

public class Narudzba extends Artikl implements Serializable
{
    int kolicina;
    ArrayList<Artikl> prilozi;

    Narudzba ()
    {
        id = 0;
        naziv = "";
        tip = "";
        cijena = 0.0f;
        kolicina = 0;
        prilozi = new ArrayList<Artikl>();
    }

    Narudzba (Artikl artikl, int kolicinaArtikala)
    {
        id = artikl.id;
        naziv = artikl.naziv;
        tip = artikl.tip;
        cijena = artikl.cijena;
        kolicina = kolicinaArtikala;
        prilozi = new ArrayList<Artikl>();
    }

    Narudzba (Artikl artikl, int kolicinaArtikala, ArrayList<Artikl> listaPriloga)
    {
        id = artikl.id;
        naziv = artikl.naziv;
        tip = artikl.tip;
        cijena = artikl.cijena;
        kolicina = kolicinaArtikala;
        prilozi = listaPriloga;
    }

    Narudzba (int idArtikla, String nazivArtikla, String tipArtikla, float cijenaArtikla, int kolicinaArtikala)
    {
        id = idArtikla;
        naziv = nazivArtikla;
        tip = tipArtikla;
        cijena = cijenaArtikla;
        kolicina = kolicinaArtikala;
        prilozi = new ArrayList<Artikl>();

    }

    @Override
    public String toString() {
        return id +
                " " + naziv +
                " " + tip +
                " " + cijena +
                " " + kolicina +
                " " + prilozi.toString();
    }

    Narudzba (int idArtikla, String nazivArtikla, String tipArtikla, float cijenaArtikla, int kolicinaArtikala, ArrayList<Artikl> listaPriloga)
    {
        id = idArtikla;
        naziv = nazivArtikla;
        tip = tipArtikla;
        cijena = cijenaArtikla;
        kolicina = kolicinaArtikala;
        prilozi = listaPriloga;
    }

    public void dodajPrilog(Artikl prilog){
        prilozi.add(prilog);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Narudzba)) return false;
        if (!super.equals(o)) return false;

        Narudzba narudzba = (Narudzba) o;

        if (kolicina != narudzba.kolicina) return false;
        return !(prilozi != null ? !prilozi.equals(narudzba.prilozi) : narudzba.prilozi != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + kolicina;
        result = 31 * result + (prilozi != null ? prilozi.hashCode() : 0);
        return result;
    }

}
