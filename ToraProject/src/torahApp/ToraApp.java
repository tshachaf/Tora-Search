package torahApp;

import java.io.IOException;
import java.util.ArrayList;

import console.Methods;
import frame.Frame;
import ioManagement.ExcelFunctions;
import ioManagement.PropStore;

//import java.util.Formatter;
//import java.util.Locale;

/**
 * 
 */
/**
 * @author shementov777
 *
 */
public class ToraApp {
	// static StringBuffer buffer = new StringBuffer();
	// static Formatter formatter = new Formatter(buffer, new Locale("he", "IL"));

	public static String resourceFolder = "";
	public static final String ToraLineFile = resourceFolder + "Lines.txt";
	public static final String ToraLetterFile = resourceFolder + "NoTevot.txt";
	public static final String ToraTables = resourceFolder + "TorahTables.xls";
	
	public static String subTorahTableFile = "";
	public static String subTorahLineFile = "";
	public static String subTorahLetterFile = "";
	public static String differentSearchFile = "";

	private static byte guiMode = 0;
	public static final byte id_guiMode_Console = 0;
	public static final byte id_guiMode_Frame = 1;

	private static char[] hLetters = { 'א', 'ב', 'ג', 'ד', 'ה', 'ו', 'ז', 'ח', 'ט', 'י', 'כ', 'ל', 'מ', 'נ', 'ס', 'ע',
			'פ', 'צ', 'ק', 'ר', 'ש', 'ת' };
	
	// static char[] endLetters = { 'ך', 'ם', 'ן', 'ף', 'ץ' };
	static char[] otherLetters = { '-' };
	// tablePerekBooks
	// an array[5][X]
	// [0] contains a list of perek letters from א to נג
	// [1] Bereshit - each array index contains the amount of lines up to that perek
	// for example: [1][0] is perek א Bereshit, with value 0, because there are 0
	// lines up until perek א of Bereshit
	public static String[][] tablePerekBooks;
	public static int[] pereksPerBook;
	public static String[][] tablePerekParashot;
	public static String[] bookNames = { "בראשית", "שמות", "ויקרא", "במדבר", "דברים" };

	
	public static void starter() throws IOException {
		PropStore.load();
		Methods.arrayMethodCreator();
		// There are identical calls like this, one here the another in
		// frame.PopupTextMenu())
		tablePerekBooks = ExcelFunctions
				.readBookTableXLS(new String[] { ToraTables, PropStore.map.get(PropStore.subTorahTablesFile) }, 0, 0, 1, 6, 53);
		pereksPerBook = getPereksPerBook(tablePerekBooks);
		tablePerekParashot = ExcelFunctions
				.readBookTableXLS(new String[] { ToraTables, PropStore.map.get(PropStore.subTorahTablesFile) }, 1, 0, 1, 2, 56);
	}

	public static String cSpace() {
		return cSpace(1);
	}

	public static String cSpace(int count) {
		String str = "";
		for (int i = 1; i <= count; i++) {
			str += " ";
			// str += "\u00A0";
		}
		return str;
	}

	// This should be used to find a Book Name in Static Context
	public static String[] getBookNames() {
		return bookNames;
	}
	
	//Return all the Parashot Names in Order of Appearance
	public static String[] getParashaNames() {
		return tablePerekParashot[0];
	}
	
	// Find end of Line Count in Tora
	public static int lookupLineEnd() {
		return lookupLineNumberFromPerek(6,0);
	}
	
	//Find Line Number of Start of Perek
	public static int lookupLineNumberFromPerek(int bookNum, int perekNum) {
		return lookupLineNumberFromPerek(bookNum,perekNum,0);
	}
	
	//Find Line Number
	public static int lookupLineNumberFromPerek(int bookNum, int perekNum, int pasukNum) {
		if (bookNum==5) {
			return Integer.parseInt(tablePerekBooks[bookNum][(tablePerekBooks[bookNum].length-1)]);
		} else {
			return (Integer.parseInt(tablePerekBooks[bookNum+1][perekNum])+pasukNum);
		}
	}
	

