/*
 *	Copyright 2013 HeroesGrave
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.paint.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public class ShapeChange extends Frame
{
	private Shape changeShape;
	private int colour;
	private Stroke stroke;
	private AlphaComposite trans;
	
	public ShapeChange(Shape shape, int colour)
	{
		this.changeShape = shape;
		this.colour = colour;
		if((colour & 0xff000000) != 0xff000000)
			trans = AlphaComposite.getInstance(AlphaComposite.SRC, ((colour >> 24) & 0xff) / 255f);
	}
	
	public ShapeChange(Shape shape, int colour, Stroke stroke)
	{
		this.changeShape = shape;
		this.colour = colour;
		this.stroke = stroke;
		if((colour & 0xff000000) != 0xff000000)
			trans = AlphaComposite.getInstance(AlphaComposite.SRC, ((colour >> 24) & 0xff) / 255f);
	}
	
	public void apply(BufferedImage image)
	{
		Graphics2D g2d = image.createGraphics();
		
		Stroke oldStroke = null;
		Composite oldComp = null;
		Color oldColor = g2d.getColor();
		
		if(stroke != null)
		{
			oldStroke = g2d.getStroke();
			g2d.setStroke(stroke);
		}
		if(trans != null)
		{
			oldComp = g2d.getComposite();
			g2d.setComposite(trans);
		}
		g2d.setColor(new Color(colour));
		
		g2d.draw(changeShape);
		
		g2d.setColor(oldColor);
		if(trans != null)
			g2d.setComposite(oldComp);
		if(stroke != null)
			g2d.setStroke(oldStroke);
	}
	
	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}
}