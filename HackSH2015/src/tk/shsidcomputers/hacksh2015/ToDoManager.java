/**
 * Copyright (C) 2015 SHSIDComputerClub
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tk.shsidcomputers.hacksh2015;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author SHSIDComputerClub
 *
 */
public final class ToDoManager {
	private final Storage current, archived;
	private Thread storer;
	private Date currentDate;
	private final DefaultListModel<Item> dueItemList, onGoingItemList;
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
	
	private class ItemSelectHandler implements ListSelectionListener {			
		private JList<Item> thisList, otherList;
		
		ItemSelectHandler(JList<Item> otherList) {
			super();
			this.otherList = otherList;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void valueChanged(ListSelectionEvent e) {
			thisList = (JList<Item>)e.getSource();
			if (thisList.isSelectionEmpty()) {
				resetComponentsForDeselect();
			} else {
				otherList.clearSelection();
				resetComponentsForSelected(thisList.getSelectedValue());
			}
		}
	}
	
	private final JFrame frame;
	private final JButton btnBack, btnTmrw, btnArchive, btnCheck, btnEdit, btnAdd;
	private final JLabel lblDue, lblOnGoing, lblToday, lblDate, lblDetailsTitle;
	private final JScrollPane scrlDetailsText, scrlDue, scrlOnGoing;
	private final JTextArea txtDetailsText;
	private final JList<Item> listDue, listOnGoing;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
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
					
					ToDoManager mainWindow = new ToDoManager(current, archived);
					mainWindow.frame.setVisible(true);
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
		
		dueItemList = new DefaultListModel<>();
		onGoingItemList = new DefaultListModel<>();
		reloadItemLists();
		
