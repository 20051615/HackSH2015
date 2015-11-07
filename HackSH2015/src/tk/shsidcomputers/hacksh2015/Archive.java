package tk.shsidcomputers.hacksh2015;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Archive extends JFrame {
	private static final long serialVersionUID = 5540165914513607110L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Archive(final Storage archived) {
		setTitle("View archive");
		final DefaultListModel<Item> archivedList = new DefaultListModel<>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 129, 345, 277);
		contentPane.add(scrollPane);
		
		JList<Item> list = new JList<Item>(archivedList);
		list.setFont(new Font("Dialog", Font.PLAIN, 18));
		scrollPane.setViewportView(list);
		
		JLabel lblArchive = new JLabel("Archive");
		lblArchive.setHorizontalAlignment(SwingConstants.CENTER);
		lblArchive.setFont(new Font("Dialog", Font.PLAIN, 30));
		lblArchive.setBounds(155, 18, 115, 39);
		contentPane.add(lblArchive);
		
		JButton btnClear = new JButton("Clear All");
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				archived.clear();
				archivedList.clear();
			}
		});
		btnClear.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnClear.setBounds(264, 72, 123, 45);
		contentPane.add(btnClear);
		
		MouseAdapter refresh = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				archivedList.clear();
				ArrayList<Item> archivedarray = new ArrayList<Item>(archived);
				Collections.sort(archivedarray);
				for (Item i : archivedarray) {
					archivedList.addElement(i);
				}
			}
		};
		
		JButton btnRefresh = new JButton("Refesh");
		btnRefresh.addMouseListener(refresh);
		btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnRefresh.setBounds(42, 72, 123, 45);
		contentPane.add(btnRefresh);
		
		refresh.mouseClicked(null);
	}
}
