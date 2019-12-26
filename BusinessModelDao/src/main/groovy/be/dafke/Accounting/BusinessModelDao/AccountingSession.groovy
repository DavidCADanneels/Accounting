package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Journal

class AccountingSession {

    boolean showNumbers
    Journal activeJournal
    HashMap<Journal, JournalSession> journalSessions = new HashMap<>()

    void addJournalSession(Journal journal, JournalSession journalSession){
        journalSessions.put(journal, journalSession)
    }

    JournalSession getJournalSession(Journal journal){
        journalSessions.get(journal)
    }

    boolean getShowNumbers() {
        return showNumbers
    }

    void setShowNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers
    }
}
