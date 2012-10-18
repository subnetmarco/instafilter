/*******************************************************************************
 * Instafilter, a RESTful API for manipulating images and applying customizable filters in real-time.
 * 
 * Copyright (C) 2012  Marco Palladino
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package filters.collage;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.imgscalr.Scalr;

import filters.BaseFilter;
import filters.FilterOption;
import filters.FilterOptions;

public class ResizeFilter extends BaseFilter {

	@Override
	protected void doFilter(BufferedImage image, OutputStream output,
			FilterOptions options, String format) throws IOException {
		
		Integer size = options.get(FilterOption.SIZE, Integer.class);
		BufferedImage thumbnail = Scalr.resize(image, size);
		ImageIO.write(thumbnail, format, output);
	}

}
