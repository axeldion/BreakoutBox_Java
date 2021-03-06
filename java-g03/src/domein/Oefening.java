package domein;

import javax.persistence.*;
import java.util.*;

@Entity

public class Oefening {


    @Id
    private String naam;
    private String opgave;
    private String feedback;
    private String antwoord;
    private int tijdsLimiet;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Groepsbewerking> lijstGroepsbewerkingen;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Vak vak;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Doelstellingscode> doelstellingscodes;

    public String getNaam() {
        return this.naam;
    }

    public void setNaam(String naam) {
        if (naam == null || naam.equals("")) {
            throw new IllegalArgumentException("Naam mag niet leeg zijn.");
        }
        this.naam = naam;
    }

    public String getOpgave() {
        return this.opgave;
    }

    public void setOpgave(String opgave) {

        if (opgave == null || opgave.equals("")) {
            throw new IllegalArgumentException("Opgave mag niet leeg gelaten worden");
        }
        this.opgave = opgave;
    }

    public void setVak(Vak vak) {
        if (vak == null) {
            throw new IllegalArgumentException("Vak moet ingevuld zijn");
        }
        this.vak = vak;
    }

    public Vak getVak() {
        return vak;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getTijdsLimiet() {
        return tijdsLimiet;
    }

    public void setTijdsLimiet(int tijdsLimiet) {
        this.tijdsLimiet = tijdsLimiet;
    }

    public List<Groepsbewerking> getLijstGroepsbewerkingen() {
        return lijstGroepsbewerkingen;
    }


    public void setLijstGroepsbewerkingen(List<Groepsbewerking> lijstGroepsbewerkingen) {
        if (lijstGroepsbewerkingen == null || lijstGroepsbewerkingen.isEmpty()) {
            throw new IllegalArgumentException("Er werden geen Groepsbewerkingen toegevoegd");
        }
        this.lijstGroepsbewerkingen = lijstGroepsbewerkingen;
    }

    public String getAntwoord() {
        return antwoord;
    }

    public void setAntwoord(String antwoord) {
        if (antwoord == null || antwoord.isEmpty()){
            throw new IllegalArgumentException("Antwoord mag niet leeg gelaten worden.");
        }
        this.antwoord = antwoord;
    }

    public List<Doelstellingscode> getDoelstellingscodes() {
        return doelstellingscodes;
    }

    public void setDoelstellingscodes(List<Doelstellingscode> doelstellingscodes) {
        this.doelstellingscodes = doelstellingscodes;
    }

    protected Oefening() {
    }


    /**
     * @param naam
     * @param opgave
     * @param feedback
     * @param vak
     */
    public Oefening(String naam, String opgave, String antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen, List<Doelstellingscode> doelstellingen,Vak vak, int tijdsLimiet) {
        setNaam(naam);
        setOpgave(opgave);
        setAntwoord(antwoord);
        setFeedback(feedback);
        setLijstGroepsbewerkingen(groepsbewerkingen);
        setDoelstellingscodes(doelstellingen);
        setVak(vak);
        setTijdsLimiet(tijdsLimiet);
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oefening)) return false;
        Oefening oefening = (Oefening) o;
        return Objects.equals(getNaam(), oefening.getNaam());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNaam());
    }

    @Override
    public String toString() {
        return naam.toLowerCase();
    }


}