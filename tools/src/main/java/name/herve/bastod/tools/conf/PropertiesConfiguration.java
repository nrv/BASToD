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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import name.herve.bastod.tools.SLTDException;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class PropertiesConfiguration extends Configuration {
	private Properties props;

	public PropertiesConfiguration(File f) throws SLTDException {
		super(f);
	}
	
	public String getString(String key) throws SLTDException {
		String p = props.getProperty(key);
		if (p == null) {
			throw new SLTDException("Property " + key + " not found in " + getFile());
		}
		return p;
	}

	@Override
	protected void load() throws SLTDException {
		props = new Properties();
		try {
			props.load(new FileInputStream(getFile()));
		} catch (FileNotFoundException e) {
			throw new SLTDException(e);
		} catch (IOException e) {
			throw new SLTDException(e);
		}
	}

	@Override
	public Set<String> getKeys() {
		return props.stringPropertyNames();
	}
}
