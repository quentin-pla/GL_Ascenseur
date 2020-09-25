package models;

import models.commandes.moteur.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Moteur {
    /**
     * Mode de débugage
     */
    private boolean debugMode = false;

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
     * Direction actuelle de l'ascenseur
     */
    private AtomicReference<String> actualDirection;

    /**
     * Arrêt d'urgence
     */
    private AtomicBoolean isEmergencyStopped;

    /**
     * L'ascenseur est ouvert
     */
    private AtomicBoolean isOpen;

    /**
     * Listeners sur le moteur
     */
    private List<PropertyChangeListener> listener = new ArrayList<>();

    /**
     * Constructeur par défaut
     */
    public Moteur(int levels, int defaultLevel) {
        this.levels = levels;
        this.actualLevel = new AtomicInteger(defaultLevel);
        this.nextUpLevels = new PriorityBlockingQueue<>(4);
        this.nextDownLevels = new PriorityBlockingQueue<>(4, Comparator.reverseOrder());
        this.actualDirection = new AtomicReference<>("");
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
        ArretUrgence.getInstance().linkEngine(this);
        Descendre.getInstance().linkEngine(this);
        Monter.getInstance().linkEngine(this);
    }

    /**
     * Récupérer le prochain niveau à s'arrêter
     * @return prochain niveau
     */
    public int getNextLevel() {
        final String finalDirection = actualDirection.get();
        int nextLevel = -1;
        if (finalDirection.equals("UP")) {
            final Integer upHead = getNextUpLevels().peek();
            if (upHead != null) nextLevel = upHead;
        } else if (finalDirection.equals("DOWN")) {
            final Integer downHead = getNextDownLevels().peek();
            if (downHead != null) nextLevel = downHead;
        }
        return nextLevel;
    }

    /**
     * Récupérer la prochaine direction de l'ascenseur
     * @return prochaine direction
     */
    public String getNextDirection() {
        String nextDirection = "";
        if (nextUpLevels.isEmpty() && nextDownLevels.isEmpty())
            nextDirection = "";
        else if (nextUpLevels.isEmpty() || actualLevel.get() >= levels)
            nextDirection = "DOWN";
        else if (nextDownLevels.isEmpty() || actualLevel.get() <= 0)
            nextDirection = "UP";
        else System.out.println("ERROR NEXT DIRECTION");
        return nextDirection;
    }

    /**
     * Ajouter un niveau à satisfaire
     * @param direction direction
     * @param nextLevel prochain niveau
     */
    public void addNextLevel(int nextLevel, String direction) {
        if (!nextUpLevels.contains(nextLevel) && !nextDownLevels.contains(nextLevel)) {
            if (actualDirection.get().equals("")) {
                if (nextLevel > actualLevel.get()) {
                    nextUpLevels.add(nextLevel);
                    setNextStop(nextLevel);
                    setActualDirection("UP");
                } else if (nextLevel < actualLevel.get()) {
                    nextDownLevels.add(nextLevel);
                    setNextStop(nextLevel);
                    setActualDirection("DOWN");
                } else {
                    setNextStop(nextLevel);
                }
            } else {
                if (direction.equals("UP")) {
                    if (actualDirection.get().equals("DOWN")) nextUpLevels.add(nextLevel);
                    else if (actualDirection.get().equals("UP")) {
                        if (actualLevel.get() < nextLevel) nextUpLevels.add(nextLevel);
                        else nextDownLevels.add(nextLevel);
                    }
                }
                else if (direction.equals("DOWN")) {
                    if (actualDirection.get().equals("UP")) nextDownLevels.add(nextLevel);
                    else if (actualDirection.get().equals("DOWN")) {
                        if (actualLevel.get() > nextLevel) nextDownLevels.add(nextLevel);
                        else nextUpLevels.add(nextLevel);
                    }
                }
                else {
                    if (nextLevel < actualLevel.get()) nextDownLevels.add(nextLevel);
                    else if (nextLevel > actualLevel.get()) nextUpLevels.add(nextLevel);
                    else OuverturePortes.getInstance().unlock();
                }
            }
            if (debugMode) System.out.println("up: " + nextUpLevels + " down: " + nextDownLevels);
        }
    }

    /**
     * Lorsque que le moteur s'est arrêté à un niveau demandé
     */
    public void levelPerformed() {
        if (actualDirection.get().equals("UP")) nextUpLevels.remove();
        else if (actualDirection.get().equals("DOWN")) nextDownLevels.remove();
        setActualDirection(getNextDirection());
        if (debugMode) System.out.println("Next direction: " + getActualDirection());
        setNextStop(getNextLevel());
        if (debugMode) System.out.println("Next Stop: " + getNextStop());
    }

    /**
     * Notifier les listeners
     * @param property propriété
     * @param newValue valeur
     */
    private void notifyListeners(String property, String newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, null, newValue));
        }
    }

    /**
     * Ajouter un listener sur le moteur
     * @param newListener listener
     */
    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    /**
     * Incrémenter le niveau actuel
     */
    public void incrementActualLevel() {
        getActualLevel().getAndIncrement();
        notifyListeners("actualLevel",this.actualLevel.get()+"");
    }

    /**
     * Décrémenter le niveau actuel
     */
    public void decrementActualLevel() {
        getActualLevel().getAndDecrement();
        notifyListeners("actualLevel",this.actualLevel.get()+"");
    }

    /*** GETTERS & SETTERS ***/

    public int getLevels() { return levels; }

    public AtomicInteger getActualLevel() { return actualLevel; }

    public AtomicInteger getNextStop() { return nextStop; }

    public void setNextStop(int nextStop) {
        this.nextStop.getAndSet(nextStop);
        notifyListeners("nextStop",this.nextStop.get()+"");
    }

    public PriorityBlockingQueue<Integer> getNextUpLevels() { return nextUpLevels; }

    public PriorityBlockingQueue<Integer> getNextDownLevels() { return nextDownLevels; }

    public AtomicReference<String> getActualDirection() { return actualDirection; }

    public void setActualDirection(String actualDirection) { this.actualDirection.getAndSet(actualDirection); }

    public AtomicBoolean isEmergencyStopped() { return isEmergencyStopped; }

    public void setEmergencyStopped(boolean emergencyStopped) {
        this.isEmergencyStopped.getAndSet(emergencyStopped);
        notifyListeners("isEmergencyStopped",this.isEmergencyStopped.get()+"");
    }

    public AtomicBoolean getIsOpen() { return isOpen; }

    public void setIsOpen(boolean isOpen) {
        this.isOpen.getAndSet(isOpen);
        notifyListeners("isOpen",this.isOpen.get()+"");
    }
}