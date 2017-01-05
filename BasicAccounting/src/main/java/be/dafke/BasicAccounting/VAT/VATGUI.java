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
    public static final String CREATE_FILE = "Create file";

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
        JPanel buttonPanel = createButtonPanel();

        setContentPane(createContentPanel(left,right,totals, buttonPanel));
        pack();
    }

    private JPanel createFieldPanel(String nr, String description){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel line1 = new JPanel();

        line1.add(new JLabel(nr));
        JTextField textField = new JTextField(10);
        BigDecimal amount = vatFields.getField(nr);
        if(amount!=null) {
            textField.setText(amount.toString());
        }
        line1.add(textField);
        textField.setToolTipText(description);

        JPanel line2 = new JPanel();
        line2.add(new JLabel(description));

        panel.add(line2);
        panel.add(line1);
        return panel;
    }

    private JPanel createContentPanel(JPanel left, JPanel right, JPanel totals, JPanel buttonsPanel){
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content,Y_AXIS));
        content.add(left);
        content.add(right);
        content.add(totals);
        content.add(buttonsPanel);
        return content;
    }

    private JPanel createSalesPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Sales"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));

        JPanel left = createSalesMainPanel();
        JPanel right = createSalesTaxAndCNPanel();

        panel.add(left);
        panel.add(right);

        return panel;
    }

    private JPanel createSalesMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.add(createFieldPanel("0", "0%"));
        panel.add(createFieldPanel("1", "6%"));
        panel.add(createFieldPanel("2", "12%"));
        panel.add(createFieldPanel("3", "21%"));
        return panel;
    }

    private JPanel createSalesTaxAndCNPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("54", "tax"));
        panel.add(new JLabel("Credit notes"));
        panel.add(createFieldPanel("49", "net"));
        panel.add(createFieldPanel("64", "tax"));
        return panel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Purchases"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));

        JPanel left = createPurchaseMainPanel();
        JPanel right = createPurchaseTaxAndCNPanel();

        panel.add(left);
        panel.add(right);

        return panel;
    }

    private JPanel createPurchaseMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("81", "supplies"));
        panel.add(createFieldPanel("82", "services"));
        panel.add(createFieldPanel("83", "investments"));
        return panel;
    }

    private JPanel createPurchaseTaxAndCNPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("59", "tax on 81,82,83"));
        panel.add(new JLabel("Credit notes"));
        panel.add(createFieldPanel("85", "net"));
        panel.add(createFieldPanel("63", "tax"));
        return panel;
    }

    private JPanel createTotalsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Totals"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JPanel left = createTotalsLeftPanel();
        JPanel right = createTotalsRightPanel();

        panel.add(left);
        panel.add(right);

        return panel;
    }

    private JPanel createTotalsLeftPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("XX", "54+63"));
        panel.add(createFieldPanel("YY", "59+64"));
        return panel;
    }

    private JPanel createTotalsRightPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("71", "XX-YY"));
        panel.add(createFieldPanel("72", "YY-XX"));
        return panel;
    }

    private JPanel createButtonPanel(){
        JPanel panel = new JPanel();
        JButton button = new JButton(CREATE_FILE);
        button.addActionListener(e -> createFile());
        panel.add(button);
        return panel;
    }

    private void createFile() {

    }
}