	public static TorahPlaceClass checkStartBookParashaFromLineNum(int lineNum) {
	  //return null if not start of book or parasha
		//last book not checked, so last book is default (5)
		int bookNum=5;
		lineNum--;
		for (int i=1; i<6; i++) {
			if (Integer.parseInt(tablePerekBooks[i][0])==lineNum) {
				return new TorahPlaceClass(bookNames[i-1],true,true,true);
			}
			if (Integer.parseInt(tablePerekBooks[i][0])>lineNum) {
				bookNum = i-1;
				break;
			}
		}
		for (int i=0; i<tablePerekParashot[0].length; i++) {
			if (Integer.parseInt(tablePerekParashot[1][i])==lineNum) {
				return new TorahPlaceClass(tablePerekParashot[0][i],false,true,true);
			}
			if (Integer.parseInt(tablePerekParashot[1][i])>lineNum) {
				break;
			}
		}
		// Name of Perek is not returned
		for (int i=1; i<tablePerekBooks[bookNum].length; i++) {
			if (Integer.parseInt(tablePerekBooks[bookNum][i])==lineNum) {
				return new TorahPlaceClass(null,false,true,false);
			}
			if (Integer.parseInt(tablePerekBooks[bookNum][i])>lineNum) {
				break;
			}
		}
		return new TorahPlaceClass(null,false,false,false);
	}
	
	//Find Book Perek Pasuk from Line Number
	public static String lookupTorahPositionFromLineNumber(int lineNum) {
		//last book not checked, so last book is default (5)
		lineNum--;
		int bookNum=5;
		//all pereks are checked, this has been initialized arbitrarily
		int perekNum=0;
		int pasukNum=0;
		for (int i=2; i<6; i++) {
			if (Integer.parseInt(tablePerekBooks[i][0])>lineNum) {
				bookNum = i-1;
				break;
			}
		}
		for (int i=1; i<tablePerekBooks[bookNum].length; i++) {
			if (Integer.parseInt(tablePerekBooks[bookNum][i])>lineNum) {
				perekNum = i-1;
				break;
			}
		}
		pasukNum = lineNum-Integer.parseInt(tablePerekBooks[bookNum][perekNum])+1;
		perekNum++;
		return perekBookInfo.findLetters(perekNum) + ":" +
			perekBookInfo.findLetters(pasukNum);
	}

	
	//Find Parasha Name from Line Number
	public static int lookupParashaIndexFromLine(int line) {
		int index=0;
		//System.out.println(line);
		for (String s:tablePerekParashot[1]) {
			if (line<=Integer.parseInt(s)) {
				return (index-1);
			}
			index++;
		}
		return -1;
	}
	
	//Find Line Number
	public static int lookupLineNumberFromParasha(int parasha) {
		//System.out.println(tablePerekParashot[1][parasha]);
		return Integer.parseInt(tablePerekParashot[1][parasha]);
	}
	
	public static ArrayList<String> getPasukArray(int bookIndex,int perekIndex) {
		ArrayList<String> pasukArray = new ArrayList<String>();
		String thisPasukCountLineCount = tablePerekBooks[bookIndex+1][perekIndex];
		String nextPasukCountLineCount;
		int perekCount=0;
		if (tablePerekBooks[bookIndex+1].length>(perekIndex+1)) {
			nextPasukCountLineCount = tablePerekBooks[bookIndex+1][perekIndex+1];
			if (nextPasukCountLineCount.equals(thisPasukCountLineCount)) {
				nextPasukCountLineCount = tablePerekBooks[bookIndex+2][0];
			}
			perekCount=Integer.parseInt(nextPasukCountLineCount)-Integer.parseInt(thisPasukCountLineCount);
		}
		for (int i=1; i<=perekCount;i++) {
			pasukArray.add(perekBookInfo.findLetters(i));
		}
		return pasukArray;
	}
	
	//Get array of Chapters for Book
	public static ArrayList<String> getPerekArray(int bookIndex) {
		ArrayList<String> perekArray = new ArrayList<String>();
		for (int i=0; i<pereksPerBook[bookIndex];i++) {
			perekArray.add(tablePerekBooks[0][i]);
		}
		return perekArray;
	}
	
	public static byte getGuiMode() {
		return guiMode;
	}

	public static void setGuiMode(byte i) {
		guiMode = i;
	}

