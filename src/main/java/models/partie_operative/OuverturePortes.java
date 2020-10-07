package models.partie_operative;

import models.MoteurTraction;

import static java.lang.Thread.sleep;

/**
 * Commande moteur pour gérer l'ouverture des portes
 */
public class OuverturePortes extends CommandeSysteme {
    /**
     * Constructeur
     * @param engine moteur
     */
    public OuverturePortes(MoteurTraction engine) {
        super(engine);
    }

    @Override
    public void run() {
        final String direction = getEngine().getActualDirection().get();
        getEngine().setActualDirection("STATIC");
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEngine().setIsOpen(true);
        if (!getEngine().isDebugMode()) System.out.println("<- Ouverture ->");
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEngine().setIsOpen(false);
        if (!getEngine().isDebugMode()) System.out.println("-> Fermeture <-");
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEngine().setActualDirection(direction);
        getEngine().levelPerformed();
    }
}
