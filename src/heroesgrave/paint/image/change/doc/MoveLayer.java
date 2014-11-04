// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
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

package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.DocumentChange;

public class MoveLayer implements DocumentChange
{
	private Layer layer, oldParent, newParent;
	private int oldIndex = -1, newIndex = -1;
	
	public MoveLayer(Layer layer, Layer target, int targetIndex)
	{
		this.layer = layer;
		this.oldParent = layer.getParentLayer();
		this.newParent = target;
		newIndex = targetIndex;
	}
	
	public void apply(Document doc)
	{
		oldIndex = oldParent.getIndex(layer);
		if(newIndex != -1)
			newParent.addLayer(layer, newIndex);
		else
			newParent.addLayer(layer);
		doc.setCurrent(layer);
		doc.reconstructFlatmap();
	}
	
	public void revert(Document doc)
	{
		oldParent.addLayer(layer, oldIndex);
		doc.setCurrent(layer);
		doc.reconstructFlatmap();
	}
}
