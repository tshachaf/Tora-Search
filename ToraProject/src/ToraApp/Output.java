package ToraApp;

import java.util.ArrayList;

import StringFormatting.StringAlignUtils;
import StringFormatting.StringAlignUtils.Alignment;
import frame.frame;

public class Output {
	public static String markMatchesInLine(String line,String searchSTR,StringFormatting.HtmlGenerator htmlFormat) throws Exception{
		int i=0;
		String lineHtml="";
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		indexes.add(line.indexOf(searchSTR,0));
		int STRLength=searchSTR.length();
		int newIndex=0;
		//find all occurences of searchSTR in Line and Color them
		while ((newIndex = line.indexOf(searchSTR,indexes.get(i)+1)) != -1) {
			indexes.add(newIndex);
			i++;
		}
		int lastIndex = 0;
		for (Integer thisIndex:indexes) {
			Boolean wasSpace=false;
			String tempStr ="";
			if (thisIndex>0) {
			tempStr = line.substring(lastIndex,thisIndex);
			if (tempStr.charAt(tempStr.length() - 1)==' ') {
				wasSpace=true;
				//removes whitespace from the end
				tempStr = tempStr.replaceFirst("\\s++$", "");
			}
			}
			lineHtml += tempStr
						+((wasSpace)?ToraApp.cSpace():"")+ htmlFormat.getHtml(0) + line.substring(thisIndex,STRLength+thisIndex)+htmlFormat.getHtml(1);
			lastIndex = thisIndex+STRLength;
		}
		lineHtml += line.substring(lastIndex);
		return lineHtml;
	}
	
	public static String[][] printPasukInfo(int countLines, String searchSTR, String line, StringFormatting.HtmlGenerator markupStyle ) throws NoSuchFieldException{
		ToraApp.perekBookInfo pBookInstance = ToraApp.findPerekBook(countLines);
		try {
			String tempStr1 = "\u202B" + 
				"\""+ markupStyle.getHtml(0) + searchSTR + markupStyle.getHtml(1) + "\" " + "נמצא ב"
				+ StringAlignUtils.padRight(pBookInstance.getBookName(), 6) + " "
				+ pBookInstance.getPerekLetters() + ":" + pBookInstance.getPasukLetters();
		//Output.printText(StringAlignUtils.padRight(tempStr1, 32) + " =    " + line);
		String lineHtml = markMatchesInLine(line, searchSTR, markupStyle);
		Output.printText(StringAlignUtils.padRight(tempStr1, 32) + " =    " + lineHtml);
		} catch (Exception e) {
			System.out.println("Error at line: " +countLines);
			e.printStackTrace();
		}
		return (new String[][] {{ searchSTR, pBookInstance.getBookName(),
			pBookInstance.getPerekLetters(), pBookInstance.getPasukLetters(), line }});
	}
	
	public static void printText(String text) {
		printText(text, (byte)0);
	}

	public static void printText(String text, int mode)
	{
		printText(text,(byte) mode);
	}
	
	public static void printText(String text, byte mode) {
		// mode 0 = regular
		// mode 1 = attention
		// mode 2 = silence on GUI
		StringAlignUtils util = new StringAlignUtils(frame.panelWidth, Alignment.RIGHT);
		switch (ToraApp.getGuiMode()) {
		case 1: // GUI Mode
				switch (mode) {
				case 0:
				case 1:
					frame.appendText(util.format(text), mode);
					break;
				case 2:
					break;
				}
			break;
		default: // Console Mode - Reserved guiMode=0
			switch (mode) {
			case 0: // user text
				System.out.println(util.format(text));
				break;
			case 1: // debug mode
			case 2:
				System.err.println(util.format(text));
				break;
			}
		}
	}

}
