package tk.shsidcomputers.hacksh2015;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
