package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MortgagePanel extends JPanel implements ActionListener {
    private final JList<Mortgage> mortgagesList;
    private final JButton create;
    private final JTextField nrPayed;
    private Mortgages mortgages;
    private Accounts accounts;
    private boolean init = true;
    private final JComboBox<Account> comboIntrest, comboCapital;
    private Mortgage selectedMortgage = null;
    private final MortgageDataModel model;
    private DefaultListModel<Mortgage> listModel;
    private DefaultComboBoxModel<Account> intrestModel, capitalModel;

    private final JTable table;
    private final JButton save, delete;

    public MortgagePanel(Mortgages mortgages, Accounts accounts) {
        this.mortgages = mortgages;
        this.accounts = accounts;
        mortgagesList = new JList<>();
        mortgagesList.setModel(new DefaultListModel<>());
        mortgagesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && mortgagesList.getSelectedIndex() != -1) {
                selectedMortgage = mortgagesList.getSelectedValue();
            } else {
                selectedMortgage = null;
            }
            select();
        });
        create = new JButton("Create new Mortgage table");
        create.addActionListener(e -> {
            MortgageCalculatorGUI mortgageCalculatorGUI = MortgageCalculatorGUI.showCalculator(mortgages);
            mortgageCalculatorGUI.setLocation(getLocationOnScreen());
            mortgageCalculatorGUI.setVisible(true);
        });

        JPanel left = new JPanel(new BorderLayout());
        left.add(mortgagesList, BorderLayout.CENTER);
        left.add(create, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(left, BorderLayout.WEST);

        model = new MortgageDataModel(selectedMortgage);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(600, 200));
        JScrollPane scroll = new JScrollPane(table);

        comboIntrest = new JComboBox<>();
        comboIntrest.addActionListener(this);
        comboCapital = new JComboBox<>();
        comboCapital.addActionListener(this);
        nrPayed = new JTextField(4);
        nrPayed.addActionListener(this);
        save = new JButton("Save");
        save.addActionListener(this);
        delete = new JButton("Delete");
        delete.addActionListener(this);

        activateButtons(false);
        save.setEnabled(false);

        JPanel accountsPanel = new JPanel();
        accountsPanel.setLayout(new GridLayout(2,0));
        accountsPanel.add(new JLabel("Intrest Account:"));
        accountsPanel.add(comboIntrest);
        accountsPanel.add(new JLabel("Capital Account:"));
        accountsPanel.add(comboCapital);
        //
        JPanel block3a = new JPanel();
        block3a.add(new JLabel("Already payed:"));
        block3a.add(nrPayed);
        //
        JPanel block3b = new JPanel();
        block3b.add(save);
        block3b.add(delete);
        //
        JPanel block3 = new JPanel();
        block3.setLayout(new BoxLayout(block3, BoxLayout.Y_AXIS));
        block3.add(block3a);
        block3.add(block3b);
        //
        JPanel north = new JPanel();
        north.add(accountsPanel);
        north.add(block3);

        JPanel right = new JPanel(new BorderLayout());
        right.add(scroll, BorderLayout.CENTER);
        right.add(north, BorderLayout.NORTH);

        add(right, BorderLayout.CENTER);



    }

    private void activateButtons(boolean active) {
        comboCapital.setEnabled(active);
        comboIntrest.setEnabled(active);
        nrPayed.setEnabled(active);
        delete.setEnabled(active);
        if (active) {
            save.setText("Save");
        } else {
            save.setText("Edit");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            if (save.getText().equals("Edit")) {
                activateButtons(true);
            } else {
                if (selectedMortgage != null) {
                    int nr = Utils.parseInt(nrPayed.getText());
                    selectedMortgage.setPayed(nr);
                    Main.fireMortgageEditedPayButton(selectedMortgage);
                }
                activateButtons(false);
            }
        } else if (e.getSource() == delete) {
            if (selectedMortgage != null) {
                try {
                    mortgages.removeBusinessObject(selectedMortgage);
                    Main.fireMortgageAddedOrRemoved(mortgages);
                } catch (NotEmptyException e1) {
                    System.err.println("This mortgage is in use !");
                    e1.printStackTrace();
                }
            }
        } else if (!init) {
            if (e.getSource() == comboIntrest) {
                Account intrestAccount = (Account) comboIntrest.getSelectedItem();
                if (selectedMortgage != null && intrestAccount != null) {
                    selectedMortgage.setIntrestAccount(intrestAccount);
                }
            } else if (e.getSource() == comboCapital) {
                Account capitalAccount = (Account) comboCapital.getSelectedItem();
                if (selectedMortgage != null && capitalAccount != null) {
                    selectedMortgage.setCapitalAccount(capitalAccount);
                }
            } else if (e.getSource() == nrPayed) {
                if (selectedMortgage != null) {
                    int nr = Utils.parseInt(nrPayed.getText());
                    selectedMortgage.setPayed(nr);
                }
            }
            Main.fireMortgageEditedPayButton(selectedMortgage);
        }
    }

    public void refresh() {
        listModel = new DefaultListModel<>();
        for(Mortgage mortgage :mortgages.getBusinessObjects()) {
            if (!listModel.contains(mortgage)) {
                listModel.addElement(mortgage);
            }
        }
        mortgagesList.setModel(listModel);
        mortgagesList.revalidate();

        intrestModel = new DefaultComboBoxModel<>();
        capitalModel = new DefaultComboBoxModel<>();
        for(Account account : accounts.getBusinessObjects()){
            intrestModel.addElement(account);
            capitalModel.addElement(account);
        }
        comboCapital.setModel(capitalModel);
        comboIntrest.setModel(intrestModel);
        comboCapital.revalidate();
        comboIntrest.revalidate();
    }

    public void reselect(Mortgage mortgage) {
        if (mortgage == selectedMortgage) {
            select();
        }
    }
    private void select() {
        init = true;
        save.setEnabled(selectedMortgage != null);
        nrPayed.setText(selectedMortgage==null?"":selectedMortgage.getNrPayed() + "");
        if (selectedMortgage == null) {
            activateButtons(false);
            comboIntrest.setSelectedIndex(-1);
            comboCapital.setSelectedIndex(-1);
        } else {
            comboIntrest.setSelectedItem(selectedMortgage.getIntrestAccount());
            comboCapital.setSelectedItem(selectedMortgage.getCapitalAccount());
        }
        model.revalidate(selectedMortgage);
        table.revalidate();
        init = false;
    }
}
