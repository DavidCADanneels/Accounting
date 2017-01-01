package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.VATFields;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.BoxLayout.Y_AXIS;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATGUI extends JFrame {
    private static final HashMap<VATFields, VATGUI> vatGuis = new HashMap<>();

    public static VATGUI getInstance(VATFields vatFields) {
        VATGUI gui = vatGuis.get(vatFields);
        if(gui==null){
            gui = new VATGUI(vatFields);
            vatGuis.put(vatFields,gui);
            SaveAllActionListener.addFrame(gui);
        }
        return gui;
    }
    
    private VATFields vatFields;

    public VATGUI(VATFields vatFields) {
        super(getBundle("VAT").getString("VAT_OVERVIEW"));
        this.vatFields = vatFields;
        JPanel left = createSalesPanel();
        JPanel right = createPurchasePanel();
        JPanel totals = createTotalsPanel();

        setContentPane(createContentPanel(left,right,totals));
        pack();
    }

    private JPanel createFieldPanel(String nr, String description){
        JPanel panel = new JPanel();
        panel.add(new JLabel(nr));
        JTextField textField = new JTextField(10);
        BigDecimal amount = vatFields.getField(nr);
        if(amount!=null) {
            textField.setText(amount.toString());
        }
        panel.add(textField);
        textField.setToolTipText(description);
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
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Sales"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(new JLabel("Sales"));
        panel.add(createFieldPanel("1", "6%"));
        panel.add(createFieldPanel("2", "12%"));
        panel.add(createFieldPanel("3", "21%"));
        panel.add(createFieldPanel("54", "tax"));
        panel.add(new JLabel("Sale-CNs"));
        panel.add(createFieldPanel("49", "net"));
        panel.add(createFieldPanel("64", "tax"));
        return panel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Purchases"));
        panel.add(new JLabel("Purchases"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("81", "supplies"));
        panel.add(createFieldPanel("82", "services"));
        panel.add(createFieldPanel("83", "investments"));
        panel.add(createFieldPanel("59", "tax on 81,82,83"));
        panel.add(new JLabel("Purchase-CNs"));
        panel.add(createFieldPanel("85", "net"));
        panel.add(createFieldPanel("63", "tax"));
        return panel;
    }

    private JPanel createTotalsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Totals"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("XX", "54+63"));
        panel.add(createFieldPanel("YY", "59+64"));
        panel.add(createFieldPanel("71", "XX-YY"));
        panel.add(createFieldPanel("72", "YY-XX"));
        return panel;
    }
}
