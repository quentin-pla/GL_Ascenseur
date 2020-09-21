package models;

import models.commandes.moteur.ArretProchainNiveau;
import models.commandes.moteur.Descendre;
import models.commandes.moteur.Monter;
import models.commandes.moteur.OuverturePortes;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Moteur {
    /**
     * Nombre de niveaux (constant)
     */
    private final int levels;

    /**
     * Niveau actuel
     */
    private AtomicInteger actualLevel;

    /**
     * Prochain arrêt de l'ascenseur
     */
    private AtomicInteger nextStop;

    /**
     * Liste des niveaux à se déplacer pendant la montée
     */
    private volatile PriorityBlockingQueue<Integer> nextUpLevels;

    /**
     * Liste des niveaux à se déplacer pendant la descente
     */
    private volatile PriorityBlockingQueue<Integer> nextDownLevels;

    /**
     * Direction de l'ascenseur
     */
    public enum Direction {
        UP, DOWN, NONE
    }

    /**
     * Direction actuelle de l'ascenseur
     */
    private AtomicReference<Direction> actualDirection;

    /**
     * Arrêt d'urgence
     */
    private AtomicBoolean isEmergencyStopped;

    /**
     * L'ascenseur est ouvert
     */
    private AtomicBoolean isOpen;

    /**
     * Constructeur par défaut
     */
    public Moteur(int levels, int defaultLevel) {
        this.levels = levels;
        this.actualLevel = new AtomicInteger(defaultLevel);
        this.nextUpLevels = new PriorityBlockingQueue<>(4);
        this.nextDownLevels = new PriorityBlockingQueue<>(4, Comparator.reverseOrder());
        this.actualDirection = new AtomicReference<>(Direction.NONE);
        this.isEmergencyStopped = new AtomicBoolean(false);
        this.isOpen = new AtomicBoolean(false);
        this.nextStop = new AtomicInteger(-1);
        initCommands();
    }

    /**
     * Relie les commandes système au moteur
     */
    private void initCommands() {
        ArretProchainNiveau.getInstance().linkEngine(this);
        OuverturePortes.getInstance().linkEngine(this);
        //ArretUrgence.getInstance().linkEngine(this);
        Descendre.getInstance().linkEngine(this);
        Monter.getInstance().linkEngine(this);
    }

    /**
     * Récupérer le prochain niveau à s'arrêter
     * @return prochain niveau
     */
    public int getNextLevel() {
        final Direction finalDirection = actualDirection.get();
        int nextLevel = -1;
        if (finalDirection == Direction.UP) {
            final Integer upHead = getNextUpLevels().peek();
            if (upHead != null) nextLevel = upHead;
        } else if (finalDirection == Direction.DOWN) {
            final Integer downHead = getNextDownLevels().peek();
            if (downHead != null) nextLevel = downHead;
        }
        return nextLevel;
    }

    /**
     * Récupérer la prochaine direction de l'ascenseur
     * @return prochaine direction
     */
    public Direction getNextDirection() {
        Direction nextDirection = null;
        if (nextUpLevels.isEmpty() && nextDownLevels.isEmpty())
            nextDirection = Direction.NONE;
        else if (nextUpLevels.isEmpty() || actualLevel.get() >= levels)
            nextDirection = Direction.DOWN;
        else if (nextDownLevels.isEmpty() || actualLevel.get() <= 0)
            nextDirection = Direction.UP;
        return nextDirection;
    }

    /**
     * Ajouter un niveau à s'arrêter
     * @param direction direction
     * @param nextLevel prochain niveau
     */
    public void addNextLevel(int nextLevel, Direction direction) {
        if (actualDirection.get() == Direction.NONE) {
            if (nextLevel > actualLevel.get()) {
                nextUpLevels.add(nextLevel);
                setNextStop(nextLevel);
                setActualDirection(Direction.UP);
            } else if (nextLevel < actualLevel.get()) {
                nextDownLevels.add(nextLevel);
                setNextStop(nextLevel);
                setActualDirection(Direction.DOWN);
            } else {
                setIsOpen(true);
            }
        } else {
            if (direction == Direction.UP) nextUpLevels.add(nextLevel);
            else if (direction == Direction.DOWN) nextDownLevels.add(nextLevel);
            else {
                if (nextLevel < actualLevel.get()) nextDownLevels.add(nextLevel);
                else if (nextLevel > actualLevel.get()) nextUpLevels.add(nextLevel);
                else setIsOpen(true);
            }
        }
        System.out.println("up: " + nextUpLevels + " down: " + nextDownLevels);
    }

    /**
     * Lorsque que le moteur s'est arrêté à un niveau demandé
     */
    public void levelPerformed() {
        if (actualDirection.get() == Direction.UP) nextUpLevels.remove();
        else nextDownLevels.remove();
        setIsOpen(true);
        setActualDirection(getNextDirection());
        setNextStop(getNextLevel());
    }

    /**
     * Retourne la hauteur en mètres à partir d'un niveau
     * @param level niveau
     * @return hauteur en mètres
     */
    private double getHeightFromLevel(int level) {
        //La cabine de l'ascenseur fait 2 mètres de haut,
        //entre chaque niveau il y a une marge de 40cm
        return 2.4*level;
    }

    /*** GETTERS & SETTERS ***/

    public int getLevels() { return levels; }

    public AtomicInteger getActualLevel() { return actualLevel; }

    public AtomicInteger getNextStop() { return nextStop; }

    public void setNextStop(int nextStop) { this.nextStop.getAndSet(nextStop); }

    public double getActualHeight() { return getHeightFromLevel(actualLevel.get()); }

    public PriorityBlockingQueue<Integer> getNextUpLevels() { return nextUpLevels; }

    public PriorityBlockingQueue<Integer> getNextDownLevels() { return nextDownLevels; }

    public AtomicReference<Direction> getActualDirection() { return actualDirection; }

    public void setActualDirection(Direction actualDirection) { this.actualDirection.getAndSet(actualDirection); }

    public AtomicBoolean isEmergencyStopped() { return isEmergencyStopped; }

    public void setEmergencyStopped(boolean emergencyStopped) { isEmergencyStopped.getAndSet(emergencyStopped); }

    public AtomicBoolean getIsOpen() { return isOpen; }

    public void setIsOpen(boolean isOpen) { this.isOpen.getAndSet(isOpen); }
}