		frame = new JFrame("Done!");
		frame.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 18));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					current.store();
					archived.store();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				frame.dispose();
				if (storer != null)
					storer.interrupt();
				System.exit(0);
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 600, 608);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnBack = new JButton("<<");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentDate = ItemListProcessor.getTodayDate();
				reloadItemLists();
				resetComponentsForDate();
				resetComponentsForDeselect();
			}
		});
		btnBack.setBounds(30, 33, 70, 45);
		btnBack.setFont(new Font("Dialog", Font.PLAIN, 18));
		frame.getContentPane().add(btnBack);
		
		btnTmrw = new JButton(">");
		btnTmrw.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentDate = ItemListProcessor.getNextDate(currentDate);
				reloadItemLists();
				resetComponentsForDate();
				resetComponentsForDeselect();
			}
		});
		btnTmrw.setBounds(490, 33, 70, 45);
		btnTmrw.setFont(new Font("Dialog", Font.PLAIN, 18));
		frame.getContentPane().add(btnTmrw);
		
		btnArchive = new JButton("DoneList");
		btnArchive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Archive a = new Archive(archived);
				a.setVisible(true);
			}
		});
		btnArchive.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnArchive.setBounds(330, 113, 135, 55);
		frame.getContentPane().add(btnArchive);
		
		btnCheck = new JButton("\u2713");
		btnCheck.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				current.remove(currentSelected);
				archived.add(currentSelected);
				dueItemList.removeElement(currentSelected);
				onGoingItemList.removeElement(currentSelected);
				reloadItemLists();
				resetComponentsForDeselect();
			}
		});
		btnCheck.setEnabled(false);
		btnCheck.setFont(new Font("Dialog", Font.PLAIN, 25));
		btnCheck.setBounds(131, 113, 63, 55);
		frame.getContentPane().add(btnCheck);
		
		btnEdit = new JButton("Edit");
		btnEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Item beingChanged = currentSelected;
				Input input = new Input(beingChanged);
				input.setVisible(true);
				Item toAdd;
				try {
					toAdd = input.getValue();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					return;
				}
				if (toAdd == null) return;
				current.remove(beingChanged);
				dueItemList.removeElement(beingChanged);
				onGoingItemList.removeElement(beingChanged);
				current.add(toAdd);
				reloadItemLists();
				resetComponentsForDeselect();
			}
		});
		btnEdit.setEnabled(false);
		btnEdit.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnEdit.setBounds(490, 269, 70, 55);
		frame.getContentPane().add(btnEdit);
		
		btnAdd = new JButton("+");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Input input = new Input();
				input.setVisible(true);
				Item toAdd;
				try {
					toAdd = input.getValue();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					return;
				}
				if (toAdd == null) return;
				current.add(toAdd);
				reloadItemLists();
				resetComponentsForDeselect();
			}
		});
		btnAdd.setFont(new Font("Dialog", Font.PLAIN, 25));
		btnAdd.setBounds(232, 113, 63, 55);
		frame.getContentPane().add(btnAdd);
		
		lblDue = new JLabel("Due:");
		lblDue.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblDue.setBounds(30, 216, 101, 30);
		frame.getContentPane().add(lblDue);
		
		lblOnGoing = new JLabel("Ongoing:");
		lblOnGoing.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblOnGoing.setBounds(12, 345, 119, 30);
		frame.getContentPane().add(lblOnGoing);
		
		lblToday = new JLabel("");
		lblToday.setBounds(183, 28, 107, 50);
		lblToday.setFont(new Font("Dialog", Font.PLAIN, 20));
		frame.getContentPane().add(lblToday);
		
		lblDate = new JLabel("");
		lblDate.setBounds(305, 35, 135, 37);
		lblDate.setFont(new Font("Dialog", Font.PLAIN, 20));
		frame.getContentPane().add(lblDate);
		
		resetComponentsForDate();
		
		lblDetailsTitle = new JLabel("");
		lblDetailsTitle.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblDetailsTitle.setBounds(40, 426, 107, 21);
		frame.getContentPane().add(lblDetailsTitle);
		
		scrlDetailsText = new JScrollPane();
		scrlDetailsText.setBorder(null);
		scrlDetailsText.setBounds(40, 454, 425, 99);
		frame.getContentPane().add(scrlDetailsText);
		
		txtDetailsText = new JTextArea();
		txtDetailsText.setEditable(false);
		txtDetailsText.setFont(new Font("Dialog", Font.PLAIN, 18));
		txtDetailsText.setBackground(UIManager.getColor("InternalFrame.minimizeIconBackground"));
		scrlDetailsText.setViewportView(txtDetailsText);
		
		scrlDue = new JScrollPane();
		scrlDue.setBounds(131, 183, 334, 99);
		frame.getContentPane().add(scrlDue);
		
		listDue = new JList<>(dueItemList);
		listDue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDue.setFont(new Font("Dialog", Font.PLAIN, 18));
		scrlDue.setViewportView(listDue);
		
		scrlOnGoing = new JScrollPane();
		scrlOnGoing.setBounds(131, 312, 334, 99);
		frame.getContentPane().add(scrlOnGoing);
		
		listOnGoing = new JList<>(onGoingItemList);
		listOnGoing.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOnGoing.setFont(new Font("Dialog", Font.PLAIN, 18));
		scrlOnGoing.setViewportView(listOnGoing);
		
		listDue.addListSelectionListener(new ItemSelectHandler(listOnGoing));
		listOnGoing.addListSelectionListener(new ItemSelectHandler(listDue));
				
		storer = new Thread(new Storer());
		storer.start();
	}
	
	private void reloadItemLists() {
		dueItemList.clear();
		for (Item toAdd: ItemListProcessor.getDueTomorrow(currentDate, current)) {
			dueItemList.addElement(toAdd);
		}
		onGoingItemList.clear();
		for (Item toAdd: ItemListProcessor.getOnGoing(currentDate, current)) {
			onGoingItemList.addElement(toAdd);
		}
	}

	private void resetComponentsForSelected(Item newSelect) {
		currentSelected = newSelect;
		btnCheck.setEnabled(true);
		btnEdit.setEnabled(true);
		lblDetailsTitle.setText("Details:");
		txtDetailsText.setText(currentSelected.getDetails());
	}
	
	private void resetComponentsForDeselect() {
		currentSelected = null;
		btnCheck.setEnabled(false);
		btnEdit.setEnabled(false);
		lblDetailsTitle.setText("");
		txtDetailsText.setText("");
	}
	
	private void resetComponentsForDate() {
		int diff = ItemListProcessor.dateDifference(currentDate, ItemListProcessor.getTodayDate());
		if (diff == 0) lblToday.setText("Today");
		else if (diff == 1) lblToday.setText("Tomorrow");
		else lblToday.setText("Future");
		int[] YMD = ItemListProcessor.getYMD(currentDate);
		DecimalFormat fmt = new DecimalFormat("00");
		lblDate.setText(YMD[0] + "/" + fmt.format(YMD[1]) + "/" + fmt.format(YMD[2]));
	}
	
}
