package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.VATTransactions;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.BoxLayout.Y_AXIS;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATGUI extends JFrame {
    private static final HashMap<VATTransactions, VATGUI> vatGuis = new HashMap<>();

    public static VATGUI getInstance(VATTransactions vatTransactions) {
        VATGUI gui = vatGuis.get(vatTransactions);
        if(gui==null){
            gui = new VATGUI(vatTransactions);
            vatGuis.put(vatTransactions,gui);
            SaveAllActionListener.addFrame(gui);
        }
        return gui;
    }
    
    private JTextField vat1, vat2, vat3, vat81, vat82, vat83, vat54, vat59, vat49, vat64, vat85, vat63, vatXX, vatYY, vat71, vat72;

    public VATGUI(VATTransactions vatTransactions) {
        super(getBundle("VAT").getString("VAT_OVERVIEW"));

        vat1 = new JTextField(10);
        vat2 = new JTextField(10);
        vat3 = new JTextField(10);
        vat81 = new JTextField(10);
        vat82 = new JTextField(10);
        vat83 = new JTextField(10);
        vat54 = new JTextField(10);
        vat59 = new JTextField(10);
        vat49 = new JTextField(10);
        vat64 = new JTextField(10);
        vat85 = new JTextField(10);
        vat63 = new JTextField(10);
        vatXX = new JTextField(10);
        vatYY = new JTextField(10);
        vat71 = new JTextField(10);
        vat72 = new JTextField(10);
        
        JPanel left = createSalesPanel();
        JPanel right = createPurchasePanel();
        JPanel totals = createTotalsPanel();

        setContentPane(createContentPanel(left,right,totals));
        pack();
    }

    private JPanel createFieldPanel(String title, JTextField jTextField){
        JPanel panel = new JPanel();
        panel.add(new JLabel(title));
        panel.add(jTextField);
        return panel;
    }

    private JPanel createContentPanel(JPanel left, JPanel right, JPanel totals){
        JPanel content = new JPanel();

        JPanel center = new JPanel();
        center.add(left);
        center.add(right);

        content.setLayout(new BoxLayout(content,Y_AXIS));
        content.add(center);
        content.add(totals);
        return content;
    }

    private JPanel createSalesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(new JLabel("Sales"));
        panel.add(createFieldPanel("1", vat1));
        panel.add(createFieldPanel("2", vat2));
        panel.add(createFieldPanel("3", vat3));
        panel.add(createFieldPanel("54", vat54));
        panel.add(createFieldPanel("49", vat49));
        panel.add(createFieldPanel("64", vat64));
        return panel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Purchases"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("81", vat81));
        panel.add(createFieldPanel("82", vat82));
        panel.add(createFieldPanel("83", vat83));
        panel.add(createFieldPanel("59", vat59));
        panel.add(createFieldPanel("85", vat85));
        panel.add(createFieldPanel("63", vat63));
        return panel;
    }

    private JPanel createTotalsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Totals"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("XX", vatXX));
        panel.add(createFieldPanel("YY", vatYY));
        panel.add(createFieldPanel("71", vat71));
        panel.add(createFieldPanel("72", vat72));
        return panel;
    }
}
