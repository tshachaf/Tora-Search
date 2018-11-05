package frame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import java.awt.ComponentOrientation;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import ToraApp.ToraApp;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Dimension;

import ToraApp.ExcelFunctions;
import ToraApp.PropStore;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JProgressBar;

public class frame {
	private static final String combo_strSearch = "חיפוש רגיל";
	private static final String combo_strGimatriaSearch = "חיפוש גימטריה";
	private static final String combo_strGimatriaCalculate = "חישוב גימטריה";
	private static final String combo_strDilugim = "דילוגים";
	private static final String buttonRunText = "חפש";
	private static final String buttonCancelText = "בטל";
	private static final String buttonCancelRequestText = "מבטל..";
	private static final String checkBox_gimatriaSofiot_text = "<html>" + "חישוב מיוחד" + "<br/>" + "לסופיות"
			+ "</html>";
	private static final String checkBox_countPsukim_true = "<html>" + "ספירת פסוקים" + "<br/>" + "שנמצאו" + "</html>";
	private static final String checkBox_countPsukim_false = "ספירת מציאות";

	private static Boolean methodCancelRequest = false;
	private static Boolean methodRunning = false;
	private JFrame frame;
	private static JButton button;
	private static JTextField textField_Search;
	private static JTextField textField_dilugMin;
	private static JTextField textField_dilugMax;
	private static JTextField textField_offset;
	private static JComboBox<?> comboBox_main;
	private static JTextField textField_padding;
	private static JCheckBox checkBox_advancedOptions;
	static JTextPane textPane;
	private SwingActivity activity;
	private static JScrollPane scrollPane;
	private static HTMLDocument doc = new HTMLDocument();
	private static HTMLEditorKit kit = new HTMLEditorKit();
	private static JCheckBox checkBox_gimatriaSofiot;
	private static JCheckBox checkBox_wholeWord;
	private static JCheckBox checkBox_countPsukim;
	static public int panelWidth;
	private JButton btnNewButton;
	private JPanel panel_1;
	private JPopupMenu popupMenu;
	private static JProgressBar progressBar;
	private static JLabel label_offset;
	private static JLabel label_padding;
	private static JLabel label_dProgress;
	private static JLabel label_countMatch;
	private static JLabel label_dilugMin;
	private static JLabel label_dilugMax;
	private static JComboBox<?> comboBox_sub;
	
	class PopUpTextPane extends JPopupMenu {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JMenuItem anItem;

