package tk.shsidcomputers.hacksh2015;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class ToDoManager {

	private JFrame frame;
	private Storage current;
	private Storage archived;
	private Thread storer;
	
	private class Storer implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					current.store();
					archived.store();
					Thread.sleep(120000);
				} catch (IOException e) {
					int n = JOptionPane.showOptionDialog(frame,
						    "Storing file failed.\n"
						    + "Abort: Will kill this application.\n"
						    + "Retry: Will retry storing file."
						    + "Ignore: Continue running application without storing.\n"
						    + "Error:" + e,
						    "Storing file failed",
						    JOptionPane.YES_NO_CANCEL_OPTION,
						    JOptionPane.ERROR_MESSAGE,
						    null,
						    new String[] {"Abort", "Retry", "Ignore"},
						    JOptionPane.NO_OPTION);
					switch (n) {
						case JOptionPane.YES_OPTION:
							current.stopStoring();
							archived.stopStoring();
							storer = null;
							frame.dispose();
							break;
						case JOptionPane.NO_OPTION:
							break;
						case JOptionPane.CANCEL_OPTION:
							storer = null;
							return;
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File current = new File("current.json");
					if (! current.exists()) {
						int n = JOptionPane.showConfirmDialog(
							    null,
							    "Data file current.json is missing. Would you like to create an empty file?",
							    "Missing current.json",
							    JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							current.createNewFile();
						}
					}
					File archived = new File("archived.json");
					if (! archived.exists()) {
						int n = JOptionPane.showConfirmDialog(
							    null,
							    "Data file archived.json is missing. Would you like to create an empty file?",
							    "Missing archived.json",
							    JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							archived.createNewFile();
						}
					}
					
					ToDoManager window = new ToDoManager(current, archived);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public ToDoManager(File currentfile, File archivefile) throws IOException {
		current = new Storage(currentfile);
		archived = new Storage(archivefile);
		initialize();
		storer = new Thread(new Storer());
		storer.start();
	}

	private void close() {
		// frame.setVisible(false);
		try {
			current.store();
			archived.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.dispose();
		if (storer != null)
			storer.interrupt();
		System.exit(0);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		frame.setResizable(false);
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnBack = new JButton("< Back");
		btnBack.setBounds(30, 33, 89, 31);
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnBack);
		
		JLabel lblToday = new JLabel("Today");
		lblToday.setBounds(189, 30, 82, 37);
		lblToday.setFont(new Font("Tahoma", Font.PLAIN, 30));
		frame.getContentPane().add(lblToday);
		
		JLabel lblDate = new JLabel("--/--/----");
		lblDate.setBounds(311, 30, 110, 37);
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 30));
		frame.getContentPane().add(lblDate);
		
		JButton btnTmrw = new JButton(">");
		btnTmrw.setBounds(491, 33, 45, 31);
		btnTmrw.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(btnTmrw);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(111, 119, 334, 99);
		frame.getContentPane().add(scrollPane);
		
		JList listDue = new JList();
		scrollPane.setViewportView(listDue);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(111, 262, 334, 99);
		frame.getContentPane().add(scrollPane_1);
		
		JList listOnGoing = new JList();
		scrollPane_1.setViewportView(listOnGoing);
	}
}
