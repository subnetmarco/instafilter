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
import javax.persistence.criteria.CriteriaBuilder.In;

import net.sf.oval.constraint.NoSelfReference;

import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.HSBAdjustFilter;
import com.sun.org.apache.xpath.internal.operations.Bool;

import filters.BaseFilter;
import filters.FilterOption;
import filters.FilterOptions;
import groovy.swing.factory.HGlueFactory;

public class NoiseFilter extends BaseFilter {

	@Override
	protected void doFilter(BufferedImage image, OutputStream output,
			FilterOptions options, String format) throws IOException {
		int amount = options.get(FilterOption.AMOUNT, Integer.class);
		float density = options.get(FilterOption.DENSITY, Float.class);
		int distribution = options.get(FilterOption.DISTRIBUTION, Integer.class);
		boolean monochrome = options.get(FilterOption.MONOCHROME, Boolean.class);
		
		BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		com.jhlabs.image.NoiseFilter noiseFilter = new com.jhlabs.image.NoiseFilter();
		noiseFilter.setAmount(amount );
		noiseFilter.setDensity(density);
		noiseFilter.setDistribution(distribution);
		noiseFilter.setMonochrome(monochrome);
		
		noiseFilter.filter(image, outputImage);
		
		ImageIO.write(outputImage, format, output);
	}

}
