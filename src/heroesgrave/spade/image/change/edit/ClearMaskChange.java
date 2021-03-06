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
package heroesgrave.spade.image.change.edit;

import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.change.IEditChange;
import heroesgrave.spade.image.change.IMaskChange;
import heroesgrave.spade.io.Serialised;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClearMaskChange implements IEditChange, IMaskChange, Serialised
{
	public ClearMaskChange()
	{
		
	}
	
	@Override
	public void apply(RawImage image)
	{
		image.setMaskEnabled(false);
	}
	
	@Override
	public ClearMaskChange encode()
	{
		return this;
	}
	
	@Override
	public ClearMaskChange decode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
	}
}
