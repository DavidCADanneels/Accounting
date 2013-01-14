package be.dafke.Accounting.Objects.Mortgage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * @author David C.A. Danneels
 */
public class Main {
	static int aantalMaanden = 48;
	static BigDecimal jaarPercentage = new BigDecimal(4.25);// 4.73
	static int aantalPerJaar = 12;
	static BigDecimal startKapitaal = new BigDecimal(12450); // 12575

	public static void main(String[] args) {
		ArrayList<BigDecimal> bedragen = new ArrayList<BigDecimal>();
		bedragen.add(new BigDecimal(135000));
		bedragen.add(new BigDecimal(140000));
		bedragen.add(new BigDecimal(145000));
		bedragen.add(new BigDecimal(150000));
		bedragen.add(new BigDecimal(155000));
		bedragen.add(new BigDecimal(160000));
		bedragen.add(new BigDecimal(165000));
		bedragen.add(new BigDecimal(170000));
		bedragen.add(new BigDecimal(175000));
		bedragen.add(new BigDecimal(180000));
		bedragen.add(new BigDecimal(185000));
		bedragen.add(new BigDecimal(190000));
		bedragen.add(new BigDecimal(195000));
		bedragen.add(new BigDecimal(200000));
		bedragen.add(new BigDecimal(205000));
		bedragen.add(new BigDecimal(210000));
		bedragen.add(new BigDecimal(205000));
		bedragen.add(new BigDecimal(210000));
		bedragen.add(new BigDecimal(215000));
		bedragen.add(new BigDecimal(220000));
		bedragen.add(new BigDecimal(225000));
		bedragen.add(new BigDecimal(230000));
		bedragen.add(new BigDecimal(235000));
		bedragen.add(new BigDecimal(240000));
		bedragen.add(new BigDecimal(245000));
		bedragen.add(new BigDecimal(250000));
//		bedragen.add(new BigDecimal(380000));
//		bedragen.add(new BigDecimal(385000));
//		bedragen.add(new BigDecimal(390000));
//		bedragen.add(new BigDecimal(395000));
//		bedragen.add(new BigDecimal(400000));

		ArrayList<BigDecimal> percentages = new ArrayList<BigDecimal>();
		percentages.add(new BigDecimal(1));
		percentages.add(new BigDecimal(1.25));
		percentages.add(new BigDecimal(1.5));
		percentages.add(new BigDecimal(1.75));
		percentages.add(new BigDecimal(2));
		percentages.add(new BigDecimal(2.25));
		percentages.add(new BigDecimal(2.5));
		percentages.add(new BigDecimal(2.75));
		percentages.add(new BigDecimal(3));
		percentages.add(new BigDecimal(3.25));
		percentages.add(new BigDecimal(3.5));
		percentages.add(new BigDecimal(3.75));
		percentages.add(new BigDecimal(4));
		percentages.add(new BigDecimal(4.25));
		percentages.add(new BigDecimal(4.5));
		percentages.add(new BigDecimal(4.75));
		percentages.add(new BigDecimal(5));

		ArrayList<Integer> aantalJaar = new ArrayList<Integer>();
		aantalJaar.add(Integer.valueOf(20));
		aantalJaar.add(Integer.valueOf(21));
		aantalJaar.add(Integer.valueOf(22));
		aantalJaar.add(Integer.valueOf(23));
		aantalJaar.add(Integer.valueOf(24));
		aantalJaar.add(Integer.valueOf(25));
		aantalJaar.add(Integer.valueOf(26));
		aantalJaar.add(Integer.valueOf(27));
		aantalJaar.add(Integer.valueOf(28));
		aantalJaar.add(Integer.valueOf(29));
		aantalJaar.add(Integer.valueOf(30));

		for(Integer jaren : aantalJaar) {
			System.out.println(jaren + " jaar");
			System.out.println("-------");
			for(BigDecimal kapitaal : bedragen) {
				System.out.print(kapitaal);
				for(BigDecimal jaarPercent : percentages) {
					BigDecimal maandPercent = Calculate.berekenMaandPercentage(jaarPercent, 12);
					BigDecimal mens = Calculate.berekenMensualiteit(kapitaal, maandPercent, jaren * 12);
					System.out.print("\t" + mens.setScale(2, RoundingMode.HALF_UP));
				}
				System.out.println();
			}
		}
//		Calculate berekenen = new Calculate(startKapitaal, jaarPercentage, aantalMaanden, aantalPerJaar);
//		ArrayList<Vector<BigDecimal>> aflossingsTabel = berekenen.getAflossingsTabel();
	}
}
