package models.commandes.moteur;

import models.Moteur;

import static java.lang.Thread.sleep;

/**
 * Commande moteur pour gérer l'ouverture des portes
 */
public class OuverturePortes extends CommandeMoteur {
    /**
     * Constructeur
     * @param engine moteur
     */
    public OuverturePortes(Moteur engine) {
        super(engine);
    }

    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEngine().setIsOpen(true);
        System.out.println("<- Ouverture ->");
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEngine().setIsOpen(false);
        System.out.println("-> Fermeture <-");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEngine().levelPerformed();
    }
}
