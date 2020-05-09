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
package name.herve.bastod.guifwk.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import name.herve.bastod.guifwk.AbstractComponent;
import name.herve.bastod.guifwk.OnScreen;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class ComponentsLineLayout extends AbstractLayoutComponent implements Iterable<OnScreen> {
	public enum Spacing {
		FILL1, FILL2, LEFT, RIGHT
	};

	private List<OnScreen> components;
	private Spacing spacing;

	public ComponentsLineLayout() {
		super();

		components = new ArrayList<>();
		setSpacing(Spacing.FILL1);
	}

	public boolean addComponent(OnScreen e) {
		return components.add(e);
	}

	public void clearComponents() {
		components.clear();
	}

	public OnScreen getComponent(int index) {
		return components.get(index);
	}

	public int getNbComponents() {
		return components.size();
	}

	public int indexOfComponent(AbstractComponent o) {
		return components.indexOf(o);
	}

	@Override
	public Iterator<OnScreen> iterator() {
		return components.iterator();
	}

	public boolean removeComponent(AbstractComponent o) {
		return components.remove(o);
	}

	public OnScreen removeComponent(int index) {
		return components.remove(index);
	}

	public void setSpacing(Spacing spacing) {
		this.spacing = spacing;
	}

	@Override
	public void validate() {
		if (getNbComponents() == 0) {
			return;
		}

		int totalWidth = 0;
		int totalHeight = 0;

		for (OnScreen c : this) {
			totalWidth += c.getWidth();
			totalHeight = Math.max(totalHeight, c.getHeight());
		}

		int yOffset = getY() + ((getHeight() - totalHeight) / 2);
		int widthLeft = Math.max(0, getWidth() - totalWidth);

		if (spacing == Spacing.RIGHT) {
			float currentX = (getX() + getWidth()) - 1;
			for (ListIterator<OnScreen> iterator = components.listIterator(components.size()); iterator.hasPrevious();) {
				OnScreen c = iterator.previous();
				currentX -= c.getWidth();
				c.moveTo((int) currentX, yOffset);
			}
		} else if (spacing == Spacing.LEFT) {
			float currentX = getX();
			for (OnScreen c : this) {
				c.moveTo((int) currentX, yOffset);
				currentX += c.getWidth();
			}
		} else {
			if (getNbComponents() == 1) {
				int spacer = widthLeft / 2;
				components.get(0).moveTo(getX() + spacer, yOffset);
			} else {
				float currentX = getX();
				int nbSpacerLeft = getNbComponents() - 1;
				if (spacing == Spacing.FILL2) {
					nbSpacerLeft += 2;
				}
				float spacer = widthLeft / nbSpacerLeft;
				if (spacing == Spacing.FILL2) {
					currentX += spacer;
				}
				for (OnScreen c : this) {
					c.moveTo((int) currentX, yOffset);
					currentX += spacer + c.getWidth();
				}
			}
		}

		for (OnScreen c : this) {
			c.validate();
		}
	}
}
