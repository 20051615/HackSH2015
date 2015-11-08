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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @author SHSIDComputerClub
 *
 */
final class Storage extends HashSet<Item> {
	private static final long serialVersionUID = 4680889975419391629L;
	private final File file;
	private boolean storing = true;
	
	Storage(File file) throws IOException {
		this.file = file;
		Scanner reader = new Scanner(file);
		while (reader.hasNextLine()) {
			add(Item.fromJSON(reader.nextLine()));
		}
		reader.close();
	}
	
	synchronized void store() throws IOException {
		if (!storing) return;
		if (!file.delete()) throw new IOException();
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		for (Item toWrite: this) {
			writer.println(toWrite.toJSON());
		}
		writer.close();
	}
	
	void stopStoring() {
		storing = false;
	}
	
}
