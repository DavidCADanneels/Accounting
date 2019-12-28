package be.dafke.Accounting.BasicAccounting.Journals.Management

import be.dafke.Accounting.BusinessModel.AccountsList

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

import static be.dafke.Accounting.BusinessModel.AccountsList.DEBIT
import static be.dafke.Accounting.BusinessModel.AccountsList.CREDIT

class ButtonConfigPanel extends JPanel{

    final JTextField leftButtonLabel
    final JTextField rightButtonLabel
    final JCheckBox leftButton
    final JCheckBox rightButton
    final JComboBox<String> leftActions
    final JComboBox<String> rightActions
    AccountsList accountsList

    ButtonConfigPanel() {
        setLayout(new GridLayout(0,1))

        leftButtonLabel = new JTextField()
        rightButtonLabel = new JTextField()

        leftButton = new JCheckBox("Left Button")
        rightButton = new JCheckBox("Right Button")

        leftActions = new JComboBox<>()
        rightActions = new JComboBox<>()

        leftActions.addItem(DEBIT)
        leftActions.addItem(CREDIT)

        rightActions.addItem(CREDIT)
        rightActions.addItem(DEBIT)

        initialize()

        leftActions.addActionListener({ e -> updateLeftAction() })
        rightActions.addActionListener({ e -> updateRightAction() })
        leftButtonLabel.addFocusListener(new FocusAdapter() {
            @Override
            void focusLost(FocusEvent e) {
                String newName = leftButtonLabel.getText()
                accountsList.setLeftButton(newName)
            }
        })
        rightButtonLabel.addFocusListener(new FocusAdapter() {
            @Override
            void focusLost(FocusEvent e) {
                String newName = rightButtonLabel.getText()
                accountsList.setRightButton(newName)
            }
        })

        JPanel leftPanel = new JPanel(new BorderLayout())
        JPanel rightPanel = new JPanel(new BorderLayout())

        JPanel leftCenter = new JPanel(new GridLayout(0,2))
        JPanel rightCenter = new JPanel(new GridLayout(0,2))

        leftCenter.add(new JLabel("Action:"))
        leftCenter.add(leftActions)
        leftCenter.add(new JLabel("Label:"))
        leftCenter.add(leftButtonLabel)

        rightCenter.add(new JLabel("Action:"))
        rightCenter.add(rightActions)
        rightCenter.add(new JLabel("Label:"))
        rightCenter.add(rightButtonLabel)

        leftPanel.add(leftButton, BorderLayout.NORTH)
        leftPanel.add(leftCenter, BorderLayout.SOUTH)

        rightPanel.add(rightButton, BorderLayout.NORTH)
        rightPanel.add(rightCenter, BorderLayout.SOUTH)

        leftPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Left Button"))
        rightPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Right Button"))

        add(leftPanel)
        add(rightPanel)
    }

    void initialize() {
        leftButton.selected = true
        rightButton.selected = true

        if(accountsList==null){
            leftActions.setSelectedItem(DEBIT)
            rightActions.setSelectedItem(CREDIT)
            leftButtonLabel.setText(DEBIT)
            rightButtonLabel.setText(CREDIT)
        } else {
            leftActions.setSelectedItem(accountsList.isLeftAction() ? DEBIT : CREDIT)
            rightActions.setSelectedItem(accountsList.isRightAction() ? DEBIT : CREDIT)
            leftButtonLabel.setText(accountsList.getLeftButton())
            rightButtonLabel.setText(accountsList.getRightButton())
        }
    }

    void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList
        initialize()
    }

    void updateRightAction() {
        String selectedItem = (String)rightActions.selectedItem
        accountsList.setRightAction(DEBIT.equals(selectedItem))
    }

    void updateLeftAction() {
        String selectedItem = (String)leftActions.selectedItem
        accountsList.setLeftAction(DEBIT.equals(selectedItem))
    }
}
