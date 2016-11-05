package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.Journals;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.*;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 1/05/2016.
 */
public class JournalActions {
    public static void modifyNames(ArrayList<Journal> journalList, Journals journals) {
        for(Journal journal : journalList){
            String oldName = journal.getName();
            boolean retry = true;
            while(retry){
                String newName = JOptionPane.showInputDialog(getBundle("BusinessActions").getString("NEW_NAME"), oldName.trim());
                try {
                    if(newName!=null && !oldName.trim().equals(newName.trim())){
                        journals.modifyJournalName(oldName, newName);
//                        //ComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_NAME, newName.trim());
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_NAME_EMPTY);
                }
            }
        }
    }

    public static void modifyAbbr(ArrayList<Journal> journalList, Journals journals) {
        for(Journal journal : journalList){
            String oldAbbreviation = journal.getAbbreviation();
            boolean retry = true;
            while(retry){
                String newAbbreviation = JOptionPane.showInputDialog(getBundle("BusinessActions").getString("NEW_ABBR"), oldAbbreviation.trim());
                try {
                    if(newAbbreviation!=null && !oldAbbreviation.trim().equals(newAbbreviation.trim())){
                        journals.modifyJournalAbbreviation(oldAbbreviation, newAbbreviation);
//                        //ComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_ABBR,newAbbreviation.trim());
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_ABBR_EMPTY);
                }
            }
        }
    }

    public static void modifyType(ArrayList<Journal> journalList, JournalTypes journalTypes) {

        boolean singleMove;
        if (journalList.size() == 1) {
            singleMove = true;
        } else {
            int option = JOptionPane.showConfirmDialog(null, getBundle("BusinessActions").getString("APPLY_SAME_TYPE_FOR_ALL_JOURNALS"),
                    getBundle("BusinessActions").getString("ALL"),
                    JOptionPane.YES_NO_OPTION);
            singleMove = (option == JOptionPane.YES_OPTION);
        }
        if (singleMove) {
            Object[] types = journalTypes.getBusinessObjects().toArray();
            int nr = JOptionPane.showOptionDialog(null, getBundle("BusinessActions").getString("CHOOSE_NEW_TYPE"),
                    getBundle("BusinessActions").getString("CHANGE_TYPE"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
            if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                for(Journal journal : journalList) {
                    journal.setType((JournalType) types[nr]);
                }
            }
        } else {
            for(Journal journal : journalList) {
                Object[] types = journalTypes.getBusinessObjects().toArray();
                int nr = JOptionPane.showOptionDialog(null, getBundle("BusinessActions").getString("CHOOSE_NEW_TYPE_FOR")+" " + journal.getName(),
                        getBundle("BusinessActions").getString("CHANGE_TYPE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                        journal.getType());
                if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                    journal.setType((JournalType) types[nr]);
                }
            }
        }
    }

    public static void deleteJournal(ArrayList<Journal> journalList, Journals journals) {
        ArrayList<String> failed = new ArrayList<String>();
        for(Journal journal : journalList) {
            try{
                journals.removeBusinessObject(journal);
            }catch (NotEmptyException e){
                failed.add(journal.getName());
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(null, failed.get(0) + " "+ getBundle("BusinessActions").getString("JOURNAL_NOT_EMPTY"));
            } else {
                StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_JOURNALS_NOT_EMPTY")+"\n");
                for(String s : failed){
                    builder.append("- ").append(s).append("\n");
                }
                JOptionPane.showMessageDialog(null, builder.toString());
            }
        }
    }
}
