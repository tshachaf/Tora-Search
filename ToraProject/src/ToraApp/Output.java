package ToraApp;

import java.util.ArrayList;

import StringFormatting.StringAlignUtils;
import StringFormatting.StringAlignUtils.Alignment;
import frame.frame;

public class Output {
	public static String[][] printPasukInfo(int countLines, String searchSTR, String line) throws NoSuchFieldException{
		ToraApp.perekBookInfo pBookInstance = ToraApp.findPerekBook(countLines);
		String[] htmlText1 = StringFormatting.HtmlGenerator.setRGBHtmlString(128, 150, 255);
		int i=0;
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		indexes.add(line.lastIndexOf(searchSTR,0));
		int STRLength=searchSTR.length();
		int newIndex;
		//find all occurences of searchSTR in Line and Color them
		while ((newIndex = line.lastIndexOf(indexes.get(i))) != -1) {
			indexes.add(newIndex);
			i++;
		}
		int lastIndex = 0;
		String lineHtml="";
		for (Integer thisIndex:indexes) {
			lineHtml += ((thisIndex>0)? line.substring(lastIndex,thisIndex-1):"")
						+ htmlText1[0] + line.substring(thisIndex-1,STRLength)+htmlText1[1];
			lastIndex = thisIndex+STRLength;
		}
		lineHtml += line.substring(lastIndex);
		
		String tempStr1 = "\u202B" + "\""+ htmlText1[0] + searchSTR + htmlText1[1] + "\" " + "נמצא ב"
				+ StringAlignUtils.padRight(pBookInstance.getBookName(), 6) + " "
				+ pBookInstance.getPerekLetters() + ":" + pBookInstance.getPasukLetters();
		Output.printText(StringAlignUtils.padRight(tempStr1, 32) + " =    " + line);
		Output.printText(StringAlignUtils.padRight(tempStr1, 32) + " =    " + lineHtml);

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
