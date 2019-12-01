package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.*

import java.awt.*
import java.text.MessageFormat

import static java.util.ResourceBundle.getBundle

class ActionUtils {
    public static final String ACCOUNTING_DUPLICATE_NAME = "ACCOUNTING_DUPLICATE_NAME"
    public static final String ACCOUNTING_NAME_EMPTY = "ACCOUNTING_NAME_EMPTY"

    public static final String ACCOUNT_DUPLICATE_NAME = "ACCOUNT_DUPLICATE_NAME"
    public static final String ACCOUNT_NAME_EMPTY = "ACCOUNT_NAME_EMPTY"

    public static final String ARTICLE_DUPLICATE_NAME = "ARTICLE_DUPLICATE_NAME"
    public static final String ARTICLE_NAME_EMPTY = "ARTICLE_NAME_EMPTY"

    public static final String INGREDIENT_DUPLICATE_NAME = "INGREDIENT_DUPLICATE_NAME"
    public static final String INGREDIENT_NAME_EMPTY = "INGREDIENT_NAME_EMPTY"

    public static final String PROJECT_DUPLICATE_NAME = "PROJECT_DUPLICATE_NAME"
    public static final String PROJECT_NAME_EMPTY = "PROJECT_NAME_EMPTY"

    public static final String JOURNAL_DUPLICATE_NAME = "JOURNAL_DUPLICATE_NAME"
    public static final String JOURNAL_NAME_EMPTY = "JOURNAL_NAME_EMPTY"
    public static final String JOURNAL_DUPLICATE_ABBR = "JOURNAL_DUPLICATE_ABBR"
    public static final String JOURNAL_ABBR_EMPTY = "JOURNAL_ABBR_EMPTY"
    public static final String JOURNAL_DUPLICATE_NAME_AND_OR_ABBR = "JOURNAL_DUPLICATE_NAME_AND_OR_ABBR"
    public static final String JOURNAL_NAME_ABBR_EMPTY = "JOURNAL_NAME_ABBR_EMPTY"

    public static final String JOURNAL_TYPE_DUPLICATE_NAME = "JOURNAL_TYPE_DUPLICATE_NAME"
    public static final String JOURNAL_TYPE_NAME_EMPTY = "JOURNAL_TYPE_NAME_EMPTY"

    public static final String COUNTERPARTY_DUPLICATE_NAME = "COUNTERPARTY_DUPLICATE_NAME"
    public static final String COUNTERPARTY_NAME_EMPTY = "COUNTERPARTY_NAME_EMPTY"

    public static final String MORTGAGE_DUPLICATE_NAME = "MORTGAGE_DUPLICATE_NAME"
    public static final String MORTGAGE_NAME_EMPTY = "MORTGAGE_NAME_EMPTY"

    public static final String TRANSACTION_MOVED = "TRANSACTION_MOVED"
    public static final String TRANSACTION_REMOVED = "TRANSACTION_REMOVED"

    public static final String INVALID_INPUT = "INVALID_INPUT"
    public static final String FILL_IN_DATE = "FILL_IN_DATE"
    public static final String SELECT_JOURNAL_FIRST = "SELECT_JOURNAL_FIRST"
    public static final String SELECT_ACCOUNT_FIRST = "SELECT_ACCOUNT_FIRST"
    public static final String ACCOUNT_NOT_EMPTY = "ACCOUNT_NOT_EMPTY"
    public static final String CHOOSE_NEW_TYPE_FOR = "CHOOSE_NEW_TYPE_FOR"
    public static final String CHOOSE_NEW_TYPE = "CHOOSE_NEW_TYPE"
    public static final String CHANGE_TYPE = "CHANGE_TYPE"

    static void showErrorMessage(Component component, String message){
        JOptionPane.showMessageDialog(component, getBundle("BusinessActions").getString(message))
    }

    static void showErrorMessage(Component component, String message, Object ... messageArguments){
        MessageFormat formatter = new MessageFormat(getBundle("BusinessActions").getString(message))
        JOptionPane.showMessageDialog(component, formatter.format(messageArguments))
    }

    static String getFormattedString(String message, Object ... messageArguments){
        MessageFormat formatter = new MessageFormat(getBundle("BusinessActions").getString(message))
        formatter.format(messageArguments)
    }

//    static int showOptionDialog(String message, String title, Object ... messageArguments){
//        MessageFormat messageFormat = new MessageFormat(getBundle("BusinessActions").getString(message))
//        MessageFormat titleFormat = new MessageFormat(getBundle("BusinessActions").getString(title))
//        JOptionPane.showOptionDialog(null, messageFormat.format(messageArguments), titleFormat, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,)
//    }
}
