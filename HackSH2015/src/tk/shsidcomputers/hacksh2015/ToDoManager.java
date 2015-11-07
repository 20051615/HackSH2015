package tk.shsidcomputers.hacksh2015;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToDoManager {

	private JFrame frame;
	private Storage current;
	private Storage archived;
	private Thread storer;
	private Date currentDate;
	private DefaultListModel<Item> dueItemList;
	private DefaultListModel<Item> onGoingItemList;
	private Item currentSelected;
	
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
		currentDate = ItemListProcessor.getTodayDate();
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
		dueItemList = new DefaultListModel<>();
		onGoingItemList = new DefaultListModel<>();
		reloadItemLists();
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 18));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		frame.setResizable(false);
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnBack = new JButton("<<");
		btnBack.setBounds(30, 33, 70, 45);
		btnBack.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnBack);
		
		JLabel lblToday = new JLabel("Today");
		lblToday.setBounds(183, 28, 107, 50);
		lblToday.setFont(new Font("Dialog", Font.PLAIN, 30));
		frame.getContentPane().add(lblToday);
		
		JLabel lblDate = new JLabel("--/--/----");
		lblDate.setBounds(305, 35, 110, 37);
		lblDate.setFont(new Font("Dialog", Font.PLAIN, 30));
		frame.getContentPane().add(lblDate);
		
		JButton btnTmrw = new JButton(">");
		btnTmrw.setBounds(490, 33, 70, 45);
		btnTmrw.setFont(new Font("Dialog", Font.PLAIN, 18));
		frame.getContentPane().add(btnTmrw);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(131, 183, 334, 99);
		frame.getContentPane().add(scrollPane);
		
		JList<Item> listDue = new JList<>(dueItemList);
		listDue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDue.setFont(new Font("Dialog", Font.PLAIN, 18));
		scrollPane.setViewportView(listDue);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(131, 312, 334, 99);
		frame.getContentPane().add(scrollPane_1);
		
		JList<Item> listOnGoing = new JList<>(onGoingItemList);
		listOnGoing.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOnGoing.setFont(new Font("Dialog", Font.PLAIN, 18));
		scrollPane_1.setViewportView(listOnGoing);
		
		JButton btnCheck = new JButton("\u2713");
		btnCheck.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentSelected != null) {
					current.remove(currentSelected);
					archived.add(currentSelected);
					dueItemList.removeElement(currentSelected);
					onGoingItemList.removeElement(currentSelected);
				}
			}
		});
		btnCheck.setEnabled(false);
		btnCheck.setFont(new Font("Dialog", Font.PLAIN, 40));
		btnCheck.setBounds(131, 113, 63, 55);
		frame.getContentPane().add(btnCheck);
		
		class SharedListSelectionHandler implements ListSelectionListener {			
			private JButton btnCheck;
			
			SharedListSelectionHandler(JButton btnCheck) {
				super();
				this.btnCheck = btnCheck;
			}
			@Override
			public void valueChanged(ListSelectionEvent e) {
				@SuppressWarnings("unchecked")
				JList<Item> source = (JList<Item>)e.getSource();
				if (source.getSelectedIndex() == -1) {
					btnCheck.setEnabled(false);
					currentSelected = null;
				} else {
					currentSelected = (Item)(source.getSelectedValue());
					btnCheck.setEnabled(true);
				}
			}
		}
		
		SharedListSelectionHandler selectHandler = new SharedListSelectionHandler(btnCheck);
		listDue.addListSelectionListener(selectHandler);
		listOnGoing.addListSelectionListener(selectHandler);
		
		JLabel lblDue = new JLabel("DUE:");
		lblDue.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblDue.setBounds(15, 216, 116, 30);
		frame.getContentPane().add(lblDue);
		
		JLabel lblOngoing = new JLabel("ONGOING:\r\n");
		lblOngoing.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblOngoing.setBounds(15, 345, 116, 30);
		frame.getContentPane().add(lblOngoing);
		
		JButton btnCompleted = new JButton("Completed");
		btnCompleted.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnCompleted.setBounds(342, 113, 123, 55);
		frame.getContentPane().add(btnCompleted);
		
		JButton btnAdd = new JButton("+");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Item toAdd = (new Input()).getValue();
					if (toAdd == null) return;
					current.add(toAdd);
					reloadItemLists();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					return;
				}
				
			}
		});
		btnAdd.setFont(new Font("Dialog", Font.PLAIN, 40));
		btnAdd.setBounds(232, 113, 63, 55);
		frame.getContentPane().add(btnAdd);
	}
	
	private void reloadItemLists() {
		dueItemList.clear();
		for (Item toAdd: ItemListProcessor.getDueTomorrow(currentDate, current)) {
			dueItemList.addElement(toAdd);
		}
		onGoingItemList.clear();
		for (Item toAdd: ItemListProcessor.getOnGoing(currentDate, current)) {
			dueItemList.addElement(toAdd);
		}
	}
}
