package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.VATFields;

import javax.swing.*;
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

    private JPanel createFieldPanel(String nr){
        JPanel panel = new JPanel();
        panel.add(new JLabel(nr));
        JTextField textField = new JTextField(10);
        BigDecimal amount = vatFields.getField(nr);
        if(amount!=null) {
            textField.setText(amount.toString());
        }
        panel.add(textField);
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
        panel.add(createFieldPanel("1"));
        panel.add(createFieldPanel("2"));
        panel.add(createFieldPanel("3"));
        panel.add(createFieldPanel("54"));
        panel.add(createFieldPanel("49"));
        panel.add(createFieldPanel("64"));
        return panel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Purchases"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("81"));
        panel.add(createFieldPanel("82"));
        panel.add(createFieldPanel("83"));
        panel.add(createFieldPanel("59"));
        panel.add(createFieldPanel("85"));
        panel.add(createFieldPanel("63"));
        return panel;
    }

    private JPanel createTotalsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Totals"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("XX"));
        panel.add(createFieldPanel("YY"));
        panel.add(createFieldPanel("71"));
        panel.add(createFieldPanel("72"));
        return panel;
    }
}
