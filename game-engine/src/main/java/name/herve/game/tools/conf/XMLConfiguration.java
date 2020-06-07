/*
 * Copyright 2012, 2020 Nicolas HERVE
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
package name.herve.game.tools.conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import name.herve.game.tools.GameException;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class XMLConfiguration extends Configuration {
	public final static String ROOT_NODE = "configuration";

	private Map<String, String> props;

	public XMLConfiguration(File f) throws GameException {
		super(f);
	}

	@Override
	public Set<String> getKeys() {
		return props.keySet();
	}

	@Override
	public String getString(String key) throws GameException {
		return props.get(key);
	}

	// Nasty but it works !
	@Override
	protected void load() throws GameException {
		props = new HashMap<>();

		BufferedReader r = null;
		try {
			InputStream is = new FileInputStream(getFile());
			r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}
			// System.out.println(sb.toString());

			boolean insideComment = false;
			boolean insideHeader = false;
			boolean insideOpeningTag = false;
			boolean insideClosingTag = false;
			LinkedList<String> currentKey = new LinkedList<>();
			StringBuilder currentValue = new StringBuilder();
			StringBuilder currentTag = null;

			for (int idx = 0; idx < sb.length();) {
				char c = sb.charAt(idx);

				if (c == '<') {
					if (insideOpeningTag || insideClosingTag) {
						throw new GameException("Invalid XML, found a '<' inside a tag");
					}
					if (!insideComment && !insideHeader) {
						if ((idx + 1) < sb.length()) {
							c = sb.charAt(idx + 1);
							if (c == '?') {
								insideHeader = true;
								idx++;
							} else if (c == '/') {
								insideClosingTag = true;
								idx++;
								currentTag = new StringBuilder();
							} else if (c == '!') {
								if ((idx + 3) < sb.length()) {
									if ((sb.charAt(idx + 2) == '-') && (sb.charAt(idx + 3) == '-')) {
										insideComment = true;
										idx += 3;
									}
								} else {
									throw new GameException("Invalid XML, found a '<!' in last position");
								}
							} else {
								insideOpeningTag = true;
								currentTag = new StringBuilder();
							}
						} else {
							throw new GameException("Invalid XML, found a '<' in last position");
						}
					}
				} else if (c == '>') {
					if (insideOpeningTag) {
						String tag = currentTag.toString().trim();
						if (currentKey.isEmpty() && !ROOT_NODE.equalsIgnoreCase(tag)) {
							throw new GameException("Invalid XML, first tag '" + ROOT_NODE + "' expected");
						}
						currentKey.addLast(tag);
						insideOpeningTag = false;
						currentValue = new StringBuilder();
					} else if (insideClosingTag) {
						String tag = currentTag.toString().trim();
						String val = currentValue.toString().trim();

						if (!tag.equalsIgnoreCase(currentKey.getLast())) {
							throw new GameException("Invalid XML, found closing tag '" + tag + "' where '" + currentKey.getLast() + "' was expected");
						}

						if (val.length() > 0) {
							StringBuilder key = new StringBuilder();
							for (int k = 1; k < currentKey.size(); k++) {
								if (k > 1) {
									key.append('.');
								}
								key.append(currentKey.get(k));
							}
							props.put(key.toString(), val);
						}

						currentKey.removeLast();
						insideClosingTag = false;
						currentValue = new StringBuilder();
					} else if (insideComment) {
						if (sb.charAt(idx - 1) == '-') {
							if (((idx - 2) >= 0) && (sb.charAt(idx - 2) == '-')) {
								insideComment = false;
							}
						}
					} else if (insideHeader) {
						if (sb.charAt(idx - 1) == '?') {
							insideHeader = false;
						}
					} else {
						throw new GameException("Invalid XML, found a '>' without corresponding '<'");
					}
				} else {
					if (insideOpeningTag || insideClosingTag) {
						currentTag.append(c);
					} else if (!insideComment && !insideHeader) {
						currentValue.append(c);
					}
				}

				idx++;
			}

			if (insideOpeningTag || insideClosingTag || insideComment || insideHeader || (currentKey.size() > 0)) {
				throw new GameException("Invalid XML, everything is not closed properly");
			}

		} catch (IOException e) {
			throw new GameException(e);
		} finally {
			try {
				if (r != null) {
					r.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
