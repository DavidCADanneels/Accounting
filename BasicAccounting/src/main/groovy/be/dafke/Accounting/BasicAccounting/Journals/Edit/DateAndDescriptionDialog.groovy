package be.dafke.Accounting.BasicAccounting.Journals.Edit


import be.dafke.ComponentModel.RefreshableDialog

class DateAndDescriptionDialog extends RefreshableDialog {
    final DateAndDescriptionPanel dateAndDescriptionPanel
    static DateAndDescriptionDialog dateAndDescriptionDialog= null

    DateAndDescriptionDialog() {
        super("Enter Date and Description")
        dateAndDescriptionPanel = new DateAndDescriptionPanel()
        setContentPane(dateAndDescriptionPanel)
        pack()
    }

    Calendar getDate() {
        dateAndDescriptionPanel.date
    }

    String getDescription() {
        dateAndDescriptionPanel.description
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