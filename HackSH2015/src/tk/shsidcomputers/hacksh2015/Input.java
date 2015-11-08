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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

/**
 * @author SHSIDComputerClub
 *
 */
public class Input extends JDialog {
	private static final long serialVersionUID = 3816330566947754829L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtTitle;
	private JTextField txtDueMonth;
	private JTextField txtDueYear;
	private JTextField txtDueDay;
	private JTextField txtStMonth;
	private JTextField txtStYear;
	private JTextField txtStDay;
	private JTextArea txtDesc;
	JComboBox<Priority> cmbPriority;
	private boolean stChanged = false;
	private Item value;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Input dialog = new Input();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Item getValue() throws InterruptedException {

		dispose();
		return value;
	}
	
	private void setValueAndExit(boolean b) {
		if (b) {
			String title = txtTitle.getText();
			String details = txtDesc.getText();
			Date dueDate = ItemListProcessor.getDayStartDate(
					Integer.parseInt(txtDueYear.getText()),
					Integer.parseInt(txtDueMonth.getText()),
					Integer.parseInt(txtDueDay.getText())
			);
			Date mustStartDate = ItemListProcessor.getDayStartDate(
					Integer.parseInt(txtStYear.getText()),
					Integer.parseInt(txtStMonth.getText()),
					Integer.parseInt(txtStDay.getText())
			);
			if (mustStartDate.equals(dueDate))
				mustStartDate = null;
			Priority priority = (Priority) cmbPriority.getSelectedItem();
			value = new Item(title, details, mustStartDate, dueDate,
					value == null ? false : value.isFinished(), priority);
		}
		setVisible(false);
	}
	
	/**
	 * Create the dialog.
	 */
	public Input(Item value) {
		this();
		this.value = value;
		txtTitle.setText(value.getTitle());
		txtDesc.setText(value.getDetails());
		int[] dueDate = ItemListProcessor.getYMD(value.getDue());
		txtDueYear.setText(Integer.toString(dueDate[0]));
		txtDueMonth.setText(Integer.toString(dueDate[1]));
		txtDueDay.setText(Integer.toString(dueDate[2]));
		int[] mustStartDate = ItemListProcessor.getYMD(
				value.hasStartDate() ? value.getStartDate() : value.getDue());
		txtStYear.setText(Integer.toString(mustStartDate[0]));
		txtStMonth.setText(Integer.toString(mustStartDate[1]));
		txtStDay.setText(Integer.toString(mustStartDate[2]));		
		cmbPriority.setSelectedItem(value.getPriority());
	}
	
