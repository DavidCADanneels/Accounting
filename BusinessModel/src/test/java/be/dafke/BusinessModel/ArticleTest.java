package be.dafke.BusinessModel;

import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ArticleTest {

    @Test
    public void test(){
        Article article = new Article("name");
        BigDecimal purchasePriceWithoutVat = new BigDecimal(100).setScale(2);
        article.setPurchasePrice(purchasePriceWithoutVat);

        BigDecimal purchasePriceWithVat = new BigDecimal(106).setScale(2);
        BigDecimal purchasePriceWithoutVatFive = new BigDecimal(500).setScale(2);
        BigDecimal purchasePriceWithVatFive = new BigDecimal(530).setScale(2);
        assertEquals(purchasePriceWithoutVat, article.getPurchasePrice().setScale(2));
        assertEquals(purchasePriceWithVat, article.getPurchasePriceWithVat().setScale(2));
        assertEquals(purchasePriceWithoutVatFive, article.getPurchasePrice(5).setScale(2));
        assertEquals(purchasePriceWithVatFive, article.getPurchasePriceWithVat(5).setScale(2));

        BigDecimal salesPriceWithoutVat = new BigDecimal(200).setScale(2);
        BigDecimal salesPriceWithVat = new BigDecimal(212).setScale(2);
        BigDecimal salesPriceWithoutVatFive = new BigDecimal(1000).setScale(2);
        BigDecimal salesPriceWithVatFive = new BigDecimal(1060).setScale(2);
        assertEquals(salesPriceWithoutVat, article.getSalesPriceWithoutVat(salesPriceWithVat).setScale(2));
        assertEquals(salesPriceWithVat, article.getSalesPriceWithVat(salesPriceWithoutVat).setScale(2));
        assertEquals(salesPriceWithoutVatFive, article.getSalesPriceWithoutVat(salesPriceWithVat, 5).setScale(2));
        assertEquals(salesPriceWithVatFive, article.getSalesPriceWithVat(salesPriceWithoutVat, 5).setScale(2));
    }
}
