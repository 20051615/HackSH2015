package tk.shsidcomputers.hacksh2015;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.lang.Runnable;
import java.lang.String;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ToDoManager {

	private JFrame frame;
	private Storage current;
	private Storage archived;

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
		this.current = new Storage(currentfile);
		this.archived = new Storage(archivefile);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
