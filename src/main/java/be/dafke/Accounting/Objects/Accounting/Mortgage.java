package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.BusinessCollection;
import be.dafke.Accounting.Objects.BusinessCollectionDependent;
import be.dafke.Accounting.Objects.BusinessTyped;
import be.dafke.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class Mortgage extends Account implements BusinessTyped<AccountType>, BusinessCollectionDependent<Account> {
    private final static String TOTAL = "total";
    private final static String NRPAYED = "nrPayed";
    private final static String CAPITAL_NAME = "CapitalAccountName";
    private final static String CAPITAL_XML = "CapitalAccountXml";
    private final static String CAPITAL_HTML = "CapitalAccountHtml";
    private final static String INTREST_NAME = "IntrestAccountName";
    private final static String INTREST_XML = "IntrestAccountXml";
    private final static String INTREST_HTML = "IntrestAccountHtml";
    private ArrayList<Vector<BigDecimal>> table;
	private int alreadyPayed = 0;
	private Account capital, intrest;
	private BigDecimal startCapital;
    private final MultiValueMap<Calendar,Movement[]> movements;
    private BusinessCollection<Account> accounts;

    public Mortgage(){
        movements = new MultiValueMap<Calendar, Movement[]>();
    }

    public boolean isBookable(){
        return (capital!=null && intrest!=null);
    }
    @Override
    public boolean isDeletable(){
        return alreadyPayed > 0 && alreadyPayed < table.size();
    }

	public BigDecimal getStartCapital() {
		return startCapital;
	}

    public void setStartCapital(BigDecimal startCapital) {
        this.startCapital = startCapital;
    }

    public void setCapitalAccount(Account capital) {
		this.capital = capital;
	}

	public Account getCapitalAccount() {
		return capital;
	}

	public void setIntrestAccount(Account intrest) {
		this.intrest = intrest;
	}

	public Account getIntrestAccount() {
		return intrest;
	}

	public ArrayList<Vector<BigDecimal>> getTable() {
		return table;
	}

	public void setTable(ArrayList<Vector<BigDecimal>> table) {
		this.table = table;
	}

	public void setPayed(int nr) {
		alreadyPayed = nr;
	}

	public int getNrPayed() {
		return alreadyPayed;
	}

    public BigDecimal getMensuality(){
        return table.get(alreadyPayed).get(0);
    }

	public boolean isPayedOff() {
		return alreadyPayed == table.size();
	}

    @Override
    protected void book(Calendar date, Movement movement){
        System.out.println("Mortgage.book()");

        // Define new Amounts
        BigDecimal newIntrestAmount = table.get(alreadyPayed).get(1);
        BigDecimal newCapitalAmount = table.get(alreadyPayed).get(2);

        // Check for more recent Movements
        ArrayList<Movement[]> moreRecentMovements = movements.tailList(date, false);
        if(!moreRecentMovements.isEmpty())  {
            System.err.println("Insert needed");

            // Check if the amount is equal to the amount of the most recent Movement
            ArrayList<Movement[]> allMovements = movements.values();
            Movement[] lastMovements = allMovements.get(allMovements.size()-1);
            Movement lastMovement = lastMovements[0];

            if(lastMovement.getAmount().compareTo(movement.getAmount()) == 0){  // if fixed
                // Amounts are equal
                for(int i=moreRecentMovements.size()-1;i>=0;i--){
                    Movement[] couple = moreRecentMovements.get(i);
                    Movement oldIntrestMovement = couple[1];
                    Movement oldCapitalMovement = couple[2];

                    BigDecimal oldIntrestAmount = oldIntrestMovement.getAmount();
                    BigDecimal oldCapitalAmount = oldCapitalMovement.getAmount();

                    oldIntrestMovement.setAmount(newIntrestAmount);
                    oldCapitalMovement.setAmount(newCapitalAmount);

                    newIntrestAmount = oldIntrestAmount;
                    newCapitalAmount = oldCapitalAmount;
                }
            } else {
                // Amounts are not equal
                System.err.println("Degressive");
            }
        }

        // Define new Movements
        Movement newIntrestMovement = new Movement(newIntrestAmount, true);
        Movement newCapitalMovement = new Movement(newCapitalAmount, true);

        // Copy booking reference to new Movements
        newIntrestMovement.setBooking(movement.getBooking());
        newCapitalMovement.setBooking(movement.getBooking());

        alreadyPayed++;

        // Save data in Mortgages
        Movement[] couple = new Movement[3];
        couple[0] = movement;
        couple[1] = newIntrestMovement;
        couple[2] = newCapitalMovement;
        movements.addValue(date, couple);

        intrest.book(date, newIntrestMovement);
        capital.book(date, newCapitalMovement);
    }

    @Override
    protected void unbook(Calendar date, Movement movement){
        System.out.println("Mortgage.unbook()");

        alreadyPayed--;

        // Define new Amounts
        BigDecimal newIntrestAmount = table.get(alreadyPayed).get(1);
        BigDecimal newCapitalAmount = table.get(alreadyPayed).get(2);

        // Check for more recent Movements
        ArrayList<Movement[]> moreRecentMovements = movements.tailList(date, false);
        if(!moreRecentMovements.isEmpty())  {
            System.err.println("Insert needed");

            // Check if the amount is equal to the amount of the most recent Movement
            ArrayList<Movement[]> allMovements = movements.values();
            Movement[] lastMovements = allMovements.get(allMovements.size()-1);
            Movement lastMovement = lastMovements[0];

            if(lastMovement.getAmount().compareTo(movement.getAmount()) == 0){  // if fixed
                // Amounts are equal
                for(Movement[] couple:moreRecentMovements){
                    Movement oldIntrestMovement = couple[1];
                    Movement oldCapitalMovement = couple[2];

                    BigDecimal oldIntrestAmount = oldIntrestMovement.getAmount();
                    BigDecimal oldCapitalAmount = oldCapitalMovement.getAmount();

                    oldIntrestMovement.setAmount(newIntrestAmount);
                    oldCapitalMovement.setAmount(newCapitalAmount);

                    newIntrestAmount = oldIntrestAmount;
                    newCapitalAmount = oldCapitalAmount;
                }
            } else {
                // Amounts are not equal
                System.err.println("Degressive");
            }
        }

        ArrayList<Movement[]> movementsOnThatDay = movements.get(date);
        Movement[] lastMovementOnThatDay = movementsOnThatDay.get(movementsOnThatDay.size()-1);
        Movement intrestMovementToRemove = lastMovementOnThatDay[1];
        Movement capitalMovementToRemove = lastMovementOnThatDay[2];

        intrest.unbook(date, intrestMovementToRemove);
        capital.unbook(date,capitalMovementToRemove);
    }


    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        keySet.add(TOTAL);
        keySet.add(NRPAYED);
        keySet.add(CAPITAL_NAME);
        keySet.add(CAPITAL_XML);
        keySet.add(CAPITAL_HTML);
        keySet.add(INTREST_NAME);
        keySet.add(INTREST_XML);
        keySet.add(INTREST_HTML);
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> properties = super.getInitProperties();
        if(startCapital!=null){
            properties.put(TOTAL, startCapital.toString());
        }
        properties.put(NRPAYED, Integer.toString(alreadyPayed));
        if(capital!=null){
            properties.put(CAPITAL_NAME,capital.getName());
            if(capital.getXmlFile()!=null){
                properties.put(CAPITAL_XML,capital.getXmlFile().getPath());
            }
            if(capital.getHtmlFile()!=null){
                properties.put(CAPITAL_XML,capital.getHtmlFile().getPath());
            }
        }
        if(intrest!=null){
            properties.put(INTREST_NAME,intrest.getName());
            if(intrest.getXmlFile()!=null){
                properties.put(INTREST_XML,intrest.getXmlFile().getPath());
            }
            if(intrest.getHtmlFile()!=null){
                properties.put(INTREST_HTML, intrest.getHtmlFile().getPath());
            }
        }
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String startCapitalString = properties.get(TOTAL);
        String nrPayedString = properties.get(NRPAYED);
        if(startCapitalString!=null){
            startCapital = new BigDecimal(startCapitalString);
        }
        if(nrPayedString!=null){
            alreadyPayed = Integer.parseInt(nrPayedString);
        }
        String capitalAccount = properties.get(CAPITAL_NAME);
        if(capitalAccount!=null){
            capital = accounts.getBusinessObject(capitalAccount);
        }
        String intrestAccount = properties.get(INTREST_NAME);
        if(intrestAccount!=null){
            intrest = accounts.getBusinessObject(intrestAccount);
        }
    }

    @Override
    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        accounts = businessCollection;
    }
}
