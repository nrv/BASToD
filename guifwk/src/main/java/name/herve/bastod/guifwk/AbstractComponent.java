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

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class AbstractComponent extends AbstractDisplay implements OnScreen {
	
	public enum HorizontalAlignment {
		CENTER, LEFT, LEFT_OUTISDE, RIGHT, RIGHT_OUTSIDE
	};
	
	public enum VerticalAlignment {
		ABOVE, BOTTOM, MIDDLE, TOP, UNDER
	};
	
	public final static int SMALL_SPACER = 2;
	
	private Texture background;
	private Texture component;
	private Texture disabled;
	private boolean enabled;
	private int h;
	private long lastUpdateTime;
	private String name;
	private boolean needUpdate;
	private long updateRate;
	private int w;
	private int x;
	private int xOffset;
	private int y;
	private int yOffset;

	public AbstractComponent(String name, int x, int y, int w, int h) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.name = name;
		component = null;
		background = null;
		xOffset = 0;
		yOffset = 0;
		setNeedUpdate(true);
		setEnabled(true);
		setUpdateRate(100000000);
	}

	protected void disposeComponent() {
		if (component != null) {
			component.dispose();
			component = null;
		}
	}

	public Rectangle2D.Float getBounds(BitmapFont font, CharSequence cs) {
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, cs);
		return new Rectangle2D.Float(0, 0, layout.width, layout.height);
	}
	
	public void draw(BitmapFont font, CharSequence cs, VerticalAlignment va, HorizontalAlignment ha) {
		float x = getX();
		float y = getY();
		
		Rectangle2D.Float layout = getBounds(font, cs);
//		TextBounds b = font.getBounds(cs);
		
		switch (va) {
		case MIDDLE :
			y += (getHeight() - layout.height) / 2;
			break;
		case ABOVE :
			y += getHeight() + SMALL_SPACER;
			break;
		case UNDER:
			y -= layout.height + SMALL_SPACER;
			break;
		case TOP:
			y += getHeight() - layout.height - SMALL_SPACER;
			break;
		case BOTTOM:
			y += SMALL_SPACER;
			break;
		}
		
		switch (ha) {
		case CENTER:
			x += (getWidth() - layout.width) / 2;
			break;
		case LEFT_OUTISDE:
			x -= layout.width + SMALL_SPACER;
			break;
		case RIGHT_OUTSIDE:
			x += getWidth() + SMALL_SPACER;
			break;
		case RIGHT:
			x += getWidth() - layout.width + SMALL_SPACER;
			break;
		case LEFT:
			x += SMALL_SPACER;
			break;
		}
		
		draw(font, cs, x, y);
	}
	
	public void drawCentered(BitmapFont font, CharSequence cs) {
		draw(font, cs, VerticalAlignment.MIDDLE, HorizontalAlignment.CENTER);
	}
	
	public void drawLeft(BitmapFont font, CharSequence cs) {
		draw(font, cs, VerticalAlignment.MIDDLE, HorizontalAlignment.LEFT_OUTISDE);
	}
	
	public void drawRight(BitmapFont font, CharSequence cs) {
		draw(font, cs, VerticalAlignment.MIDDLE, HorizontalAlignment.RIGHT_OUTSIDE);
	}
	
	public abstract void drawText();
	
	protected Texture getBackground() {
		return background;
	}

	protected Texture getComponent() {
		return component;
	}

	@Override
	public int getHeight() {
		return h;
	}
	
	public String getName() {
		return name;
	}

	public long getUpdateRate() {
		return updateRate;
	}

	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public boolean keyDown(int k) {
		return false;
	}

	@Override
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void render() {
		long now = TimeUtils.nanoTime();
		if (needUpdate && (now - lastUpdateTime > updateRate)) {
			disposeComponent();
			component = updateComponent();
			lastUpdateTime = now;
		}

		batchBegin();
		if (background != null) {
			draw(background, x, y);
		}
		if (component != null) {
			draw(component, xOffset + x, yOffset + y);
		}
		if (!isEnabled()) {
			draw(disabled, x, y);
		}
		drawText();
		batchEnd();
	}

	protected void setBackground(Texture background) {
		this.background = background;
	}

	@Override
	public void setBounds(int w, int h) {
		this.w = w;
		this.h = h;
	}

	protected void setComponent(Texture component) {
		this.component = component;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public void setUpdateRate(long updateRate) {
		this.updateRate = updateRate;
	}
	
	protected void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	protected void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	@Override
	public void start() {
		super.start();
		lastUpdateTime = 0;
		
		
//		Blending bck = Pixmap.getBlending();
//		Pixmap.setBlending(Blending.None);

		Pixmap p = new Pixmap(getWidth(), getHeight(), Pixmap.Format.RGBA8888);
		p.setBlending(Blending.None);
		Color c1 = Color.BLACK.cpy();
		c1.a = 0.8f;
		p.setColor(c1);
		p.fillRectangle(0, 0, getWidth(), getHeight());
		
		
//		p.setColor(Color.WHITE);
//		int step = 8;
//		for (int d = 0; d < w; d += step) {
//			p.drawLine(x + d, h, 0, h - d);
//			p.drawLine(x + d, h - w, w, h - d);
//			p.drawLine(x + d, h, w, h - w + d);
//			p.drawLine(x + d, h - w, 0, h - w + d);
//		}
		
		disabled = new Texture(p);
		p.dispose();
//		Pixmap.setBlending(bck);
		
	}

	@Override
	public void stop() {
		super.stop();

		if (disabled != null) {
			disabled.dispose();
			disabled = null;
		}
	}

	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	public abstract Texture updateComponent();

	@Override
	public void validate() {
	}
}
