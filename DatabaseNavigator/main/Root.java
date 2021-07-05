package main;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mysql.cj.jdbc.result.ResultSetImpl;

import net.proteanit.sql.DbUtils;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Root extends JFrame {

	private JPanel contentPane;
	private JComboBox<String> serverlist;
	private JButton showdb, connect, showtables, reload;
	JList<String> list, tlist;
	String dbserver, msg, item;
	Vector<String> dbs, tbs = new Vector<String>();
	private JScrollPane scrollPane_2;
	private JTable table;
	private JButton viewt;

	public static void main(String[] args) throws IOException {
		Root frame = new Root();
		frame.setVisible(true);

	}

	/**
	 * @throws IOException
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Root() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Root.class.getResource("/assets/icon.png")));
		setTitle("Database Navigator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1110, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lbl1 = new JLabel("Database Server");
		lbl1.setLabelFor(lbl1);
		lbl1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		lbl1.setBounds(37, 22, 170, 35);
		contentPane.add(lbl1);

		serverlist = new JComboBox();
		serverlist.setBounds(10, 71, 197, 35);
		serverlist.addItem("Select Server");
		serverlist.addItem("MySQL");
		serverlist.addItem("MongoDB");
		serverlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbserver = (String) serverlist.getSelectedItem();
				connect.setEnabled(true);
			}
		});
		contentPane.add(serverlist);

		connect = new JButton("Connect");
		connect.setEnabled(false);
		connect.setIcon(new ImageIcon(Root.class.getResource("/assets/link.png")));
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int i = Main.Connect(dbserver);
					if (i == 1) {
						JOptionPane.showMessageDialog(new JFrame(), "Connection Successful.");
						showdb.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "Connection Failed!!!!");
					}
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}

			}
		});
		connect.setBounds(252, 71, 112, 35);
		contentPane.add(connect);

		showdb = new JButton("Show Databases");
		showdb.setEnabled(false);
		showdb.setIcon(new ImageIcon(Root.class.getResource("/assets/connect.png")));
		showdb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dbs = Main.showDB();
					if (dbs.isEmpty()) {
						JOptionPane.showMessageDialog(new JFrame(), "No Databases Found!!!");
					} else {
						list.setListData(dbs);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		showdb.setBounds(10, 160, 170, 35);
		contentPane.add(showdb);

		showtables = new JButton("Show Tables");
		showtables.setEnabled(false);
		showtables.setIcon(new ImageIcon(Root.class.getResource("/assets/table.png")));
		showtables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tbs = Main.showTB(item);
					if (tbs.isEmpty()) {
						JOptionPane.showMessageDialog(new JFrame(), "No Tables Found!!!");
					} else {
						tlist.setListData(tbs);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		showtables.setBounds(228, 160, 170, 35);
		contentPane.add(showtables);

		list = new JList();
		list.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setEnabled(false);
		scrollPane.setBounds(10, 206, 170, 187);
		contentPane.add(scrollPane);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				item = list.getSelectedValue();
				showtables.setEnabled(true);
			}

		});
		list.setBounds(443, 171, 141, 45);
		tlist = new JList();
		tlist.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		JScrollPane scrollPane_1 = new JScrollPane(tlist);
		scrollPane_1.setEnabled(false);
		scrollPane_1.setBounds(228, 206, 170, 187);
		contentPane.add(scrollPane_1);
		tlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				item = tlist.getSelectedValue();
				viewt.setEnabled(true);
			}

		});

		table = new JTable();
		table.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		table.setFillsViewportHeight(true);
		scrollPane_2 = new JScrollPane(table);
		scrollPane_2.setBounds(454, 56, 630, 337);
		contentPane.add(scrollPane_2);

		viewt = new JButton("View Table");
		viewt.setEnabled(false);
		viewt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reload.setEnabled(true);

				try {
					ResultSet rs = Main.viewTable(item);
					int rowcount = (int) ((ResultSetImpl) rs).getUpdateCount();
					if (rowcount > 1) {
						table.setModel(DbUtils.resultSetToTableModel(rs));
						rs.close();
					} else {
						table.setModel(DbUtils.resultSetToTableModel(rs));
						rs.close();
						JOptionPane.showMessageDialog(new JFrame(), "Table is Empty!!!");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});
		viewt.setIcon(new ImageIcon(Root.class.getResource("/assets/view.png")));
		viewt.setBounds(454, 10, 134, 35);
		contentPane.add(viewt);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Root.class.getResource("/assets/server.png")));
		lblNewLabel.setBounds(10, 22, 30, 35);
		contentPane.add(lblNewLabel);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.DARK_GRAY);
		separator.setBounds(0, 135, 442, 2);
		contentPane.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.DARK_GRAY);
		separator_1.setBounds(442, 0, 12, 403);
		contentPane.add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.DARK_GRAY);
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(206, 207, 1, 186);
		contentPane.add(separator_2);

		JLabel license = new JLabel("@Deepak Chaudhari");
		license.setHorizontalAlignment(SwingConstants.RIGHT);
		license.setForeground(Color.BLACK);
		license.setBackground(Color.DARK_GRAY);
		license.setBounds(10, 404, 1074, 25);
		contentPane.add(license);

		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.DARK_GRAY);
		separator_3.setBounds(0, 404, 1094, 2);
		contentPane.add(separator_3);

		reload = new JButton("");
		reload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ResultSet rs = Main.viewTable(item);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		reload.setToolTipText("Refresh");
		reload.setEnabled(false);
		reload.setIcon(new ImageIcon(Root.class.getResource("/assets/sync.png")));
		reload.setBounds(598, 10, 36, 35);
		contentPane.add(reload);

	}
}
