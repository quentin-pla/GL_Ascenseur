package views.ascenseur;

import views.VueAscenseur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Graphique des boutons intérieurs
 */
public class BoutonsInterieurCanvas extends JPanel implements MouseListener {
    /**
     * Vue
     */
    private VueAscenseur view;

    /**
     * Liste des boutons
     */
    private Map<String,Shape> buttons;

    /**
     * Liste des boutons actifs
     */
    private ArrayList<String> activeButtons;

    /**
     * Constructeur
     * @param view vue
     */
    public BoutonsInterieurCanvas(VueAscenseur view) {
        this.view = view;
        this.buttons = new HashMap<>();
        this.activeButtons = new ArrayList<>();
        addMouseListener(this);
        initButtons();
    }

    /**
     * Initialisation des boutons
     */
    private void initButtons() {
        buttons.put("1",new Ellipse2D.Double(20,20, 40, 40));
        buttons.put("2",new Ellipse2D.Double(70,20, 40, 40));
        buttons.put("3",new Ellipse2D.Double(120,20, 40, 40));
        buttons.put("4",new Ellipse2D.Double(20,70, 40, 40));
        buttons.put("5",new Ellipse2D.Double(70,70, 40, 40));
        buttons.put("6",new Ellipse2D.Double(120,70, 40, 40));
        buttons.put("7",new Ellipse2D.Double(20,120, 40, 40));
        buttons.put("8",new Ellipse2D.Double(70,120, 40, 40));
        buttons.put("9",new Ellipse2D.Double(120,120, 40, 40));
        buttons.put("0",new Ellipse2D.Double(70,170, 40, 40));
        buttons.put("⚠",new Ellipse2D.Double(20,170, 40, 40));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(185, 620);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, 185, 600);
        g2d.setColor(Color.white);
        paintButtons(g2d);
    }

    /**
     * Dessiner les boutons
     * @param g graphique
     */
    private void paintButtons(Graphics2D g) {
        for (Map.Entry<String, Shape> button : buttons.entrySet()) {
            final Rectangle bounds = button.getValue().getBounds();
            if (activeButtons.contains(button.getKey())) {
                g.setColor(Color.RED);
                g.fillOval(bounds.x, bounds.y, 40, 40);
                g.setColor(Color.WHITE);
            }
            g.draw(button.getValue());
            g.drawString(button.getKey(),bounds.x+16,bounds.y+24);
        }
    }

    /**
     * Supprimer un bouton actif
     * @param button bouton
     */
    public void clearActiveButton(String button) {
        activeButtons.remove(button);
        repaint();
    }

    /**
     * Nouvelle saisie utilisateur
     * @param value valeur
     */
    private void newInput(String value) {
        if (!activeButtons.contains(value)) activeButtons.add(value);
        else if (value.equals("⚠")) activeButtons.remove("⚠");
        view.notifyListeners("buttonInput", value);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        for (Map.Entry<String, Shape> button : buttons.entrySet())
            if (button.getValue().contains(p))
                newInput(button.getKey());
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}