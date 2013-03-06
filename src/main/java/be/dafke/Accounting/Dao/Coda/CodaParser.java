/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.Accounting.Dao.Coda;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.Movement;
import be.dafke.Accounting.Objects.Accounting.Movements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author David C.A. Danneels
 */
public class CodaParser {
	public void parseFile(File[] files, Accounting accounting) {
		CounterParties counterParties = accounting.getCounterParties();
		Movements movements = accounting.getMovements();
		Movement movement = null;
		for(File file : files) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("21")) {
						movement = Movement.parse(line, counterParties);
                        try {
                            movements.addBusinessObject(movement);
                        } catch (EmptyNameException e) {
                            System.err.println("Movement name is empty.");
                        } catch (DuplicateNameException e) {
                            System.err.println("Movement name already exist.");
                        }
                    } else if (line.startsWith("22")) {
                        if(movement!=null){
						    movement.addPart2(line);
                        } else {
                            System.err.println("Corrupt CODA file: " + file.getName());
                        }
					} else if (line.startsWith("23")) {
                        if(movement!=null){
						    movement.addPart3(line);
                        } else {
                            System.err.println("Corrupt CODA file: " + file.getName());
                        }
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException io) {
                io.printStackTrace();
			}
		}
	}

	public static Calendar convertDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		Calendar cal = Calendar.getInstance();
		try {
			Date datum = sdf.parse(date);
			cal.setTime(datum);
		} catch (ParseException p) {
            p.printStackTrace();
		}
		return cal;
	}

	public static BigDecimal convertBigDecimal(String amountString) {
		BigInteger amountNoDec = new BigInteger(amountString);
		BigDecimal amount = new BigDecimal(amountNoDec, 3);
		amount = amount.setScale(2);
		return amount;
	}
}