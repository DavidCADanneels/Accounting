package be.dafke.BusinessModel;

        import java.util.function.Predicate;

/**
 * Created by ddanneels on 6/05/2017.
 */
public class AccountNumberPrefixFilter implements Predicate<String>{
    private Account account;

    public AccountNumberPrefixFilter(Account account) {
        this.account = account;
    }

    @Override
    public boolean test(String prefix) {
        return account.getNumber().toString().startsWith(prefix);
    }
}