	public Input() {
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setValueAndExit(false);
			}
		});
		setTitle("New Item...");
		int[] tomorrow = ItemListProcessor.getYMD(
				ItemListProcessor.getNextDate(ItemListProcessor.getTodayDate()));
		setBounds(100, 100, 450, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel label = new JLabel("Parameters");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(new Font("Dialog", Font.PLAIN, 22));
			label.setBounds(145, 15, 145, 31);
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel(" Title");
			label.setFont(new Font("Dialog", Font.PLAIN, 18));
			label.setBounds(45, 61, 81, 21);
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel(" Priority");
			label.setFont(new Font("Dialog", Font.PLAIN, 18));
			label.setBounds(249, 61, 81, 21);
			contentPanel.add(label);
		}
		{
			txtTitle = new JTextField();
			txtTitle.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtTitle.setColumns(10);
			txtTitle.setBounds(45, 81, 132, 37);
			contentPanel.add(txtTitle);
		}
		{
			JLabel label = new JLabel(" Due Date");
			label.setFont(new Font("Dialog", Font.PLAIN, 18));
			label.setBounds(45, 133, 138, 21);
			contentPanel.add(label);
		}
		{
			txtDueMonth = new JTextField();
			txtDueMonth.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (!stChanged)
						txtStMonth.setText(txtDueMonth.getText());
				}
			});
			txtDueMonth.setToolTipText("Month");
			txtDueMonth.setHorizontalAlignment(SwingConstants.CENTER);
			txtDueMonth.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtDueMonth.setText(Integer.toString(tomorrow[1]));
			txtDueMonth.setColumns(10);
			txtDueMonth.setBounds(45, 153, 30, 37);
			contentPanel.add(txtDueMonth);
		}
		{
			txtDueYear = new JTextField();
			txtDueYear.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (!stChanged)
						txtStYear.setText(txtDueYear.getText());
				}
			});
			txtDueYear.setToolTipText("Year");
			txtDueYear.setText(Integer.toString(tomorrow[0]));
			txtDueYear.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtDueYear.setColumns(10);
			txtDueYear.setBounds(135, 153, 48, 37);
			contentPanel.add(txtDueYear);
		}
		{
			JLabel lblNewLabel = new JLabel("/");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(77, 161, 9, 21);
			contentPanel.add(lblNewLabel);
		}
		{
			txtDueDay = new JTextField();
			txtDueDay.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (!stChanged)
						txtStDay.setText(txtDueDay.getText());
				}
			});
			txtDueDay.setToolTipText("Day");
			txtDueDay.setText(Integer.toString(tomorrow[2]));
			txtDueDay.setHorizontalAlignment(SwingConstants.CENTER);
			txtDueDay.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtDueDay.setColumns(10);
			txtDueDay.setBounds(90, 153, 30, 37);
			contentPanel.add(txtDueDay);
		}
		{
			JLabel label = new JLabel("/");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(122, 161, 9, 21);
			contentPanel.add(label);
		}
		{
			JLabel lblStartDate = new JLabel("Opt: Start Date");
			lblStartDate.setFont(new Font("Dialog", Font.PLAIN, 18));
			lblStartDate.setBounds(249, 133, 136, 21);
			contentPanel.add(lblStartDate);
		}
		{
			txtStMonth = new JTextField();
			txtStMonth.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					stChanged = true;
				}
			});
			txtStMonth.setToolTipText("Month");
			txtStMonth.setText(Integer.toString(tomorrow[1]));
			txtStMonth.setHorizontalAlignment(SwingConstants.CENTER);
			txtStMonth.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtStMonth.setColumns(10);
			txtStMonth.setBounds(249, 153, 30, 37);
			contentPanel.add(txtStMonth);
		}
		{
			txtStYear = new JTextField();
			txtStYear.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					stChanged = true;
				}
			});
			txtStYear.setToolTipText("Year");
			txtStYear.setText(Integer.toString(tomorrow[0]));
			txtStYear.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtStYear.setColumns(10);
			txtStYear.setBounds(339, 153, 48, 37);
			contentPanel.add(txtStYear);
		}
		{
			JLabel label = new JLabel("/");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(281, 161, 9, 21);
			contentPanel.add(label);
		}
		{
			txtStDay = new JTextField();
			txtStDay.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					stChanged = true;
				}
			});
			txtStDay.setToolTipText("Day");
			txtStDay.setText(Integer.toString(tomorrow[2]));
			txtStDay.setHorizontalAlignment(SwingConstants.CENTER);
			txtStDay.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtStDay.setColumns(10);
			txtStDay.setBounds(294, 153, 30, 37);
			contentPanel.add(txtStDay);
		}
		{
			JLabel label = new JLabel("/");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(326, 161, 9, 21);
			contentPanel.add(label);
		}
		{
			JLabel lblNewLabel_1 = new JLabel(" Description");
			lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 18));
			lblNewLabel_1.setBounds(45, 216, 127, 21);
			contentPanel.add(lblNewLabel_1);
		}
		
		cmbPriority = new JComboBox<Priority>();
		cmbPriority.setToolTipText("Select priority");
		cmbPriority.setModel(new DefaultComboBoxModel<Priority>(Priority.values()));
		cmbPriority.setSelectedIndex(1);
		cmbPriority.setFont(new Font("Dialog", Font.PLAIN, 18));
		cmbPriority.setBounds(249, 84, 138, 31);
		contentPanel.add(cmbPriority);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(45, 250, 340, 120);
			contentPanel.add(scrollPane);
			{
				txtDesc = new JTextArea();
				scrollPane.setViewportView(txtDesc);
				txtDesc.setFont(new Font("Dialog", Font.PLAIN, 18));
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						setValueAndExit(true);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						setValueAndExit(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			
		}
	}
}
