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
package name.herve.bastod.engine;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import name.herve.bastod.tools.SLTDException;
import name.herve.bastod.tools.conf.Configuration;
import name.herve.bastod.tools.conf.PropertiesConfiguration;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class UnitFactory {
	public enum UnitSet {
		STANDARD("conf/unitset_standard.conf"), BRICKS("conf/unitset_bricks.conf");
		
		final String file;
		
		private UnitSet(String file) {
			this.file = file;
		}
	};
	
	
}
