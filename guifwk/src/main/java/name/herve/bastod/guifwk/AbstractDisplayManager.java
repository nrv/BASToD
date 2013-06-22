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
package name.herve.bastod.guifwk;

import com.badlogic.gdx.math.Vector3;



/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class AbstractDisplayManager extends AbstractDisplay {
	private AbstractScreen screen;
	
	public void attach(AbstractScreen screen) {
		this.screen = screen;
	}
	
	@Override
	public void batchBegin() {
		super.batchBegin(screen.cameraCombined());
	}
	
	public void detach() {
		screen = null;
	}

	public int getScreenHeight() {
		return screen.getScreenHeight();
	}

	public int getScreenWidth() {
		return screen.getScreenWidth();
	}

	public void cameraUnproject(Vector3 vec) {
		screen.cameraUnproject(vec);
	}
}
