package tk.shsidcomputers.hacksh2015;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

public class Storage extends HashSet<Item> {
	private static final long serialVersionUID = 4680889975419391629L;
	private final File file;
	private boolean storing = true;
	
	public Storage(File file) throws IOException {
		this.file = file;
		Scanner reader = new Scanner(file);
		while (reader.hasNextLine()) {
			add(Item.fromJSON(reader.nextLine()));
		}
		reader.close();
	}
	
	public synchronized void store() throws IOException {
		if (!storing) return;
		if (!file.delete()) throw new IOException();
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		for (Item toWrite: this) {
			writer.println(toWrite.toJSON());
		}
		writer.close();
	}
	
	protected void stopStoring() {
		storing = false;
	}
	
}
