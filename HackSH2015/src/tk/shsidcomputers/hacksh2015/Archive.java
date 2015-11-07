package tk.shsidcomputers.hacksh2015;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Archive extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Archive frame = new Archive();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Archive() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 129, 345, 277);
		contentPane.add(scrollPane);
		
		JList list = new JList();
		list.setFont(new Font("Dialog", Font.PLAIN, 18));
		scrollPane.setViewportView(list);
		
		JLabel lblArchive = new JLabel("Archive");
		lblArchive.setHorizontalAlignment(SwingConstants.CENTER);
		lblArchive.setFont(new Font("Dialog", Font.PLAIN, 30));
		lblArchive.setBounds(155, 18, 115, 39);
		contentPane.add(lblArchive);
		
		JButton btnClear = new JButton("Clear All");
		btnClear.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnClear.setBounds(42, 69, 123, 45);
		contentPane.add(btnClear);
	}
}
