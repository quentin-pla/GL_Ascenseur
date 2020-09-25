package views;

import views.ascenseur.AscenseurCanvas;
import views.ascenseur.BoutonsExterieurCanvas;
import views.ascenseur.BoutonsInterieurCanvas;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class VueAscenseur extends JFrame {

    /**
     * Liste des écouteurs
     */
    private List<PropertyChangeListener> listener = new ArrayList<>();

    /**
     * Canvas ascenseur
     */
    private AscenseurCanvas ascenseurCanvas;

    /**
     * Canvas boutons intérieur
     */
    private BoutonsInterieurCanvas inButtonsCanvas;

    /**
     * Canvas boutons extérieur
     */
    private BoutonsExterieurCanvas outButtonsCanvas;

    /**
     * Animation en cours
     */
    private boolean animationRunning;

    /**
     * Constructeur
     * @param levels niveaux
     * @param defaultLevel niveau par défaut
     */
    public VueAscenseur(int levels, int defaultLevel) {
        this.ascenseurCanvas = new AscenseurCanvas(this, levels, defaultLevel);
        this.inButtonsCanvas = new BoutonsInterieurCanvas(this);
        this.outButtonsCanvas = new BoutonsExterieurCanvas(this, levels);
        this.animationRunning = false;
        initFrame(levels);
    }

    /**
     * Initialisation de la fenêtre
     * @param levels niveaux
     */
    private void initFrame(int levels) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(600, (levels*100)+170);
        setResizable(false);
        getContentPane().add(inButtonsCanvas, BorderLayout.WEST);
        getContentPane().add(ascenseurCanvas, BorderLayout.CENTER);
        getContentPane().add(outButtonsCanvas, BorderLayout.EAST);
    }

    public void notifyListeners(String property, String newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, null, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    /*** GETTERS & SETTERS ***/

    public AscenseurCanvas getAscenseurCanvas() {
        return ascenseurCanvas;
    }

    public BoutonsInterieurCanvas getInButtonsCanvas() {
        return inButtonsCanvas;
    }

    public BoutonsExterieurCanvas getOutButtonsCanvas() { return outButtonsCanvas; }

    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
        notifyListeners("animationRunning",this.animationRunning+"");
    }
}