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

package heroesgrave.spade.image;

import heroesgrave.spade.image.change.IChange;
import heroesgrave.spade.image.change.IDocChange;
import heroesgrave.spade.io.ImageExporter;
import heroesgrave.spade.io.ImageImporter;
import heroesgrave.spade.main.Popup;
import heroesgrave.spade.main.Spade;
import heroesgrave.utils.misc.Metadata;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class Document
{
	public static int MAX_DIMENSION = 4096;
	
	private LinkedList<IDocChange> changes = new LinkedList<IDocChange>();
	private LinkedList<IDocChange> reverted = new LinkedList<IDocChange>();
	
	private int width, height;
	private File file;
	private Metadata info;
	private Layer root, current;
	private History history;
	
	private IChange previewChange;
	public int lowestChange;
	
	private boolean initialised;
	public boolean repaint;
	
	private ArrayList<Layer> flatmap = new ArrayList<Layer>();
	
	private Document()
	{
		this.info = new Metadata();
		this.history = new History(this);
	}
	
	public Document(int width, int height)
	{
		this();
		this.width = width;
		this.height = height;
		
		this.current = this.root = new Layer(this, new RawImage(width, height), new Metadata());
		this.flatmap.clear();
		root.constructFlatMap(flatmap);
		initialised = true;
	}
	
	public static Document loadFromFile(File f)
	{
		Document doc = new Document();
		doc.file = f;
		if(!ImageImporter.loadImage(f.getAbsolutePath(), doc))
			return null;
		doc.flatmap.clear();
		doc.root.constructFlatMap(doc.flatmap);
		doc.initialised = true;
		return doc;
	}
	
	public void reconstructFlatmap()
	{
		if(!initialised)
			return;
		Spade.main.gui.layers.redrawTree();
		this.flatmap.clear();
		root.constructFlatMap(flatmap);
	}
	
	public History getHistory()
	{
		return history;
	}
	
	public Layer getRoot()
	{
		return root;
	}
	
	public Layer getCurrent()
	{
		return current;
	}
	
	public void setRoot(Layer root)
	{
		this.current = this.root = root;
		this.reconstructFlatmap();
	}
	
	public boolean setCurrent(Layer current)
	{
		if(this.current == current)
		{
			return false;
		}
		this.current = current;
		Spade.main.gui.layers.select(current);
		return true;
	}
	
	public Metadata getMetadata()
	{
		return info;
	}
	
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.allChanged();
		Spade.main.gui.canvas.resized(width, height);
	}
	
	public void save()
	{
		save(false);
	}
	
	public void save(boolean panic)
	{
		final String fileName = this.file.getAbsolutePath();
		
		String extension = "";
		
		int i = fileName.lastIndexOf('.');
		
		if(i > 0)
		{
			extension = fileName.substring(i + 1);
		}
		
		final ImageExporter exporter = ImageExporter.get(extension);
		
		final Document doc = this;
		
		try
		{
			exporter.save(doc, new File(fileName));
			history.save();
			Spade.main.gui.checkButtonNames();
		}
		catch(Exception e)
		{
			if(!panic)
				Popup.showException("Error Saving Document", e, "This error occured while saving the file " + file
						+ ". It may work if you try again, but if not, we apologise. Report the bug to get it fixed as soon as possible");
			else
				Popup.showException("Error Saving Document", e, "This error occured while saving the file " + file
						+ " after a crash. Sadly, we cannot recover from this.");
			return;
		}
	}
	
	public String getDir()
	{
		if(file != null)
			return this.file.getParent();
		else
			return System.getProperty("user.dir");
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public ArrayList<Layer> getFlatMap()
	{
		return flatmap;
	}
	
	/*
	 * This should only be called when loading an image. I need to find another way to do this.
	 */
	public void setDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public BufferedImage getRenderedImage()
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		{
			Graphics2D g = image.createGraphics();
			for(Layer l : flatmap)
			{
				l.render(g);
			}
			g.dispose();
		}
		return image;
	}
	
	public void preview(IChange change)
	{
		this.previewChange = change;
		this.repaint();
	}
	
	public void applyPreview()
	{
		this.getCurrent().addChange(previewChange);
		this.previewChange = null;
		this.repaint();
	}
	
	public void changed(Layer layer)
	{
		if(!initialised)
			return;
		Spade.main.gui.checkButtonNames();
		lowestChange = Math.min(lowestChange, flatmap.indexOf(layer));
		this.repaint();
	}
	
	public void allChanged()
	{
		lowestChange = 0;
		this.repaint();
	}
	
	public IChange getPreview()
	{
		return previewChange;
	}
	
	public void setFile(File file)
	{
		this.file = file;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public void addChange(IDocChange change)
	{
		history.addChange(-1);
		changes.push(change);
		change.apply(this);
		this.allChanged();
	}
	
	public void addChangeSilent(IDocChange change)
	{
		changes.push(change);
		change.apply(this);
		this.allChanged();
	}
	
	public void revertChange()
	{
		if(changes.isEmpty())
		{
			return;
		}
		IDocChange change = changes.pop();
		reverted.push(change);
		change.revert(this);
		this.allChanged();
	}
	
	public void repeatChange()
	{
		if(reverted.isEmpty())
		{
			return;
		}
		IDocChange change = reverted.pop();
		changes.push(change);
		change.repeat(this);
		this.allChanged();
	}
	
	public boolean saved()
	{
		return this.file != null && this.history.isSaved();
	}
	
	public void repaint()
	{
		repaint = true;
		Spade.main.gui.repaint();
	}
	
	public void setMetadata(Metadata info)
	{
		this.info = info;
	}
}
