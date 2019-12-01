package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.ComponentModel.RefreshableDialog

class DateAndDescriptionDialog extends RefreshableDialog {
    private final DateAndDescriptionPanel dateAndDescriptionPanel
    private static DateAndDescriptionDialog dateAndDescriptionDialog= null

    private DateAndDescriptionDialog() {
        super("Enter Date and Description")
        dateAndDescriptionPanel = new DateAndDescriptionPanel()
        setContentPane(dateAndDescriptionPanel)
        pack()
    }

    Calendar getDate() {
        dateAndDescriptionPanel.getDate()
    }

    String getDescription() {
        dateAndDescriptionPanel.getDescription()
    }

    static DateAndDescriptionDialog getDateAndDescriptionDialog() {
        if(dateAndDescriptionDialog == null){
            dateAndDescriptionDialog = new DateAndDescriptionDialog()
        }
        dateAndDescriptionDialog
    }

    void setDescription(String description) {
        dateAndDescriptionPanel.setDescription(description)
    }

    void setDate(Calendar date) {
        dateAndDescriptionPanel.setDate(date)
    }

    void enableDescription(boolean enabled){
        dateAndDescriptionPanel.enableDescription(enabled)
    }

    void enableDate(boolean enabled){
        dateAndDescriptionPanel.enableDate(enabled)
    }
}