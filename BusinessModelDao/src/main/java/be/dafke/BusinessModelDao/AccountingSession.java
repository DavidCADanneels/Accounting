package be.dafke.BusinessModelDao;

import be.dafke.Accounting.BusinessModel.Journal;

import java.util.HashMap;

public class AccountingSession {

    private Journal activeJournal;
    private HashMap<Journal, JournalSession> journalSessions = new HashMap<>();

    public Journal getActiveJournal() {
        return activeJournal;
    }

    public void setActiveJournal(Journal activeJournal) {
        this.activeJournal = activeJournal;
    }

    public void addJournalSession(Journal journal, JournalSession journalSession){
        journalSessions.put(journal, journalSession);
    }

    public JournalSession getJournalSession(Journal journal){
        return journalSessions.get(journal);
    }
}
