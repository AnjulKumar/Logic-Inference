package aiHomework3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class agent {

	static int numSent = 0;
	static predicate query = new predicate();
	static knowledgeBase kb = new knowledgeBase();

	// static String xSubstitute = null;

	public static void main(String[] args) {
		readData();
		// displaySentence(newS);
		// unification();
		predicate q = query;
		predicate p = bwChaining(q);
		int count = 0;
		for (String arg : q.arguments) {
			int index = q.arguments.indexOf(arg);
			if (p != null && arg.equals(p.arguments.get(index))) {
				count++;
			}
		}
		if (count == q.arguments.size()) {
			writeData("TRUE");
		} else {
			writeData("FALSE");
		}

	}

	public static void readData() {
		int countLine = 0;
		FileReader inputFile;
		try {
			inputFile = new FileReader(
					"input.txt");
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String line;

			while ((line = bufferReader.readLine()) != null) {
				countLine++;
				if (countLine == 2) {
					numSent = Integer.parseInt(line);
				} else if (countLine == 1) {
					predicateArgument(line, countLine, query);
					query.predLine = line;
				} else {
					sentence newS = new sentence();
					String[] conj = line.split("&|=>");
					int flag = 0;

					for (String part : conj) {
						flag = 0;
						predicate existPred = null;
						for (predicate str : kb.pred) {
							// System.out.println("STRING:"+str.predLine+" "+
							// part+ " " + str.predLine.equals(part));
							if (str.predLine.equals(part)) {

								flag = 1;
								existPred = str;
								break;
							}
						}
						// System.out.println(flag);
						if (flag == 0) {
							predicate newPred = new predicate();
							newPred.predLine = part;
							// System.out.println("Query arguments: " + part);
							predicateArgument(part, countLine, newPred);
							newS.pred.add(newPred);
							kb.pred.add(newPred);
						} else if (flag == 1) {

							newS.pred.add(existPred);
						}
					}
					for (int i = 0; i < line.length(); i++) {
						char c = line.charAt(i);
						if (c == '&') {
							newS.op.add(Character.toString(c));
						} else if (c == '=') {
							char nextC = line.charAt(i + 1);
							newS.op.add(c + "" + nextC);
						}
					}
					kb.sent.add(newS);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeData(String value) {
		try {
			// PrintWriter out = new PrintWriter(new BufferedWriter(new
			// FileWriter("output.txt",false)));
			PrintWriter out = new PrintWriter(
					new BufferedWriter(
							new FileWriter(
									"output.txt",
									false)));

			out.println(value);

			out.close();
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void predicateArgument(String line, int countLine,
			predicate newPred) {

		String pString1 = null;
		String regexString1 = "(^.*?)" + Pattern.quote("(");
		Pattern pattern1 = Pattern.compile(regexString1);
		// text contains the full text that you want to extract data
		Matcher matcher1 = pattern1.matcher(line);

		while (matcher1.find()) {
			pString1 = matcher1.group(1);
			newPred.pName = pString1;
			// System.out.println("Query Predicate: " + pString1);
		}

		String pString2 = null;
		String regexString2 = Pattern.quote("(") + "(.*?)" + Pattern.quote(")");
		Pattern pattern2 = Pattern.compile(regexString2);
		// text contains the full text that you want to extract data
		Matcher matcher2 = pattern2.matcher(line);

		while (matcher2.find()) {
			pString2 = matcher2.group(1);
			// System.out.println("Query arguments: " + pString2);
		}
		String[] parts = pString2.split(",");
		for (String part1 : parts) {
			newPred.arguments.add(part1);

			// System.out.println("Query arguments parts: " + part1);
		}
	}

	public static void displayPredicate(predicate pred) {
		System.out.println("PREDICATE: " + pred.predLine);

		System.out.println("Predicate name: " + pred.pName);
		for (String arg : pred.arguments) {
			System.out.println("Argument: " + arg);
		}

	}

	public static void displaySentence(sentence sent) {

		if (sent == null) {
			System.out.println("null");
			return;
		}
		int length = sent.pred.size();
		// System.out.println(length);
		for (predicate p : sent.pred) {
		//	displayPredicate(p);
			if (sent.pred.indexOf(p) < length - 1) {
				System.out.println("OPERATOR: "
						+ sent.op.get(sent.pred.indexOf(p)));
			}
		}
	}

	public static ArrayList<predicate> findNameSake(predicate p) {

		ArrayList<predicate> facts = new ArrayList<predicate>();

		for (sentence s : kb.sent) {

			// System.out.println("s.op.indexOf(=>)"+ s.op.indexOf("=>"));
			if (s.op.indexOf("=>") == -1) {
				predicate singlePred = s.pred.get(0);

				System.out.println(p.pName + " " + singlePred.pName + " "
						+ p.pName.equals(singlePred.pName));
				if (singlePred != null && p.pName.equals(singlePred.pName)) {
					int count = 0;

					for (String arg : p.arguments) {
						int index = p.arguments.indexOf(arg);

						if (arg.equals(singlePred.arguments.get(index))) {
							count++;
							System.out.println("findNameSake: constants ="
									+ count);

						} else if ((arg.equals("x"))
								&& (!singlePred.arguments.get(index)
										.equals("x"))) {
							count++;
							singlePred.subXpred = singlePred.arguments
									.get(index);
							System.out.println("findNameSake: x =" + count);
							// p.subXpred = singlePred.arguments.get(index);
							System.out.println("singlePred.name="
									+ singlePred.pName + " singlePred.subXpred"
									+ singlePred.subXpred);
						}
					}
					/*
					 * System.out.println("count == p.arguments.size()" + count
					 * + " " + p.arguments.size() + " " + (count ==
					 * p.arguments.size()));
					 */
					if (count == p.arguments.size()) {
						facts.add(singlePred);
					}
				}
			}
		}
		System.out.println("retuning facts findnamesake");
		return facts;
	}

	public static ArrayList<String> trueAntecedent(sentence querySent) {

		int index = querySent.op.indexOf("=>");


		ArrayList<String> xSub = new ArrayList<String>();

		for (predicate p : querySent.pred) {

			if ((querySent.pred.indexOf(p) < (index + 1))) {

				System.out
						.println("Predicate facts finding-----------------------");
			//	displayPredicate(p);
				System.out
						.println("Predicate facts finding-----------------------");

				ArrayList<predicate> facts = findNameSake(p);
				System.out.println("Number of facts found: " + facts.size());
				for (predicate fact : facts) {
					p.facts.add(fact);
				}
			}
		}
		// int size = querySent.pred.size()-1;

		for (predicate p : querySent.pred) {
			if ((querySent.pred.indexOf(p) < (index + 1))) {
				if (p.facts.size() == 0) {
					System.out.println("\npredicate has no fact:");
					return xSub;
				}
			}
		}

		if ((querySent.pred.size() - 1) == 1) {
			predicate p = querySent.pred.get(0);
			for (predicate ft : p.facts) {

				xSub.add(ft.subXpred);
			}
		}

		for (predicate f : querySent.pred.get(0).facts) {

			String xValue = f.subXpred;
			for (int i = 1; i < querySent.pred.size(); i++) {

				predicate p = querySent.pred.get(i);
				if ((querySent.pred.indexOf(p) < (index + 1))) {

					for (predicate ft : p.facts) {

						System.out.println("ft.subXpred.equals(xValue): "
								+ ft.subXpred + "" + xValue + "="
								+ ft.subXpred.equals(xValue));
						if (ft.subXpred.equals(xValue)) {
							xSub.add(xValue);
						}
					}
				}
			}
		}

		System.out.println("fact size= " + xSub.size());
		return xSub;
	}
	

	public static boolean predEqualsCons(sentence sent){
		
		int s = sent.pred.size();
		predicate cons = sent.pred.get(s-1);
		int constant =0;
		int x=0;
		int flag =0;
		
		for(int i=0;i < sent.pred.size()-1; i++ ){
			predicate p = sent.pred.get(i);
			if(p.pName.equals(cons.pName)){
				for (String arg : p.arguments) {
					int argIndex = p.arguments.indexOf(arg);
					System.out.println("arg:"+arg);
					if (arg.equals(cons.arguments.get(argIndex))) {
						/*
						 * System.out.println(arg + "=" +
						 * (q.arguments.get(argIndex)) +
						 * arg.equals(q.arguments.get(argIndex)));
						 */
						constant++;
					} else if (arg.equals("x")) {
						x++;
						System.out.println("x"+x);
					} else if (!arg.equals("x")
							&& cons.arguments.get(argIndex).equals("x")) {
						constant++;
						// argPtr = arg;
					}
				}
				if (constant + x == cons.arguments.size()) {
					flag++;
				}

			}
		}
		if(flag ==0){
			return false;
		}
		else{
			return true;
		}
		
	}
	
	
	public static predicate bwChaining(predicate pred) {


		
		predicate retPred = null;
		predicate rPred = null;
		System.out.println("\nBW Chaining");
		/*
		 * int count=0; int predArgSize = pred.arguments.size(); for(String arg:
		 * pred.arguments){
		 * System.out.println("chaining check args not equal to x"+
		 * !arg.equals("x")); if(!arg.equals("x")){ count++; } } if(count==
		 * predArgSize){ return pred; }
		 */

		ArrayList<sentence> returnSent = unification(pred);
		System.out.println("returnSent.predicate.size()" + returnSent.size());

		if (returnSent.size() != 0) {

			for (sentence querySent : returnSent) {

				int tf = 1;
				System.out.println("-----------------------------------");
			//	displaySentence(querySent);
				System.out.println("-----------------------------------");
				if (querySent != null) {

					String x = null;
					int index = querySent.op.indexOf("=>");
					for (predicate p : querySent.pred) {
						
						if(querySent.pred.size()==1){
							if (p.arguments.indexOf("x") == -1) {
								System.out.println("Returning p:" + p.predLine);
								retPred = p;
								return p;
							}
						}
						int indX = p.arguments.indexOf("x");

					//	System.out.println("PREDICATE-LINE: " + p.predLine + " "+ index + " "+ querySent.pred.indexOf(p));

						
						bwChaining(p);

						if (index != -1 && querySent.pred.indexOf(p) == index) {
							System.out.println("breaking");

							break;
						}
					}

					ArrayList<String> xS = trueAntecedent(querySent);

					if (xS.size() != 0) {

						for (String xSub : xS) {
							predicate newPred = new predicate();
							predicate qSentImpPred = querySent.pred
									.get(index + 1);
							int flag = 0;

							newPred.pName = qSentImpPred.pName;
							System.out.println(newPred.pName);

							for (String pArg : qSentImpPred.arguments) {

								if (pArg.equals("x")) {
									newPred.arguments.add(xSub);
									newPred.subXpred = xSub;
								} else {
									newPred.arguments.add(pArg);

								}
							}

							newPred.subXpred = xSub;
					//		displayPredicate(newPred);
							sentence newSent = new sentence();
							newSent.pred.add(newPred);
							System.out
									.println("add to kb----------------------");
						//	displaySentence(newSent);
							System.out
									.println("add to kb----------------------");
							kb.sent.add(newSent);
							retPred = newSent.pred.get(0);
							System.out.println("\n NEW PREDICATE MADE");
						}
					} else if (xS.size() == 0) {
						tf = 0;
						System.out.println("\n NO NEW PREDICATE MADE");

						continue;
						/*
						 * writeData("FALSE"); System.exit(0);
						 */
					}

				} else if (querySent == null) {
					tf = 0;

					/*
					 * writeData("FALSE"); System.exit(0);
					 */
				}

				if (tf == 1) {
					rPred = retPred;
					return rPred;
				}
				System.out.println("\nNEW PATH");
				/*
				 * displayPredicate(retPred); return retPred;
				 */
			}
		} else {
			return null;
		}
		return rPred;
	}

	public static ArrayList<sentence> unification(predicate q) {

		ArrayList<sentence> returnSent = new ArrayList<sentence>();

		System.out.println("\nenter unification");
		String xSub = null;
		sentence querySent = null;
		int retConst = 0;
		String argPtr = null;

		int x = 0;
		int constant = 0;

		

		for (sentence s : kb.sent) {
			System.out.println("enter unification for 1");
			constant = 0;
			x = 0;
			int impIndex = s.op.indexOf("=>");
			if (impIndex != -1) {
				predicate conPred = s.pred.get(impIndex + 1);
				System.out.println("conPred predLine"+ conPred.predLine);
				if (conPred.pName.equals(q.pName)) {
					for (String arg : conPred.arguments) {
						int argIndex = conPred.arguments.indexOf(arg);
						System.out.println("arg:"+arg);
						if (arg.equals(q.arguments.get(argIndex))) {
							/*
							 * System.out.println(arg + "=" +
							 * (q.arguments.get(argIndex)) +
							 * arg.equals(q.arguments.get(argIndex)));
							 */
							constant++;
						} else if (arg.equals("x")) {
							x++;
							System.out.println("x"+x);
						} else if (!arg.equals("x")
								&& q.arguments.get(argIndex).equals("x")) {
							constant++;
							// argPtr = arg;
						}
					}
					
					System.out.println("(constant + x == conPred.arguments.size())"+ ((constant + x == conPred.arguments.size())));
					if (constant + x == conPred.arguments.size()) {
						if (constant >= retConst) {
							retConst = constant;
							querySent = s;
							returnSent.add(querySent);
							// conPred.subXpred = argPtr;
						}
					}
				}
			}
		}

		for (sentence s : kb.sent) {
			// System.out.println("enter unification for 2");
			constant = 0;
			x = 0;
			int impIndex = s.op.indexOf("=>");
			if (impIndex == -1) {
				predicate conPred = s.pred.get(0);

				// System.out.println("conPred predLine"+ conPred.predLine
				// +"conPred.pName.equals(q.pName)"+conPred.pName.equals(q.pName));
				if (conPred.pName.equals(q.pName)) {

					for (String arg : conPred.arguments) {
						int argIndex = conPred.arguments.indexOf(arg);
						// System.out.println(arg.equals(q.arguments.get(argIndex)));
						if (arg.equals(q.arguments.get(argIndex))) {

							System.out.println("Unification:" + arg + "="
									+ (q.arguments.get(argIndex))
									+ arg.equals(q.arguments.get(argIndex)));

							constant++;
							System.out.println("constant=" + constant);

						} else {
							/*
							 * System.out
							 * .println("if !arg.equals(q.arguments.get(argIndex))"
							 * + q.arguments.get(argIndex).equals( "x"));
							 */
							if (q.arguments.get(argIndex).equals("x")) {
								x++;
								System.out.println("x=" + x);
								argPtr = arg;
							}
						}
						/*
						 * else if(arg.equals("x")){ x++; }
						 */
					}
					/*
					 * System.out
					 * .println("((constant + x) ==conPred.arguments.size())" +
					 * ((constant + x) == conPred.arguments .size()));
					 */
					if (constant + x == conPred.arguments.size()) {
						// System.out.println((constant > retConst) +" " +
						// "constant: "+ constant +"retConst" + retConst);
						if (constant >= retConst) {
							retConst = constant;
							querySent = s;
							returnSent.add(querySent);
							conPred.subXpred = argPtr;
							// xSubstitute = argPtr;

							// System.out.println("conPred, conPred.subXpred: "
							// +conPred.pName+"," +conPred.subXpred);

						}
					}
				}
			}
		}
		/*
		 * if (retConst == q.arguments.size()) { displaySentence(querySent);
		 * return querySent; }
		 */

		System.out.println("Unification sentence returned");
		// displaySentence(querySent);
		return returnSent;
	}

}

class knowledgeBase {
	ArrayList<sentence> sent = new ArrayList<sentence>();
	ArrayList<predicate> pred = new ArrayList<predicate>();
}

class sentence {
	ArrayList<predicate> pred = new ArrayList<predicate>();
	ArrayList<String> op = new ArrayList<String>();
	String subXsent;
}

class predicate {
	String predLine;
	String pName;
	ArrayList<String> arguments = new ArrayList<String>();
	ArrayList<predicate> facts = new ArrayList<predicate>();
	ArrayList<String> xSubs = new ArrayList<String>();
	String subXpred;
}
