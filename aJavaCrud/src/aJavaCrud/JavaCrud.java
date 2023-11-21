package aJavaCrud;

import java.sql.*;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;




public class JavaCrud {

	private JFrame frame;
	private JTextField txtEdition;
	private JTextField txtBName;
	private JTextField txtPrice;
	private JTable table;
	private JTextField txtBookID;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaCrud window = new JavaCrud();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JavaCrud() {
		initialize();
		/*Connect();
		table_load();*/
	}
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	public void Connect()
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/mydb","Geo","$routa_3");
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	//for automatically populating records in the display table
	public void table_load()
	{
		try
		{
			pst = con.prepareStatement("select * from book");
			rs = pst.executeQuery();
			//
			loadTData(table);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	private void loadTData(JTable table) {
		try {
			ResultSetMetaData mdata = rs.getMetaData();
			int colCount = mdata.getColumnCount();
			
			DefaultTableModel dTmodel = new DefaultTableModel();
			for (int i = 1; i <= colCount; i++)
			{
				dTmodel.addColumn(mdata.getColumnName(i));
			}
			while (rs.next()) {
				Object[] rowData = new Object[colCount];
				for (int i = 1; i <= colCount; i++) {
					rowData[i - 1] = rs.getObject(i);
				}
				dTmodel.addRow(rowData);
			}
			table.setModel(dTmodel);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* Initialize the contents of the frame.
	*/
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 747, 505);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JLabel lblNewLabel = new JLabel("book Shop");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(197, 22, 171, 59);
		frame.getContentPane().add(lblNewLabel);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING,
		TitledBorder.TOP, null, null));
		panel.setBounds(38, 108, 305, 144);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		JLabel lblBName = new JLabel("book Name");
		lblBName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBName.setBounds(10, 22, 93, 26);
		panel.add(lblBName);
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPrice.setBounds(10, 96, 93, 26);
		panel.add(lblPrice);
		JLabel lblEdition = new JLabel("Edition");
		lblEdition.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEdition.setBounds(10, 59, 93, 26);
		panel.add(lblEdition);
		txtEdition = new JTextField();
		txtEdition.setBounds(113, 61, 162, 26);
		panel.add(txtEdition);
		txtEdition.setColumns(10);
		txtBName = new JTextField();
		txtBName.setColumns(10);
		txtBName.setBounds(113, 24, 162, 26);
		panel.add(txtBName);
		txtPrice = new JTextField();
		txtPrice.setColumns(10);
		txtPrice.setBounds(113, 101, 162, 26);
		panel.add(txtPrice);
		//Inserting records ---start here
		JButton btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			String bname,edition,price;
			bname = txtBName.getText();
			edition = txtEdition.getText();
			price = txtPrice.getText();
			try {
				pst = con.prepareStatement("insert into book(Name,Edition,Price)values(?,?,?)");
				pst.setString(1,bname);
				pst.setString(2,edition);
				pst.setString(3,price);
				pst.executeUpdate();
				JOptionPane.showMessageDialog(null, "Inserting Record "+txtBookID.getText()+"!");
				table_load(); //ONLY activate after completing the table scroll to automatically display added records
				txtBName.setText("");
				txtEdition.setText("");
				txtPrice.setText("");
				txtBName.requestFocus();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			}
		});
		btnInsert.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnInsert.setBounds(38, 263, 91, 37);
		frame.getContentPane().add(btnInsert);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(369, 108, 337, 187);
		frame.getContentPane().add(scrollPane_1);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane_1.setViewportView(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search BookID", TitledBorder.LEADING,
		TitledBorder.TOP, null, null));
		panel_1.setBounds(160, 310, 305, 65);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		//BookID to automatically refresh the registration fields after inserting Book ID in the Search box
		txtBookID = new JTextField();
		txtBookID.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					String id = txtBookID.getText();
					pst = con.prepareStatement("select Name,Edition,Price from book where ID = ?");
					pst.setString(1, id);
					ResultSet rs = pst.executeQuery();
					if(rs.next()==true)
					{
						String name = rs.getString(1);
						String edition = rs.getString(2);
						String price = rs.getString(3);
						txtBName.setText(name);
						txtEdition.setText(edition);
						txtPrice.setText(price);
					}
					else
					{
						txtBName.setText("");
						txtEdition.setText("");
						txtPrice.setText("");
						txtBookID.requestFocus();
					}
				}
				catch (SQLException ex) {
				}
			}
		});
		txtBookID.setColumns(10);
		txtBookID.setBounds(82, 28, 167, 26);
		panel_1.add(txtBookID);
		JLabel lblBookId = new JLabel("book ID");
		lblBookId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBookId.setBounds(10, 26, 93, 26);
		panel_1.add(lblBookId);
		//Update method
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bname,edition,price,bid;
				bname = txtBName.getText();
				edition = txtEdition.getText();
				price = txtPrice.getText();
				bid = txtBookID.getText();
				try {
					pst = con.prepareStatement("update book set Name=?,Edition=?,Price=? where ID =?");
					pst.setString(1, bname);
					pst.setString(2, edition);
					pst.setString(3, price);
					pst.setString(4, bid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Updating Record "+txtBookID.getText()+"!");
					table_load();
					txtBName.setText("");
					txtEdition.setText("");
					txtPrice.setText("");
					txtBookID.setText("");
					txtBookID.requestFocus();
				}
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUpdate.setBounds(139, 263, 89, 37);
		frame.getContentPane().add(btnUpdate);
		//Delete Method
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bid;
				bid = txtBookID.getText();
				try {
					pst = con.prepareStatement("delete from book where ID =?");
					pst.setString(1, bid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Deleting Record "+txtBookID.getText()+"!");
					table_load();
					txtBName.setText("");
					txtEdition.setText("");
					txtPrice.setText("");
					txtBookID.setText("");
					txtBName.requestFocus();
				}
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBounds(240, 263, 89, 37);
		frame.getContentPane().add(btnDelete);
		JLabel lblBookTableRecords = new JLabel("Book Table Records");
		lblBookTableRecords.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblBookTableRecords.setBounds(400, 58, 214, 48);
		frame.getContentPane().add(lblBookTableRecords);
	}

}
