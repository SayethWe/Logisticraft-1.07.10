/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package com.sinesection.logisticraft.gui.tooltips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
@SideOnly(Side.CLIENT)
public class ToolTip {

	private final List<String> lines = new ArrayList<>();
	private final long delay;
	private long mouseOverStart;

	public ToolTip() {
		this.delay = 0;
	}

	public ToolTip(int delay) {
		this.delay = delay;
	}

	public void clear() {
		lines.clear();
	}
	
	public boolean add(String line, EnumChatFormatting formatting) {
		return lines.add(line);
	}

	public boolean add(String line) {
		return lines.add(line);
	}

	public boolean add(List<?> lines) {
		boolean changed = false;
		for (Object line : lines) {
			if (line instanceof String) {
				changed |= add((String) line);
			}
		}
		return changed;
	}

	public List<String> getLines() {
		return Collections.unmodifiableList(lines);
	}

	public void onTick(boolean mouseOver) {
		if (delay == 0) {
			return;
		}
		if (mouseOver) {
			if (mouseOverStart == 0) {
				mouseOverStart = System.currentTimeMillis();
			}
		} else {
			mouseOverStart = 0;
		}
	}

	public boolean isReady() {
		return delay == 0 || mouseOverStart != 0 && System.currentTimeMillis() - mouseOverStart >= delay;
	}

	public void refresh() {
	}

}