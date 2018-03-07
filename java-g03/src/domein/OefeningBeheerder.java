package domein;

import com.sun.tools.javah.Gen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.*;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class OefeningBeheerder extends Observable {

    private Oefening oefening;
    private GenericDao<Oefening> oefeningRepo;
    private GenericDao<Bob> bobRepo;
    private FileTransfer fileTransfer;

    private FilteredList<Oefening> oefeningList;

    private final Comparator<Oefening> byOefeningNaam = (o1, o2) -> o1.getNaam().compareToIgnoreCase(o2.getNaam());

    public OefeningBeheerder() {
        setOefeningRepo(new OefeningDaoJpa());
        setBobRepo(new GenericDaoJpa(Bob.class));
        fileTransfer = new FileTransfer();
    }

    public void setBobRepo(GenericDaoJpa mock){
        this.bobRepo = mock;
    }

    public OefeningBeheerder(OefeningDao mock) {
        this.oefeningRepo = mock;
    }

    public void setOefeningRepo(OefeningDao mock) {
        oefeningRepo = mock;
    }


    public Oefening getOefening() {
        return oefening;
    }

    public void setOefening(Oefening oefening) {
        this.oefening = oefening;
        setChanged();
        notifyObservers(oefening);
    }

    private Comparator<Oefening> getByOefeningNaam() {
        return byOefeningNaam;
    }



    public void verwijderOefening() {
        if (controleerOefInBob(oefening.getNaam())) {
            fileTransfer.connect();
            fileTransfer.deleteFile(oefening.getOpgave());
            if (oefening.getFeedback() != null) {
                fileTransfer.deleteFile(oefening.getFeedback());
            }
            fileTransfer.disconnect();
            GenericDaoJpa.startTransaction();
            oefeningRepo.delete(oefening);
            GenericDaoJpa.commitTransaction();
            oefeningList = null;
            getOefeningList();
        }

    }

    /**
     * @param naam
     * @param opgaveFile
     * @param antwoord
     * @param feedbackFile
     * @param groepsbewerkingen
     * @param vak
     */

    public void wijzigOefening(String naam, File opgaveFile, String antwoord, File feedbackFile, List<Groepsbewerking> groepsbewerkingen, Vak vak) {
        if (controleerOefInBob(oefening.getNaam())) {
            GenericDaoJpa.startTransaction();
            oefeningRepo.delete(oefening);
            GenericDaoJpa.commitTransaction();
            createOefening(naam, opgaveFile, antwoord, feedbackFile, groepsbewerkingen, vak);
        }

    }


    public ObservableList<Oefening> geefOefeningen() {
        return new SortedList<>(getOefeningList(), getByOefeningNaam());
    }

    private ObservableList<Oefening> getOefeningList() {
        if (oefeningList == null) {
            oefeningList = new FilteredList<>(FXCollections.observableList(oefeningRepo.findAll()), e -> true);
        }

        return oefeningList;
    }

    private boolean controleerOefInBob(String oefNaam){
        return bobRepo.findAll().stream().filter(bob -> bob.getLijstOefeningen()
                .stream().filter(oef -> oef.getNaam().equals(oefNaam))
                .collect(Collectors.toList()).size() == 0)
                .collect(Collectors.toList()).size() == 0;
    }

    /**
     * @param naam
     * @param opgaveFile
     * @param antwoord
     * @param feedbackFile
     * @param groepsbewerkingen
     * @param vak
     */
    public void createOefening(String naam, File opgaveFile, String antwoord, File feedbackFile, List<Groepsbewerking> groepsbewerkingen, Vak vak) {
        Oefening oef;
        if (feedbackFile == null) {
            oef = new Oefening(naam, "Opgave_" + naam + "_" + opgaveFile.getName(), antwoord, groepsbewerkingen, vak);

        } else {
            oef = new Oefening(naam, "Opgave_" + naam + "_" + opgaveFile.getName(), antwoord, "Feedback_" + naam + "_" + feedbackFile.getName(), groepsbewerkingen, vak);
        }

        if (oefeningRepo.exists(oef.getNaam())) {
            throw new IllegalArgumentException("Oefening met naam: " + naam + " bestaat al");
        } else {
            GenericDaoJpa.startTransaction();
            oefeningRepo.insert(oef);
            GenericDaoJpa.commitTransaction();
            fileTransfer.connect();
            fileTransfer.uploadFile(opgaveFile.getPath(), oef.getOpgave());
            if (oef.getFeedback() != null) {
                fileTransfer.uploadFile(feedbackFile.getPath(), oef.getFeedback());
            }
            fileTransfer.disconnect();
            oefeningList = null;
            getOefeningList();
        }
    }

    /**
     * @param naam
     */
    public Oefening geefOefening(String naam) {
        return oefeningRepo.get(naam);
    }

    /**
     * @param oefeningNaam
     */
    public void changeFilter(String oefeningNaam, List<String> vakken) {
        oefeningList.setPredicate(oefening -> {

            boolean oefeningNaamLeeg = oefeningNaam == null || oefeningNaam.isEmpty();
            boolean vakkenLeeg = vakken == null || vakken.isEmpty();

            if (oefeningNaamLeeg && vakkenLeeg) {
                return true;
            }

            String lowerCaseOefeningNaam = "";

            if (!oefeningNaamLeeg) {
                lowerCaseOefeningNaam = oefeningNaam.toLowerCase();
            }

            boolean conditieOefeningNaam = oefeningNaamLeeg ? false : oefening.getNaam().toLowerCase().contains(lowerCaseOefeningNaam);
            boolean conditieVakken = vakkenLeeg ? false : vakken.stream().anyMatch(t -> t.equals(oefening.getVak().getNaam()));

            if (oefeningNaamLeeg) {
                return conditieVakken;
            } else if (vakkenLeeg) {
                return conditieOefeningNaam;
            }

            return conditieOefeningNaam && conditieVakken;
        });
    }


    /**
     * @param fileName
     */
    public File geefFile(String fileName) {
        fileTransfer.connect();
        File file = fileTransfer.retrieveFile(fileName, fileName);
        fileTransfer.disconnect();
        return file;
    }


}