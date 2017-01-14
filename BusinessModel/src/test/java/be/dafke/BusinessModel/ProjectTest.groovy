package be.dafke.BusinessModel

/**
 * Created by ddanneels on 10/01/2017.
 */
class ProjectTest extends GroovyTestCase {

    public static final String ACCOUNT_NAME = "account_name"
    public static final String PROJECT_NAME = "project_name"

    void testGetChildType() {

    }

    void testCreateNewChild() {

    }

    void testGetOutputProperties() {

    }

    void testBusinessObjects() {
        def accountTypes = new AccountTypes();
        def project = new Project(PROJECT_NAME, new Accounts(accountTypes), accountTypes);
        def account = new Account(ACCOUNT_NAME);
        project.addBusinessObject(account);
        assert account == project.getBusinessObject(ACCOUNT_NAME);
        assert project.getBusinessObjects().contains(account);
    }

    void testGetJournal() {

    }

    void testGetResultBalance() {

    }

    void testGetRelationsBalance() {

    }

}
