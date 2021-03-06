// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.spade.gui.misc;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.separator.WebSeparator;

/**
 * Helper methods to allow plugins to create dialogs with weblaf components.
 * Expand as neccessary.
 * 
 * @author HeroesGrave
 *
 */
public class WeblafWrapper
{
	public static JMenuItem createMenuItem(String text)
	{
		WebMenuItem c = new WebMenuItem(text);
		return c;
	}
	
	public static JCheckBox createCheckBox()
	{
		WebCheckBox c = new WebCheckBox();
		return c;
	}
	
	public static JMenuItem asMenuItem(JComponent comp)
	{
		WebMenuItem c = new WebMenuItem();
		c.add(comp);
		return c;
	}
	
	public static JSeparator createSeparator()
	{
		WebSeparator c = new WebSeparator();
		return c;
	}
	
	public static JRadioButton createRadioButton()
	{
		WebRadioButton c = new WebRadioButton();
		return c;
	}
	
	public static JLabel createLabel(String text)
	{
		WebLabel c = new WebLabel(text);
		return c;
	}
	
	public static JButton createButton(String text)
	{
		WebButton c = new WebButton(text);
		return c;
	}
}
