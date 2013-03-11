package be.dafke.Accounting.Objects;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 15:07
 */
public interface BusinessTypeProvider<T extends BusinessType> {
    public BusinessTypeCollection<T> getBusinessTypeCollection();

    public void setBusinessTypeCollection(BusinessTypeCollection<T> businessTypeCollection);
}
