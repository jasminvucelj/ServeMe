package jv.serve_me_clientside;

import java.io.Serializable;

public class Artikl implements Serializable
{
    int id;
    String naziv;
    String tip;
    float cijena;

    Artikl ()
    {
        id = 0;
        naziv = "";
        tip = "";
        cijena = 0.0f;
    }

    Artikl (int idArtikla, String nazivArtikla, String tipArtikla, float cijenaArtikla)
    {
        id = idArtikla;
        naziv = nazivArtikla;
        tip = tipArtikla;
        cijena = cijenaArtikla;
    }

    @Override
    public String toString() {
        return id +
                " " + naziv +
                " " + tip +
                " " + cijena;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artikl artikl = (Artikl) o;

        if (id != artikl.id) return false;
        if (Float.compare(artikl.cijena, cijena) != 0) return false;
        if (!naziv.equals(artikl.naziv)) return false;
        return tip.equals(artikl.tip);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + naziv.hashCode();
        result = 31 * result + tip.hashCode();
        result = 31 * result + (cijena != +0.0f ? Float.floatToIntBits(cijena) : 0);
        return result;
    }
}
