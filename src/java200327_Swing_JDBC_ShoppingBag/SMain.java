package java200327_Swing_JDBC_ShoppingBag;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class SMain extends JFrame {
	String header[] = { "상품", "수량", "단가", "합계" };

	JTabbedPane tabPane = new JTabbedPane();

	DefaultTableModel tableModel = new DefaultTableModel(null, header);
	JTable table = new JTable(tableModel);
	JScrollPane tableScroll = new JScrollPane(table);

	JPanel wholeTab = new JPanel();
	JPanel underTab = new JPanel();
	JPanel inputTab = new JPanel();
	JPanel lastTab = new JPanel();

	JTextField[] indata = new JTextField[3];
	String[] in = null;

	SDAO dao = SDAO.getInstance();
	ArrayList<String[]> objList = new ArrayList<>();

	int modIntRow = -1;

	String product;
	String amount;
	JTextField sumField = new JTextField();
	
	String total = ""; // 전체 금액 

	SMain() {
		Dimension size = new Dimension(500, 400);
		tableSetting();
		createInput();
		createTabbed();

		init();

		this.setLocation(150, 150);
		this.setSize(size);
		this.add(tabPane);

		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
	}

	private void init() {
		objList = dao.getList();
		for (int i = 0; i < objList.size(); i++) {
			tableModel.addRow(objList.get(i));
		}
		total(); // 전체 금액
	}

	private void createTabbed() {
		wholeTab.setLayout(new BorderLayout());
		wholeTab.add(tableScroll, "Center");
		wholeTab.add(underTab, "South");
		tabPane.add("장바구니", wholeTab);
	}

	private void createInput() {
		underTab.setLayout(new BorderLayout());
		inputTab.setLayout(new BoxLayout(inputTab, BoxLayout.X_AXIS));
		lastTab.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 오른쪽 정렬

		for (int i = 0; i < indata.length; i++) {
			inputTab.add(indata[i] = new JTextField());
		}

		JButton addB = new JButton("등록");
		inputTab.add(addB);
		addB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				in = new String[4];
				for (int i = 0; i < indata.length; i++) {
					in[i] = indata[i].getText();
					indata[i].setText("");
				}
				in[3] = sum(); // 합계 계산
				tableModel.addRow(in);
				total(); // 전체 금액 계산
			}
		});

		JButton modB = new JButton("수정");
		inputTab.add(modB);
		modB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (modIntRow != -1) {
					in = new String[4];
					for (int i = 0; i < indata.length; i++) {
						in[i] = indata[i].getText();
						indata[i].setText("");
					}
					in[3] = sum(); // 합계 계산

					delTableRow(modIntRow);
					tableModel.insertRow(modIntRow, in);
					modIntRow = -1;
					total(); // 전체 금액 계산
				}
			}
		});

		JButton delB = new JButton("삭제");
		inputTab.add(delB);
		delB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (modIntRow != -1) {
					in = new String[4];
					for (int i = 0; i < indata.length; i++) {
						in[i] = indata[i].getText();
						indata[i].setText("");
					}
					delTableRow(modIntRow);
					modIntRow = -1;
					total(); // 전체 금액 계산
				}
			}
		});

		JButton saveB = new JButton("저장"); /////
		inputTab.add(saveB);
		saveB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<SDTO> saveAll = new ArrayList<>(); /////

				for (int i = 0; i < tableModel.getRowCount(); i++) {
					SDTO newDTO = new SDTO();
					newDTO.setProduct((String) tableModel.getValueAt(i, 0));
					newDTO.setAmount((String) tableModel.getValueAt(i, 1));
					newDTO.setUnitPrice((String) tableModel.getValueAt(i, 2));
					newDTO.setTotalPrice((String) tableModel.getValueAt(i, 3));
					saveAll.add(newDTO);
				}
				dao.saveDB(saveAll);
			}
		});

		JLabel paymentText = new JLabel("결제금액");
		lastTab.add(paymentText);
		sumField = new JTextField(10);
		sumField.setHorizontalAlignment(JTextField.RIGHT); // 오른쪽 정렬
		lastTab.add(sumField);

		JButton orderBtn = new JButton("주문");
		lastTab.add(orderBtn);
		orderBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<String[]> saveList = new ArrayList<>(); /////

				for (int i = 0; i < tableModel.getRowCount(); i++) {
					in = new String[4];
					in[0] = (String) tableModel.getValueAt(i, 0);
					in[1] = (String) tableModel.getValueAt(i, 1);
					in[2] = (String) tableModel.getValueAt(i, 2);
					in[3] = (String) tableModel.getValueAt(i, 3);
					saveList.add(in);
				}
				new SList(saveList,total);
				tableModel.setNumRows(0); // 주문 후 전체 삭제
				sumField.setText("");
			}
		});
		underTab.add(inputTab, "Center");
		underTab.add(lastTab, "South");
	}

	private void delTableRow(int row) {
		tableModel.removeRow(row);
	}

	private void tableSetting() {
		table.setRowMargin(0);
		table.getColumnModel().setColumnMargin(0);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setShowVerticalLines(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					modIntRow = table.getSelectedRow();
					for (int i = 0; i < indata.length; i++) {
						indata[i].setText((String) table.getValueAt(modIntRow, i));
					}
				}
				if (e.getClickCount() == 2) {
				}
			}
		});

		DefaultTableCellRenderer ts = new DefaultTableCellRenderer();
		ts.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tc = table.getColumnModel();
		for (int i = 0; i < tc.getColumnCount(); i++) {
			tc.getColumn(i).setCellRenderer(ts);
		}
	}

	// 합계 계산
	public String sum() {
		String sum = null;
		int a = 0;
		a = Integer.valueOf(in[1]) * Integer.valueOf(in[2]);
		sum = String.valueOf(a);
		return sum;
	}

	// 결제 금액 계산
	public void total() {
		String s = "";
		int sum = 0; // 합

		ArrayList<String> sList = new ArrayList<>(); /////

		for (int i = 0; i < tableModel.getRowCount(); i++) {
			s = (String) tableModel.getValueAt(i, 3);
			sList.add(s);
		}

		for (int i = 0; i < sList.size(); i++) {
			sum = sum + Integer.valueOf(sList.get(i));
		}

		total = String.valueOf(sum);
		sumField.setText(total);
	}

	public static void main(String[] args) {
		new SMain();
	}

}
