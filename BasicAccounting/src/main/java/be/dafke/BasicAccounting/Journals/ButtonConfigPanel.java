package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.AccountsList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static be.dafke.BusinessModel.AccountsList.DEBIT;
import static be.dafke.BusinessModel.AccountsList.CREDIT;

public class ButtonConfigPanel extends JPanel{

    private final JTextField leftButtonLabel;
    private final JTextField rightButtonLabel;
    private final JCheckBox leftButton;
    private final JCheckBox rightButton;
    private final JComboBox<String> leftActions;
    private final JComboBox<String> rightActions;
    private AccountsList accountsList;

    public ButtonConfigPanel() {
        setLayout(new GridLayout(0,1));

        leftButtonLabel = new JTextField(DEBIT);
        rightButtonLabel = new JTextField(CREDIT);

        leftButton = new JCheckBox("Left Button");
        rightButton = new JCheckBox("Right Button");

        leftButton.setSelected(true);
        rightButton.setSelected(true);

        leftActions = new JComboBox<>();
        rightActions = new JComboBox<>();

        leftActions.addItem(DEBIT);
        leftActions.addItem(CREDIT);

        rightActions.addItem(CREDIT);
        rightActions.addItem(DEBIT);

        leftActions.setSelectedItem(DEBIT);
        rightActions.setSelectedItem(CREDIT);

        leftActions.addActionListener(e -> updateLeftAction());
        rightActions.addActionListener(e -> updateRightAction());
        leftButtonLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String newName = leftButtonLabel.getText();
                accountsList.setLeftButton(newName);
            }
        });
        rightButtonLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String newName = rightButtonLabel.getText();
                accountsList.setRightButton(newName);
            }
        });

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel leftCenter = new JPanel(new GridLayout(0,2));
        JPanel rightCenter = new JPanel(new GridLayout(0,2));

        leftCenter.add(new JLabel("Action:"));
        leftCenter.add(leftActions);
        leftCenter.add(new JLabel("Label:"));
        leftCenter.add(leftButtonLabel);

        rightCenter.add(new JLabel("Action:"));
        rightCenter.add(rightActions);
        rightCenter.add(new JLabel("Label:"));
        rightCenter.add(rightButtonLabel);

        leftPanel.add(leftButton, BorderLayout.NORTH);
        leftPanel.add(leftCenter, BorderLayout.SOUTH);

        rightPanel.add(rightButton, BorderLayout.NORTH);
        rightPanel.add(rightCenter, BorderLayout.SOUTH);

        leftPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Left Button"));
        rightPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Right Button"));

        add(leftPanel);
        add(rightPanel);
    }

    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
    }

    private void updateRightAction() {
        String selectedItem = (String)rightActions.getSelectedItem();
        accountsList.setRightAction(DEBIT.equals(selectedItem));
    }

    private void updateLeftAction() {
        String selectedItem = (String)leftActions.getSelectedItem();
        accountsList.setLeftAction(DEBIT.equals(selectedItem));
    }
}
