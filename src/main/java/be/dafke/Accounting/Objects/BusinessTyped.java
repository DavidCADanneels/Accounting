package be.dafke.Accounting.Objects;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 7:39
 */
public interface BusinessTyped<T extends BusinessType> {

    public void setType(T type);

    public T getType();

}
