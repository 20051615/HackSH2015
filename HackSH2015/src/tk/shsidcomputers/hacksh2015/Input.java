package tk.shsidcomputers.hacksh2015;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Input extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtTitle;
	private JTextField txtDueMonth;
	private JTextField txtDueYear;
	private JTextField txtDueDay;
	private JTextField txtStMonth;
	private JTextField txtStYear;
	private JTextField txtStDay;
	private JTextField txtDesc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Input dialog = new Input();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Input() {
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
			label.setBounds(45, 133, 81, 21);
			contentPanel.add(label);
		}
		{
			txtDueMonth = new JTextField();
			txtDueMonth.setToolTipText("Month");
			txtDueMonth.setHorizontalAlignment(SwingConstants.CENTER);
			txtDueMonth.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtDueMonth.setText("12");
			txtDueMonth.setColumns(10);
			txtDueMonth.setBounds(45, 153, 30, 37);
			contentPanel.add(txtDueMonth);
		}
		{
			txtDueYear = new JTextField();
			txtDueYear.setToolTipText("Year");
			txtDueYear.setText("1999");
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
			txtDueDay.setToolTipText("Day");
			txtDueDay.setText("31");
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
			JLabel lblStartDate = new JLabel("Start Date");
			lblStartDate.setFont(new Font("Dialog", Font.PLAIN, 18));
			lblStartDate.setBounds(249, 133, 81, 21);
			contentPanel.add(lblStartDate);
		}
		{
			txtStMonth = new JTextField();
			txtStMonth.setToolTipText("Month");
			txtStMonth.setText("1");
			txtStMonth.setHorizontalAlignment(SwingConstants.CENTER);
			txtStMonth.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtStMonth.setColumns(10);
			txtStMonth.setBounds(249, 153, 30, 37);
			contentPanel.add(txtStMonth);
		}
		{
			txtStYear = new JTextField();
			txtStYear.setToolTipText("Year");
			txtStYear.setText("1999");
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
			txtStDay.setToolTipText("Day");
			txtStDay.setText("1");
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
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(45, 250, 340, 120);
			contentPanel.add(scrollPane);
			{
				txtDesc = new JTextField();
				scrollPane.setViewportView(txtDesc);
				txtDesc.setFont(new Font("Dialog", Font.PLAIN, 18));
				txtDesc.setColumns(10);
			}
		}
		
		JComboBox cmbPriority = new JComboBox();
		cmbPriority.setToolTipText("Select priority");
		cmbPriority.setModel(new DefaultComboBoxModel(Priority.values()));
		cmbPriority.setSelectedIndex(1);
		cmbPriority.setFont(new Font("Dialog", Font.PLAIN, 18));
		cmbPriority.setBounds(249, 84, 138, 31);
		contentPanel.add(cmbPriority);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			
		}
		
	}
}
