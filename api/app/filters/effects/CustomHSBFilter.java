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
package filters.effects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.HSBAdjustFilter;

import filters.BaseFilter;
import filters.FilterOption;
import filters.FilterOptions;
import groovy.swing.factory.HGlueFactory;

public class CustomHSBFilter extends BaseFilter {

	@Override
	protected void doFilter(BufferedImage image, OutputStream output,
			FilterOptions options, String format) throws IOException {

		float hue = options.get(FilterOption.HUE, Float.class);
		float saturation = options.get(FilterOption.SATURATION, Float.class);
		float brightness = options.get(FilterOption.BRIGHTNESS, Float.class);
		
		BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		HSBAdjustFilter hsbAdjustFilter = new HSBAdjustFilter();
		hsbAdjustFilter.setHFactor(hue);
		hsbAdjustFilter.setSFactor(saturation);
		hsbAdjustFilter.setBFactor(brightness);
		
		hsbAdjustFilter.filter(image, outputImage);
		
		ImageIO.write(outputImage, format, output);
	}

}
