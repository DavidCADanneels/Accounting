package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class Article extends BusinessObject{
    private String HSCode = "";
    private Integer vatRate = 6;
    private BigDecimal purchasePrice = null;
    private Contact supplier;

    public Article(String name){
        setName(name);
    }

    public void setHSCode(String HSCode) {
        this.HSCode = HSCode;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSupplier(Contact supplier) {
        this.supplier = supplier;
    }

    public Contact getSupplier() {
        return supplier;
    }

    public BigDecimal getPurchasePrice(){
        return purchasePrice;
    }

    public String getHSCode() {
        return HSCode;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public BigDecimal getPurchasePrice(int number){
        return multiply(purchasePrice, number);
    }

    private BigDecimal multiply(BigDecimal source, int number){
        return source.multiply(new BigDecimal(number));
    }

    private BigDecimal divide(BigDecimal source, int number){
        return source.divide(new BigDecimal(number));
    }

    // return 0.06
    private BigDecimal getPercentage(){
        return divide(new BigDecimal(vatRate),100);
    }

    // return 1.06
    private BigDecimal getFactor(){
        return BigDecimal.ONE.add(getPercentage());
    }

    public BigDecimal getPurchasePriceWithVat(){
        return purchasePrice.multiply(getFactor());
    }

    public BigDecimal getPurchasePriceWithVat(int number){
        return multiply(getPurchasePriceWithVat(), number);
    }

    public BigDecimal getProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice);
    }

    public BigDecimal getProfit(BigDecimal salesprice, int number){
        return multiply(getProfit(salesprice), number);
    }

    public BigDecimal getPurchaseVat(){
        return purchasePrice.multiply(getPercentage());
    }

    public BigDecimal getPurchaseVat(int number){
        return multiply(getPurchaseVat(), number);
    }

    public BigDecimal getSalesPriceWithVat(BigDecimal salesPriceWithoutVat){
        return salesPriceWithoutVat.multiply(getFactor());
    }

    public BigDecimal getSalesPriceWithoutVat(BigDecimal salesPriceWithVat){
        return salesPriceWithVat.divide(getFactor());
    }

    public BigDecimal getSalesPriceWithVat(BigDecimal salesPriceWithoutVat, int number){
        return multiply(getSalesPriceWithVat(salesPriceWithoutVat), number);
    }

    public BigDecimal getSalesPriceWithoutVat(BigDecimal salesPriceWithVat, int number){
        return multiply(getSalesPriceWithoutVat(salesPriceWithVat), number);
    }

    public static Predicate<Article> ofSupplier(Contact supplier) {
        return article -> article.getSupplier() == supplier;
    }
}
