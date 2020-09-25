package views.ascenseur;

import views.VueAscenseur;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class AscenseurCanvas extends JPanel {
    /**
     * Vue
     */
    private VueAscenseur view;

    /**
     * Nombre de niveaux
     */
    private int levels;

    /**
     * Hauteur entre chaque niveau
     */
    private int levelGap;

    /**
     * Position verticale de l'ascenseur
     */
    private int ascenseurYPosition;

    /**
     * Prochaine position de l'ascenseur
     */
    private int nextPosition;

    /**
     * Direction de l'ascenseur
     */
    private String direction;

    /**
     * Ascenseur ouvert ?
     */
    private boolean isOpen;

    /**
     * Ascenseur arrêté d'urgence ?
     */
    private boolean isEmergencyStopped;

    /**
     * Timer gérant l'animation
     */
    private Timer timer;

    /**
     * Animation de l'ascenseur
     */
    private TimerTask animation;

    /**
     * Départ positionnement horizontal
     */
    private int hStart;

    /**
     * Départ positionnement vertical
     */
    private int vStart;

    /**
     * Constructeur
     * @param view vue
     * @param levels niveaux
     * @param defaultLevel niveau par défaut
     */
    public AscenseurCanvas(VueAscenseur view, int levels, int defaultLevel) {
        this.view = view;
        this.levels = levels;
        this.levelGap = 100;
        this.ascenseurYPosition = getAscenseurYPosition(defaultLevel);
        this.nextPosition = -1;
        this.direction = "";
        this.isOpen = false;
        this.isEmergencyStopped = false;
        this.hStart = 90;
        this.vStart = 100;
    }

    /**
     * Initialisation de l'animation
     */
    private void initAnimation() {
        timer = new Timer();
        animation = new TimerTask() {
            @Override
            public void run() {
                if (!isEmergencyStopped) {
                    if (direction.equals("UP") && ascenseurYPosition > nextPosition) {
                        --ascenseurYPosition;
                        if (ascenseurYPosition <= nextPosition) {
                            view.setAnimationRunning(false);
                            timer.cancel();
                        }
                    } else if (direction.equals("DOWN") && ascenseurYPosition < nextPosition) {
                        ++ascenseurYPosition;
                        if (ascenseurYPosition >= nextPosition) {
                            view.setAnimationRunning(false);
                            timer.cancel();
                        }
                    } else {
                        view.setAnimationRunning(false);
                        timer.cancel();
                    }
                    repaint();
                }
            }
        };
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, levels*100+150);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 300, levels*100+150);
        g.setColor(Color.BLACK);
        paintLevels(g);
        paintAscenseur(g);
    }

    /**
     * Obtenir la position de l'ascenseur en fonction d'un niveau
     * @param level niveau
     * @return position de l'ascenseur
     */
    private int getAscenseurYPosition(int level) {
        return (levels * levelGap + 50) - (levelGap * level);
    }

    /**
     * Peindre les niveaux
     * @param g graphique
     */
    private void paintLevels(Graphics g) {
        for (int i = 0; i <= levels; i++) {
            int yPosition = (levels*100+vStart) - (levelGap * i);
            g.drawString("Niveau " + i,hStart-70,yPosition-20);
            g.drawLine(0, yPosition, 300, yPosition);
        }
    }

    /**
     * Peindre l'ascenseur
     * @param g graphique
     */
    private void paintAscenseur(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(hStart+10,ascenseurYPosition,50,50);
        g.setColor(Color.BLACK);
        g.drawRect(hStart+10,ascenseurYPosition,50,50);
        if (!isOpen) g.drawLine(hStart+35, ascenseurYPosition, hStart+35, ascenseurYPosition + 50);
        else g.fillRect(hStart+20,ascenseurYPosition,30,50);
    }

    /**
     * Animer l'ascenseur lors du changement de niveau
     * @param level niveau à atteindre
     */
    public void animateMove(int level) {
        if (!isOpen && level >= 0 && level <= levels) {
            nextPosition = getAscenseurYPosition(level);
            direction = (nextPosition == ascenseurYPosition) ? "" : (nextPosition < ascenseurYPosition) ? "UP" : "DOWN";
            view.setAnimationRunning(true);
            initAnimation();
            timer.schedule(animation, 0, 10);
        }
    }

    /*** GETTERS & SETTERS ***/

    public void setOpen(boolean open) {
        isOpen = open;
        repaint();
    }

    public void setEmergencyStopped(boolean value) {
        isEmergencyStopped = value;
    }
}