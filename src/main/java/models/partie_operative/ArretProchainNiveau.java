package models.partie_operative;

import models.MoteurTraction;

/**
 * Commande moteur pour gérer les arrêts aux prochains niveaux
 */
public class ArretProchainNiveau extends CommandeSysteme {
    /**
     * Constructeur
     * @param engine moteur
     */
    public ArretProchainNiveau(MoteurTraction engine) {
        super(engine);
        argsCount = 2;
        //Premier argument : niveau
        //Deuxième argument : direction
    }

    @Override
    public void run() {
        if (checkArgLevel(args[0])) {
            final int nextLevel = Integer.parseInt(args[0]);
            String direction = args[1];
            final String actualDirection = getEngine().getActualDirection().get();
            final int actualLevel = getEngine().getActualLevel().get();
            String nextAdd = "";
            if (actualDirection.equals("STATIC") || actualDirection.equals("")) direction = "";
            switch (direction) {
                case "UP":
                    switch (actualDirection) {
                        case "UP": nextAdd = actualLevel < nextLevel ? "UP" : "DOWN"; break;
                        case "DOWN": nextAdd = actualLevel > nextLevel ? "UP" : "DOWN"; break;
                        default: break;
                    }
                    break;
                case "DOWN":
                    switch (actualDirection) {
                        case "UP": nextAdd = actualLevel < nextLevel ? "DOWN" : "UP"; break;
                        case "DOWN": nextAdd = actualLevel > nextLevel ? "DOWN" : "UP"; break;
                        default: break;
                    }
                    break;
                case "":
                    if (nextLevel != actualLevel || getEngine().isMoving()) {
                        int compare = Integer.compare(nextLevel, actualLevel);
                        if (compare != 0) nextAdd = compare < 0 ? "DOWN" : "UP";
                        else nextAdd = actualDirection.equals("UP") ? "DOWN" : "UP";
                    }
                    break;
                default:
                    break;
            }
            boolean move = false;
            switch (nextAdd) {
                case "UP":
                    if (!getEngine().getNextUpLevels().contains(nextLevel)) {
                        getEngine().getNextUpLevels().add(nextLevel);
                        if (actualDirection.equals("")) move = true;
                    }
                    break;
                case "DOWN":
                    if (!getEngine().getNextDownLevels().contains(nextLevel)) {
                        getEngine().getNextDownLevels().add(nextLevel);
                        if (actualDirection.equals("")) move = true;
                    }
                    break;
                case "":
                    getEngine().executeOuverturePortes();
                    break;
                default:
                    break;
            }
            if (move) getEngine().moveToNextLevel();
        }
    }
}
