package be.dafke.BusinessModel;

        import java.util.function.Predicate;

/**
 * Created by ddanneels on 6/05/2017.
 */
public class AccountNamePrefixFilter implements Predicate<String>{
    private Account account;

    public AccountNamePrefixFilter(Account account) {
        this.account = account;
    }

    @Override
    public boolean test(String prefix) {
        return account.getName().startsWith(prefix);
    }
}
