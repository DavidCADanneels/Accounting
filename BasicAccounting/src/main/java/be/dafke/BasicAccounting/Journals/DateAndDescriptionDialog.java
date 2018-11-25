package be.dafke.BasicAccounting.Journals;

import be.dafke.ComponentModel.RefreshableDialog;

import java.util.Calendar;

public class DateAndDescriptionDialog extends RefreshableDialog {
    private final DateAndDescriptionPanel dateAndDescriptionPanel;
    private static DateAndDescriptionDialog dateAndDescriptionDialog= null;

    private DateAndDescriptionDialog() {
        super("Enter Date and Description");
        dateAndDescriptionPanel = new DateAndDescriptionPanel();
        setContentPane(dateAndDescriptionPanel);
        pack();
    }

    public Calendar getDate() {
        return dateAndDescriptionPanel.getDate();
    }

    public String getDescription() {
        return dateAndDescriptionPanel.getDescription();
    }

    public static DateAndDescriptionDialog getDateAndDescriptionDialog() {
        if(dateAndDescriptionDialog == null){
            dateAndDescriptionDialog = new DateAndDescriptionDialog();
        }
        return dateAndDescriptionDialog;
    }

    public void setDescription(String description) {
        dateAndDescriptionPanel.setDescription(description);
    }

    public void setDate(Calendar date) {
        dateAndDescriptionPanel.setDate(date);
    }

    public void enableDescription(boolean enabled){
        dateAndDescriptionPanel.enableDescription(enabled);
    }

    public void enableDate(boolean enabled){
        dateAndDescriptionPanel.enableDate(enabled);
    }
}
