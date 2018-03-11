package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class Article extends BusinessObject{
    private String HSCode = "";
    private Integer vatRate = 6;
    private Integer itemsPerUnit = 1;
    private Integer minimumNumberForReduction = 10;
    private BigDecimal purchasePrice = null;
    private BigDecimal salesPriceSingleWithVat = null;
    private BigDecimal salesPricePromoWithVat = null;
    private Contact supplier, customer;

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

    public void setCustomer(Contact customer) {
        this.customer = customer;
    }

    public void setItemsPerUnit(Integer itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    public void setMinimumNumberForReduction(Integer minimumNumberForReduction) {
        this.minimumNumberForReduction = minimumNumberForReduction;
    }

    public void setSalesPriceSingleWithVat(BigDecimal salesPriceSingleWithVat) {
        this.salesPriceSingleWithVat = salesPriceSingleWithVat;
    }

    public void setSalesPricePromoWithVat(BigDecimal salesPricePromoWithVat) {
        this.salesPricePromoWithVat = salesPricePromoWithVat;
    }

    public Integer getItemsPerUnit() {
        return itemsPerUnit;
    }

    public Integer getMinimumNumberForReduction() {
        return minimumNumberForReduction;
    }

    public Contact getSupplier() {
        return supplier;
    }

    public Contact getCustomer() {
        return customer;
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

    public BigDecimal getSalesVat(BigDecimal price){
        BigDecimal factor = getFactor();
        BigDecimal withoutVat = price.divide(factor);
        BigDecimal percentage = getPercentage();
        BigDecimal vatAmount = withoutVat.multiply(percentage);
        return vatAmount;
    }

    public BigDecimal getSalesPriceSingleWithVat() {
        return salesPriceSingleWithVat;
    }

    public BigDecimal getSalesPricePromoWithVat() {
        return salesPricePromoWithVat;
    }

    public BigDecimal getSalesPriceSingleWithVat(int number){
        return multiply(salesPriceSingleWithVat, number);
    }

    public BigDecimal getSalesPricePromoWithVat(int number){
        return multiply(salesPricePromoWithVat, number);
    }

    public BigDecimal getSalesPriceSingleWithoutVat(){
        return salesPriceSingleWithVat.divide(getFactor());
    }

    public BigDecimal getSalesPricePromoWithoutVat(){
        return salesPricePromoWithVat.divide(getFactor());
    }

    public BigDecimal getSalesPriceSingleWithoutVat(int number){
        return multiply(getSalesPriceSingleWithoutVat(), number);
    }

    public BigDecimal getSalesPricePromoWithoutVat(int number){
        return multiply(getSalesPricePromoWithoutVat(), number);
    }

    public static Predicate<Article> ofSupplier(Contact supplier) {
        return article -> article.getSupplier() == supplier;
    }

    public static Predicate<Article> forCustomer(Contact customer) {
        return article -> article.getCustomer() == customer;
    }
}
