package be.dafke.Coda;

import java.util.ArrayList;
//import java.util.Iterator;

import be.dafke.Coda.Objects.Statement;

public class Statements {
	private static ArrayList<Statement> statements = new ArrayList<Statement>();
	
	public static void add(Statement value) {
		statements.add(value);
	}
	
//	public String toString(){
/*		StringBuilder builder = new StringBuilder("Statement:\r\n");
		Iterator<Statement> it = statements.iterator();
		while(it.hasNext()){
			builder.append(it.next());
		}
		return builder.toString();
	}
*/		
	public static Statement getStatement(int nr){
		return statements.get(nr);
	}
	
	public static int getSize(){
		return statements.size();
	}

}
