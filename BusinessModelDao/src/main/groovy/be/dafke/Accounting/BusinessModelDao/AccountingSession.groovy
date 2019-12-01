package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Journal

class AccountingSession {

    private Journal activeJournal
    private HashMap<Journal, JournalSession> journalSessions = new HashMap<>()

    Journal getActiveJournal() {
        activeJournal
    }

    void setActiveJournal(Journal activeJournal) {
        this.activeJournal = activeJournal
    }

    void addJournalSession(Journal journal, JournalSession journalSession){
        journalSessions.put(journal, journalSession)
    }

    JournalSession getJournalSession(Journal journal){
        journalSessions.get(journal)
    }
}
