package java200327_Swing_JDBC_ShoppingBag;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class SList extends JFrame {
	String header[] = { "상품명", "단가", "수량", "금액" };
	
	DefaultTableModel tableModel = new DefaultTableModel(null, header);
	JTable table = new JTable(tableModel);
	JScrollPane tableScroll = new JScrollPane(table);
	
	JPanel wholeTab = new JPanel();
	JPanel timeTab = new JPanel();
	JPanel underTab = new JPanel();
	
	String[] in = new String[4];
	
	ArrayList<String[]> objList = new ArrayList<>();
	ArrayList<String[]> saveList = null;
	String total = ""; 
	
	
	SList(ArrayList<String[]> saveList, String total) {
		this.saveList = saveList;
		this.total = total;
		
		Dimension size = new Dimension(300,400);
		tableSetting();
		createInput();
		
		init();
		
		this.setLocation(300, 300);
		this.setSize(size);
		this.add(wholeTab);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
	}

	private void init() {
		System.out.println(saveList.size());
		objList = saveList;
		for (int i = 0; i < objList.size(); i++) {
			tableModel.addRow(objList.get(i));
		}
	}

	private void tableSetting() {
		table.setRowMargin(0);
		table.getColumnModel().setColumnMargin(0);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setShowVerticalLines(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		
		DefaultTableCellRenderer ts = new DefaultTableCellRenderer(); // 셀 안에 들어가는 데이터의 정렬을 조절
		ts.setHorizontalAlignment(SwingConstants.CENTER); // 셀에 출력될 데이터 정렬 지정
		TableColumnModel tc = table.getColumnModel();
		for (int i = 0; i < tc.getColumnCount(); i++) {
			tc.getColumn(i).setCellRenderer(ts);
		}
	}

	private void createInput() {
		wholeTab.setLayout(new BorderLayout());
		timeTab.setLayout(new FlowLayout(FlowLayout.LEFT));
		underTab.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm"+"("+"E"+")");
		Date time = new Date();
		JLabel timeLabel = new JLabel(format1.format(time));
		timeTab.add(timeLabel);
		
		JLabel totalText = new JLabel("합계");
		underTab.add(totalText);
		JTextField totalField = new JTextField(10);
		underTab.add(totalField);
		totalField.setHorizontalAlignment(JTextField.RIGHT); // 오른쪽 정렬
		totalField.setText(total);
		
		wholeTab.add(tableScroll, "Center");
		wholeTab.add(timeTab,"North");
		wholeTab.add(underTab, "South");
	}
	
}
