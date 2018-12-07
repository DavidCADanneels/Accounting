package be.dafke.BasicAccounting.Deliveroo;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.DeliverooMeals;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class MealsGUI extends JFrame {
    private final MealsPanel mealsPanel;

    private static MealsGUI mealsGui = null;

    private MealsGUI(DeliverooMeals deliverooMeals) {
        super(getBundle("Accounting").getString("MEALS"));
        mealsPanel = new MealsPanel(deliverooMeals);
        setContentPane(mealsPanel);
        pack();
    }

    public static MealsGUI showMeals(DeliverooMeals deliverooMeals) {
        if (mealsGui == null) {
            mealsGui = new MealsGUI(deliverooMeals);
            Main.addFrame(mealsGui);
        }
        return mealsGui;
    }

    public static void fireMealUsageUpdatedForAll(){
        if (mealsGui!=null){
            mealsGui.fireMealUsageUpdated();
        }
    }

    public void fireMealUsageUpdated(){
        mealsPanel.fireMealUsageUpdated();
    }
}