		public PopUpTextPane() {
			anItem = new JMenuItem("חפש...");
			anItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					dialogFrame dFrame = dialogFrame.getInstance();
					Point p = frame.getLocation();
					dFrame.setLocation((int) (p.getX() + frame.getWidth() - dFrame.getWidth()),
							(int) (p.getY() + frame.getHeight() - dFrame.getHeight()));
					dFrame.setVisible(true);
				}
			});
			add(anItem);
			anItem = new JMenuItem("הסר הארות");
			anItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					dialogFrame.clearLastSearch();
					HighLighter.removeHighlights(textPane);
				}
			});
			add(anItem);
			anItem = new JMenuItem("לעבור על המציאות");
			anItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					HighLighter.scrollWords(textPane);
					frame.repaint();
				}
			});
			add(anItem);
			JMenu menu = new JMenu("טעינת קובץ חדש");
			anItem = new JMenuItem("טבלת אינדקס - TorahTables.xls");
			anItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					JFileChooser chooser = new JFileChooser();
					File workingDirectory = new File(System.getProperty("user.dir"));
					chooser.setCurrentDirectory(workingDirectory);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						System.out
								.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
						ToraApp.subTorahTableFile = chooser.getSelectedFile().getAbsolutePath();
						// There are identical calls like this, one here the another in
						// ToraApp.starter()
						ToraApp.tablePerekBooks = ExcelFunctions.readXLS(new String[] { ToraApp.subTorahTableFile }, 0,
								0, 1, 6, 53);
						PropStore.map.put(PropStore.subTorahTablesFile, ToraApp.subTorahTableFile);
						PropStore.store();
					}
				}
			});
			menu.add(anItem);
			anItem = new JMenuItem("קובץ תורה מחולק בשורות - Lines.txt");
			anItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					JFileChooser chooser = new JFileChooser();
					File workingDirectory = new File(System.getProperty("user.dir"));
					chooser.setCurrentDirectory(workingDirectory);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						System.out
								.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
						ToraApp.subTorahLineFile = chooser.getSelectedFile().getAbsolutePath();
						// There are identical calls like this, one here the another in
						// ToraApp.starter()
						PropStore.map.put(PropStore.subTorahLineFile, ToraApp.subTorahLineFile);
						PropStore.store();
					}
				}
			});
			menu.add(anItem);
			anItem = new JMenuItem("קובץ תורה אותיות - NoTevot.txt");
			anItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					JFileChooser chooser = new JFileChooser();
					File workingDirectory = new File(System.getProperty("user.dir"));
					chooser.setCurrentDirectory(workingDirectory);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						System.out
								.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
						ToraApp.subTorahLetterFile = chooser.getSelectedFile().getAbsolutePath();
						// There are identical calls like this, one here the another in
						// ToraApp.starter()
						PropStore.map.put(PropStore.subTorahLettersFile, ToraApp.subTorahLetterFile);
						PropStore.store();
					}
				}
			});
			menu.add(anItem);
			add(menu);
		}
	}

	class PopClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger())
				doPop(e);
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger())
				doPop(e);
		}

		private void doPop(MouseEvent e) {
			PopUpTextPane menu = new PopUpTextPane();
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public static void clearTextPane() {
		textPane.setText("");
	}

	public static String getTextField_padding() {
		return textField_padding.getText();
	}

	public static String getTextField_offset() {
		return textField_offset.getText();
	}

	public static String getTextField_Search() {
		return textField_Search.getText();
	}

	public static String getTextField_dilugMin() {
		return textField_dilugMin.getText();
	}

	public static String getTextField_dilugMax() {
		return textField_dilugMax.getText();
	}

	public static Boolean getCheckBox_gimatriaSofiot() {
		return checkBox_gimatriaSofiot.isSelected();
	}

	public static Boolean getCheckBox_wholeWord() {
		return checkBox_wholeWord.isSelected();
	}

	public static Boolean getCheckBox_countPsukim() {
		return checkBox_countPsukim.isSelected();
	}

	public static int getComboBox_main() {
		return comboBox_main.getSelectedIndex();
	}

	public static Boolean getMethodCancelRequest() {
		return methodCancelRequest;
	}

	public static void setButtonEnabled(Boolean bool) {
		button.setEnabled(bool);
	}

	public static void setMethodRunning(Boolean bool) {
		methodRunning = bool;
		if (bool) {
			button.setText(buttonCancelText);
		} else {
			button.setText(buttonRunText);
			methodCancelRequest = false;
		}
	}

	public static void showProgressBar(Boolean bool, int flag) {
		// 0b01 - progressBar and countLabel
		// 0b10 - label
		if ((flag & 0b01) == 0b01) {
			progressBar.setVisible(bool);
			label_countMatch.setVisible(bool);
		}
		if ((flag & 0b10) == 0b10) {
			label_dProgress.setVisible(bool);
		}
	}

	public static void setProgressBar(int num) {
		progressBar.setValue(num);
	}

	public static void setLabel_countMatch(String str) {
		label_countMatch.setText(str);
	}

	public static void setLabel_dProgress(String str) {
		label_dProgress.setText(str);
	}

	public static void initValues() {
		textField_Search.setText(PropStore.map.get(PropStore.searchWord));
		textField_dilugMin.setText(PropStore.map.get(PropStore.minDilug));
		textField_dilugMax.setText(PropStore.map.get(PropStore.maxDilug));
		textField_offset.setText(PropStore.map.get(PropStore.offsetDilug));
		textField_padding.setText(PropStore.map.get(PropStore.paddingDilug));
		ToraApp.subTorahTableFile = PropStore.map.get(PropStore.subTorahTablesFile);
		ToraApp.subTorahLineFile = PropStore.map.get(PropStore.subTorahLineFile);
		ToraApp.subTorahLetterFile = PropStore.map.get(PropStore.subTorahLettersFile);
		checkBox_gimatriaSofiot.setSelected(Boolean.parseBoolean(PropStore.map.get(PropStore.bool_gimatriaSofiot)));
		checkBox_wholeWord.setSelected(Boolean.parseBoolean(PropStore.map.get(PropStore.bool_wholeWord)));
		checkBox_countPsukim.setSelected(Boolean.parseBoolean(PropStore.map.get(PropStore.bool_countPsukim)));
		checkBox_countPsukim.setText(
				((checkBox_countPsukim.isSelected()) ? checkBox_countPsukim_true : checkBox_countPsukim_false));
	}

	public static void saveValues() {
		PropStore.addNotNull(PropStore.searchWord, textField_Search.getText());
		PropStore.addNotNull(PropStore.minDilug, textField_dilugMin.getText());
		PropStore.addNotNull(PropStore.maxDilug, textField_dilugMax.getText());
		PropStore.addNotNull(PropStore.offsetDilug, textField_offset.getText());
		PropStore.addNotNull(PropStore.paddingDilug, textField_padding.getText());
		PropStore.addNotNull(PropStore.subTorahTablesFile, ToraApp.subTorahTableFile);
		PropStore.addNotNull(PropStore.subTorahLineFile, ToraApp.subTorahLineFile);
		PropStore.addNotNull(PropStore.subTorahLettersFile, ToraApp.subTorahLetterFile);
		PropStore.addNotNull(PropStore.bool_gimatriaSofiot, String.valueOf(checkBox_gimatriaSofiot.isSelected()));
		PropStore.addNotNull(PropStore.bool_wholeWord, String.valueOf(checkBox_wholeWord.isSelected()));
		PropStore.addNotNull(PropStore.bool_countPsukim, String.valueOf(checkBox_countPsukim.isSelected()));
		PropStore.store();
	}

	private final static int textHtmlSize = 5;
	private static StringFormatting.HtmlGenerator attentionHTML = new StringFormatting.HtmlGenerator(textHtmlSize, 250,
			40, 40, 0b111);
	private static StringFormatting.HtmlGenerator mainStyleHTML = new StringFormatting.HtmlGenerator(textHtmlSize, 128,
			88, 255, 0b111);
	// public static StringFormatting.HtmlGenerator markupStyleHTML = new
	// StringFormatting.HtmlGenerator(textHtmlSize+1, 93, 192, 179,0b100);
	public static StringFormatting.HtmlGenerator markupStyleHTML = new StringFormatting.HtmlGenerator(textHtmlSize + 1,
			245, 195, 92, 0b100);

	public static StringFormatting.HtmlGenerator headerStyleHTML = new StringFormatting.HtmlGenerator(textHtmlSize + 1,
			58, 124, 240, 0b100);
	public static StringFormatting.HtmlGenerator footerStyleHTML = new StringFormatting.HtmlGenerator(0, 255, 144, 180,
			0b100);

	public static void appendText(String str) throws BadLocationException {
		appendText(str, (byte) 0);
	}

	public static void appendText(String str, byte mode) {
		try {
			// doc.insertString(0, "\n"+str, null );
			// mode 0 = regular style with Carriage Return
			// mode 1 = error style with Carriage Return
			// mode 2 = regular style without Carriage Return

			switch (mode) {
			case 0:
				// kit.insertHTML(doc, doc.getLength(), "<b>hello", 0, 0, HTML.Tag.B);
				kit.insertHTML(doc, doc.getLength(), (mainStyleHTML.getHtml(0) + str + mainStyleHTML.getHtml(1)), 0, 0,
						null);
				break;
			case 1:
				kit.insertHTML(doc, doc.getLength(), (attentionHTML.getHtml(0) + str + attentionHTML.getHtml(1)), 0, 0,
						null);
				break;
			}

			scrollPane.getHorizontalScrollBar().setValue(0);
			scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e + " in Code of frame.appendText()");
		} catch (BadLocationException e) {
			e.printStackTrace();
			System.out.println(e + " in Code of frame.appendText()");
		}

	}

	public static void clearText() {
		textPane.setText("");
	}

	
	private static void changeLayout(String str) {
		switch (str) {
		case combo_strSearch:
		case combo_strGimatriaSearch:
		case combo_strGimatriaCalculate:
			label_dilugMax.setVisible(false);
			label_dilugMin.setVisible(false);
			textField_dilugMax.setVisible(false);
			textField_dilugMin.setVisible(false);
			checkBox_advancedOptions.setVisible(false);
			label_offset.setVisible(false);
			label_padding.setVisible(false);
			textField_offset.setVisible(false);
			textField_padding.setVisible(false);
			switch (str) {
			case combo_strSearch:
			case combo_strGimatriaSearch:
				comboBox_sub.setVisible(true);
				checkBox_countPsukim.setVisible(true);
				checkBox_wholeWord.setVisible(true);
				break;
			case combo_strGimatriaCalculate:
				comboBox_sub.setVisible(false);
				checkBox_countPsukim.setVisible(false);
				checkBox_wholeWord.setVisible(false);
			}
			break;
		case combo_strDilugim:
			label_dilugMax.setVisible(true);
			label_dilugMin.setVisible(true);
			textField_dilugMax.setVisible(true);
			textField_dilugMin.setVisible(true);
			checkBox_advancedOptions.setVisible(false);
			label_offset.setVisible(true);
			label_padding.setVisible(true);
			textField_offset.setVisible(true);
			textField_padding.setVisible(true);
			comboBox_sub.setVisible(true);
			checkBox_countPsukim.setVisible(true);
			checkBox_wholeWord.setVisible(true);
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame window = new frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public frame() throws IOException, BadLocationException {
		initialize();
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				panelWidth = (int) (scrollPane.getWidth() / 10);
				// do stuff
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 * @throws BadLocationException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() throws IOException, BadLocationException {
		ToraApp.setGuiMode((byte) 1);
		frame = new JFrame();
		frame.setTitle("חיפוש בתורה");
		frame.getContentPane().setFont(new Font("Miriam Mono CLM", Font.PLAIN, 16));
		frame.setFont(new Font("Miriam Mono CLM", Font.PLAIN, 16));
		frame.setBounds(100, 100, 1600, 887);
		frame.setMinimumSize(new Dimension(550, 420));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JInternalFrame internalFrame = new JInternalFrame("חיפוש בתורה");
		frame.getContentPane().add(internalFrame, BorderLayout.NORTH);

		panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		btnNewButton = new JButton("קבע ברירת מחדל");
		panel_1.add(btnNewButton);

		btnNewButton.setToolTipText("שמירת ערכי חיפוש לברירת מחדל");

		comboBox_main = new JComboBox();
		panel_1.add(comboBox_main);
		comboBox_main.setMaximumSize(new Dimension(200, 32767));
		comboBox_main.setFont(new Font("Miriam Mono CLM", Font.BOLD, 18));
		comboBox_main.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		comboBox_main.setModel(new DefaultComboBoxModel(new String[] { combo_strSearch, combo_strGimatriaSearch,
				combo_strGimatriaCalculate, combo_strDilugim }));

		comboBox_main.setBackground(new java.awt.Color(255, 240, 240));

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 10));
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		frame.getContentPane().add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 124, 42, 0 };
		gbl_panel.rowHeights = new int[] { 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel label = new JLabel("חיפוש: ");
		label.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);

		textField_Search = new JTextField();
		textField_Search.setMinimumSize(new Dimension(150, 25));
		textField_Search.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		textField_Search.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_textField_Search = new GridBagConstraints();
		gbc_textField_Search.anchor = GridBagConstraints.NORTHEAST;
		gbc_textField_Search.insets = new Insets(0, 0, 5, 0);
		gbc_textField_Search.gridx = 1;
		gbc_textField_Search.gridy = 0;
		panel.add(textField_Search, gbc_textField_Search);
		textField_Search.setColumns(10);

		label_dilugMin = new JLabel("דילוג מינימום");
		label_dilugMin.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_label_dilugMin = new GridBagConstraints();
		gbc_label_dilugMin.anchor = GridBagConstraints.EAST;
		gbc_label_dilugMin.insets = new Insets(0, 0, 5, 5);
		gbc_label_dilugMin.gridx = 0;
		gbc_label_dilugMin.gridy = 1;
		panel.add(label_dilugMin, gbc_label_dilugMin);
		
		textField_dilugMin = new JTextField();
		textField_dilugMin.setMinimumSize(new Dimension(150, 25));
		textField_dilugMin.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		textField_dilugMin.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_textField_dilugMin = new GridBagConstraints();
		gbc_textField_dilugMin.anchor = GridBagConstraints.EAST;
		gbc_textField_dilugMin.insets = new Insets(0, 0, 5, 0);
		gbc_textField_dilugMin.gridx = 1;
		gbc_textField_dilugMin.gridy = 1;
		panel.add(textField_dilugMin, gbc_textField_dilugMin);
		textField_dilugMin.setColumns(10);
		
		label_dilugMax = new JLabel("דילוג מקסימום");
		label_dilugMax.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_label_dilugMax = new GridBagConstraints();
		gbc_label_dilugMax.anchor = GridBagConstraints.EAST;
		gbc_label_dilugMax.insets = new Insets(0, 0, 5, 5);
		gbc_label_dilugMax.gridx = 0;
		gbc_label_dilugMax.gridy = 2;
		panel.add(label_dilugMax, gbc_label_dilugMax);

		textField_dilugMax = new JTextField();
		textField_dilugMax.setMinimumSize(new Dimension(150, 25));
		textField_dilugMax.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		textField_dilugMax.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_dilugMax.setColumns(10);
		GridBagConstraints gbc_textField_dilugMax = new GridBagConstraints();
		gbc_textField_dilugMax.anchor = GridBagConstraints.EAST;
		gbc_textField_dilugMax.insets = new Insets(0, 0, 5, 0);
		gbc_textField_dilugMax.gridx = 1;
		gbc_textField_dilugMax.gridy = 2;
		panel.add(textField_dilugMax, gbc_textField_dilugMax);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 1;
		gbc_button.gridy = 16;

		popupMenu = new JPopupMenu();
		popupMenu.setLabel("");
		// Then on your component(s)

		textPane = new JTextPane();
		textPane.setFont(new Font("Miriam CLM", Font.BOLD, 18));
		textPane.setEditable(false);
		scrollPane = new JScrollPane(textPane);
		panelWidth = scrollPane.getWidth();
		textPane.setBackground(new java.awt.Color(251, 255, 253));
		textPane.addMouseListener(new PopClickListener());
		// textPane.setContentType( "text/html" );
		Color color1 = new java.awt.Color(240, 240, 255);
		panel.setBackground(color1);
		GroupLayout groupLayout = new GroupLayout(internalFrame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGap(0, 1598, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGap(0, 38, Short.MAX_VALUE));
		internalFrame.getContentPane().setLayout(groupLayout);

		textPane.setEditorKit(kit);
		textPane.setDocument(doc);

		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		internalFrame.setVisible(true);

		button = new JButton("חפש");
		button.setFont(new Font("Miriam Mono CLM", Font.BOLD, 18));
		comboBox_sub = new JComboBox();
		comboBox_sub.setAlignmentX(Component.RIGHT_ALIGNMENT);
		comboBox_sub.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		comboBox_sub.setModel(new DefaultComboBoxModel(new String[] { "אותיות", "מילים", "פסוקים" }));
		GridBagConstraints gbc_comboBox_sub = new GridBagConstraints();
		gbc_comboBox_sub.anchor = GridBagConstraints.EAST;
		gbc_comboBox_sub.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_sub.gridx = 0;
		gbc_comboBox_sub.gridy = 3;
		panel.add(comboBox_sub, gbc_comboBox_sub);
		comboBox_sub.setBackground(color1);

		checkBox_wholeWord = new JCheckBox("מילים שלמות");
		checkBox_wholeWord.setAlignmentX(Component.RIGHT_ALIGNMENT);
		checkBox_wholeWord.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_checkBox_wholeWord = new GridBagConstraints();
		gbc_checkBox_wholeWord.anchor = GridBagConstraints.EAST;
		gbc_checkBox_wholeWord.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_wholeWord.gridx = 0;
		gbc_checkBox_wholeWord.gridy = 4;
		panel.add(checkBox_wholeWord, gbc_checkBox_wholeWord);
		checkBox_wholeWord.setBackground(color1);

		checkBox_gimatriaSofiot = new JCheckBox(checkBox_gimatriaSofiot_text);
		checkBox_gimatriaSofiot.setAlignmentX(Component.RIGHT_ALIGNMENT);
		checkBox_gimatriaSofiot.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_checkBox_gimatriaSofiot = new GridBagConstraints();
		gbc_checkBox_gimatriaSofiot.anchor = GridBagConstraints.EAST;
		gbc_checkBox_gimatriaSofiot.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_gimatriaSofiot.gridx = 0;
		gbc_checkBox_gimatriaSofiot.gridy = 5;
		panel.add(checkBox_gimatriaSofiot, gbc_checkBox_gimatriaSofiot);
		checkBox_gimatriaSofiot.setBackground(color1);

		checkBox_countPsukim = new JCheckBox(checkBox_countPsukim_true);
		checkBox_countPsukim.setSelected(true);
		checkBox_countPsukim.setAlignmentX(Component.RIGHT_ALIGNMENT);
		checkBox_countPsukim.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_checkBox_countPsukim = new GridBagConstraints();
		gbc_checkBox_countPsukim.anchor = GridBagConstraints.EAST;
		gbc_checkBox_countPsukim.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_countPsukim.gridx = 0;
		gbc_checkBox_countPsukim.gridy = 6;
		panel.add(checkBox_countPsukim, gbc_checkBox_countPsukim);
		checkBox_countPsukim.setBackground(color1);
		checkBox_advancedOptions = new JCheckBox("אפשרויות מתקדמות");
		checkBox_advancedOptions.setSelected(true);
		checkBox_advancedOptions.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		checkBox_advancedOptions.setBackground(new Color(240, 240, 255));
		checkBox_advancedOptions.setAlignmentX(1.0f);
		GridBagConstraints gbc_checkBox_advancedOptions = new GridBagConstraints();
		gbc_checkBox_advancedOptions.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_advancedOptions.gridx = 0;
		gbc_checkBox_advancedOptions.gridy = 7;
		panel.add(checkBox_advancedOptions, gbc_checkBox_advancedOptions);
		label_padding = new JLabel("מספר אותיות");
		label_padding.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_label_padding = new GridBagConstraints();
		gbc_label_padding.anchor = GridBagConstraints.EAST;
		gbc_label_padding.insets = new Insets(0, 0, 5, 5);
		gbc_label_padding.gridx = 0;
		gbc_label_padding.gridy = 8;
		panel.add(label_padding, gbc_label_padding);
		textField_padding = new JTextField();
		textField_padding.setText((String) null);
		textField_padding.setMinimumSize(new Dimension(150, 25));
		textField_padding.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_padding.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		textField_padding.setColumns(10);
		GridBagConstraints gbc_textField_padding = new GridBagConstraints();
		gbc_textField_padding.anchor = GridBagConstraints.EAST;
		gbc_textField_padding.insets = new Insets(0, 0, 5, 0);
		gbc_textField_padding.gridx = 1;
		gbc_textField_padding.gridy = 8;
		panel.add(textField_padding, gbc_textField_padding);

		label_offset = new JLabel("קיזוז");
		label_offset.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_label_offset = new GridBagConstraints();
		gbc_label_offset.anchor = GridBagConstraints.EAST;
		gbc_label_offset.insets = new Insets(0, 0, 5, 5);
		gbc_label_offset.gridx = 0;
		gbc_label_offset.gridy = 9;
		panel.add(label_offset, gbc_label_offset);

		textField_offset = new JTextField();
		textField_offset.setText((String) null);
		textField_offset.setMinimumSize(new Dimension(150, 25));
		textField_offset.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_offset.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		textField_offset.setColumns(10);
		GridBagConstraints gbc_textField_offset = new GridBagConstraints();
		gbc_textField_offset.anchor = GridBagConstraints.EAST;
		gbc_textField_offset.insets = new Insets(0, 0, 5, 0);
		gbc_textField_offset.gridx = 1;
		gbc_textField_offset.gridy = 9;
		panel.add(textField_offset, gbc_textField_offset);

		progressBar = new JProgressBar();
		progressBar.setMinimumSize(new Dimension(150, 30));
		progressBar.setVisible(false);
		progressBar.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		progressBar.setStringPainted(true);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.gridheight = 2;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 14;
		panel.add(progressBar, gbc_progressBar);

		label_countMatch = new JLabel("dilug progress");
		label_countMatch.setVisible(false);

		label_dProgress = new JLabel("dilug progress");
		label_dProgress.setVisible(false);
		label_dProgress.setFont(new Font("Miriam Mono CLM", Font.BOLD, 16));
		GridBagConstraints gbc_label_dProgress = new GridBagConstraints();
		gbc_label_dProgress.anchor = GridBagConstraints.EAST;
		gbc_label_dProgress.insets = new Insets(0, 0, 5, 0);
		gbc_label_dProgress.gridx = 1;
		gbc_label_dProgress.gridy = 14;
		panel.add(label_dProgress, gbc_label_dProgress);
		label_countMatch.setFont(new Font("Miriam Mono CLM", Font.BOLD, 12));
		GridBagConstraints gbc_label_dCountMatch = new GridBagConstraints();
		gbc_label_dCountMatch.anchor = GridBagConstraints.EAST;
		gbc_label_dCountMatch.insets = new Insets(0, 0, 5, 0);
		gbc_label_dCountMatch.gridx = 1;
		gbc_label_dCountMatch.gridy = 15;
		panel.add(label_countMatch, gbc_label_dCountMatch);

		panel.add(button, gbc_button);
		// Button to save settings
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveValues();
			}
		});
		// Button to start search
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// button.setEnabled(false);
				if (!methodRunning) {
					methodRunning = true;
					button.setText(buttonCancelText);
					textPane.setText("");
					activity = SwingActivity.getInstance();
					activity.execute();
				} else {
					methodCancelRequest = true;
					button.setText(buttonCancelRequestText);
				}
			}
		});
		// Change countPsukim checkbox text when changing selected state
		checkBox_countPsukim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JCheckBox cb = (JCheckBox) event.getSource();
				cb.setText(((cb.isSelected()) ? checkBox_countPsukim_true : checkBox_countPsukim_false));
			}
		});
		// Change frame layout depending on comboBox choice
		comboBox_main.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox) e.getSource();
				Object selected = comboBox.getSelectedItem();
				changeLayout(selected.toString());
			}
		});
		// Setup Method Array
		// and Create Table for Torah Lookup
		ToraApp.starter();
		// Retrieve saved values for frame components
		initValues();
		changeLayout(comboBox_main.getSelectedItem().toString());
	}
}
