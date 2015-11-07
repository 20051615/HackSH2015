package tk.shsidcomputers.hacksh2015;

import java.util.HashSet;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

public class Storage extends HashSet<Item> {
	private static final long serialVersionUID = 4680889975419391629L;
	private final File file;
	
	public Storage(File file) throws IOException{
		this.file = file;
		Scanner reader = new Scanner(file);
		while (reader.hasNextLine()) {
			add(Item.fromJSON(reader.nextLine()));
		}
		reader.close();
	}
	
	public void store() throws IOException{
		if (!file.delete()) throw new IOException();
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		for (Item toWrite: this) {
			writer.println(toWrite.toJSON());
		}
		writer.close();
	}
}