	public static int[] getPereksPerBook(String[][] tablePerekBooks) {
		int[] pPerBook = new int[5];
		for (int bookNum = 1; bookNum <= 5; bookNum++) {
			String prevLineCount = "-1";
			for (int perekIndex = 0; perekIndex < tablePerekBooks[bookNum].length; perekIndex++) {
				if (tablePerekBooks[bookNum][perekIndex].equals(prevLineCount)) {
					pPerBook[bookNum - 1] = perekIndex - 1;
					break;
				} else {
					prevLineCount = tablePerekBooks[bookNum][perekIndex];
				}
			}
		}
		return pPerBook;
	}

	public static class perekBookInfo {
		private static String[] bookNames = { "בראשית", "שמות", "ויקרא", "במדבר", "דברים" };
		private static char[][] letters = { { ' ', 'א', 'ב', 'ג', 'ד', 'ה', 'ו', 'ז', 'ח', 'ט' },
				{ ' ', 'י', 'כ', 'ל', 'מ', 'נ', 'ס', 'ע', 'פ', 'צ' } };
		private String bookName;
		private String perekLetters;
		private String pasukLetters;
		private int bookNumber;

		public static String findLetters(int num) {
			switch (num) {
			case 15:
				return "טו";
			case 16:
				return "טז";
			default:
				return (Character.toString(letters[1][num / 10]) + Character.toString(letters[0][num % 10]))
						.replaceAll("\\s+", "");
			}
		}

		public perekBookInfo(int bookNum, String pLetters, int pasukNum) {
			if (bookNum==-1) {
				bookNumber = 6;
				bookName = "";
				perekLetters = "שורה #";
				pasukLetters = String.valueOf(pasukNum);
			} else {
				perekLetters = pLetters;
				bookNumber = bookNum - 1;
				bookName = bookNames[bookNumber];
				pasukLetters = findLetters(pasukNum);
			}
		}

		// This returns the Book Name of Non-Static Context
		public String getBookName() {
			return bookName;
		}

		public int getBookNumber() {
			return bookNumber;
		}
		
		public String getPerekLetters() {
			return perekLetters;
		}

		public String getPasukLetters() {
			return pasukLetters;
		}
	}

	public static perekBookInfo findPerekBook(int lineNum) throws NoSuchFieldException {
		int oldI, oldJ;
		if (Frame.getCheckBox_DifferentSearch()) {
			return new perekBookInfo(-1,"",lineNum);
		}
		if (!ExcelFunctions.tableLoaded) {
			throw new NoSuchFieldException(
					"Tora Tables were not loaded, they should be loaded through ToraApp.starter() function");
		}
		oldI = 1;
		oldJ = 0;
		for (int i = 1; i < tablePerekBooks.length; i++) {
			// second:
			oldI = i;
			oldJ = 1;
			for (int j = 0; j < tablePerekBooks[i].length; j++) {
				if (Integer.parseInt(tablePerekBooks[i][j]) >= lineNum) {
					return new perekBookInfo(oldI, tablePerekBooks[0][oldJ],
							(lineNum - Integer.parseInt(tablePerekBooks[oldI][oldJ])));
					// break second;
				}
				oldI = i;
				oldJ = j;
				if (((j + 1) <= tablePerekBooks[i].length)
						&& (tablePerekBooks[i][j].equals(tablePerekBooks[i][j + 1]))) {
					break;
				}
				// System.out.println("i="+i+" j="+j+" data=" + tablePerekBooks[i][j]);
			}
		}
		return null;
	}
/*
	public static void main(String[] args) throws IOException {
		// findFirstLetters();
		// findWords();
		ToraApp.setGuiMode(ToraApp.id_guiMode_Console);
		starter();
		Console.menu();
		Output.printText("Program Exited...");

		// printText("One Two Three Four Five Six Seven Eight Nine Ten Eleven Twelve
		// Thirteen Fourteen Fifteen Sixteen Seventeen Eighteen Nineteen Twenty
		// TwentyOne TwentyTwo");
	}
*/
	
	public static char[] getHLetters() {
		return hLetters;
	}

	public static void setHLetters(char[] hLetters) {
		ToraApp.hLetters = hLetters;
	}
}
