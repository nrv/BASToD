/*
 * Copyright 2012, 2013 Nicolas HERVE
 * 
 * This file is part of BASToD.
 * 
 * BASToD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BASToD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BASToD. If not, see <http://www.gnu.org/licenses/>.
 */
package name.herve.bastod.tools.conf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import name.herve.bastod.tools.GameException;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class Configuration {

	private static void repeatChar(Writer w, int n, char c) throws IOException {
		char[] cs = new char[n];
		Arrays.fill(cs, c);
		w.write(cs);
	}

	public static void dump(Configuration c, String comment, File file) throws GameException {
		DateFormat df = new SimpleDateFormat("EEEEE d MMMMM yyyy 'at' HH:mm:ss", Locale.US);
		BufferedWriter w = null;
		try {
			OutputStream os = new FileOutputStream(file);
			w = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

			if (file.getName().toUpperCase().endsWith(".XML")) {
				w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				w.write("<!-- Dumped " + df.format(new Date()) + " -->\n");
				if (comment != null) {
					w.write("<!-- " + comment + " -->\n");
					w.write("\n");
				}
				w.write("<" + XMLConfiguration.ROOT_NODE + ">\n");
				String[] last = new String[] {};
				for (String k : c.getSortedKeys()) {
					String[] current = k.split("\\.");
					int common = 0;
					while ((common < last.length) && (common < current.length) && (last[common].equalsIgnoreCase(current[common]))) {
						common++;
					}
					for (int i = last.length - 2; i >= common; i--) {
						repeatChar(w, i, '\t');
						w.write("</" + last[i] + ">\n");
					}
					for (int i = common; i < current.length - 1; i++) {
						repeatChar(w, i, '\t');
						w.write("<" + current[i] + ">\n");
					}
					repeatChar(w, current.length - 1, '\t');
					w.write("<" + current[current.length - 1] + ">" + c.getString(k) + "</" + current[current.length - 1] + ">\n");
					last = current;
				}
				for (int i = last.length - 2; i >= 0; i--) {
					repeatChar(w, i, '\t');
					w.write("</" + last[i] + ">\n");
				}
				w.write("</" + XMLConfiguration.ROOT_NODE + ">\n");
			} else {
				w.write("# Dumped " + df.format(new Date()) + "\n");
				if (comment != null) {
					w.write("# " + comment + "\n");
					w.write("\n");
				}

				int maxLength = 0;
				for (String k : c.getKeys()) {
					if (maxLength < k.length()) {
						maxLength = k.length();
					}
				}

				for (String k : c.getSortedKeys()) {
					w.write(k);
					repeatChar(w, maxLength - k.length(), ' ');
					w.write("   = " + c.getString(k));
					w.write("\n");
				}
			}

		} catch (IOException e) {
			throw new GameException(e);
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static Configuration load(String f) throws GameException {
		URL url = Configuration.class.getClassLoader().getResource(f);

		if (url == null) {
			throw new GameException("Unable to find configuration file '" + f + "'");
		}

		try {
			File file = new File(url.toURI());
			if (file.getName().toUpperCase().endsWith(".XML")) {
				return new XMLConfiguration(file);
			} else {
				return new PropertiesConfiguration(file);
			}
		} catch (URISyntaxException e) {
			throw new GameException(e);
		}
	}

	private File f;

	public Configuration(File f) throws GameException {
		super();
		this.f = f;
		load();
	}

	public boolean getBoolean(String key) throws GameException {
		return Boolean.parseBoolean(getString(key));
	}

	protected File getFile() {
		return f;
	}

	public float getFloat(String key) throws GameException {
		try {
			return Float.parseFloat(getString(key));
		} catch (NumberFormatException e) {
			throw new GameException("Property " + key + " in " + getFile() + " is not a valid float : " + e.getMessage());
		}
	}

	public int getInt(String key) throws GameException {
		try {
			return Integer.parseInt(getString(key));
		} catch (NumberFormatException e) {
			throw new GameException("Property " + key + " in " + getFile() + " is not a valid int : " + e.getMessage());
		}
	}

	public abstract Set<String> getKeys();

	public long getLong(String key) throws GameException {
		try {
			return Long.parseLong(getString(key));
		} catch (NumberFormatException e) {
			throw new GameException("Property " + key + " in " + getFile() + " is not a valid long : " + e.getMessage());
		}
	}

	public SortedSet<String> getSortedKeys() {
		return new TreeSet<String>(getKeys());
	}

	public abstract String getString(String key) throws GameException;

	protected abstract void load() throws GameException;
}
