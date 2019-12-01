package be.dafke.ComponentModel

import javax.swing.JDialog

abstract class RefreshableDialog extends JDialog {
    RefreshableDialog(String title){
        setTitle(title)
        setModal(true)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE)
    }
}