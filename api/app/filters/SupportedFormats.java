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
package filters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import exceptions.EmptyExtensionException;

public class SupportedFormats {

	private static List<String> formats;
	
	static {
		formats = new ArrayList<String>();
		String[] formatNames = ImageIO.getWriterFormatNames();
		for(String formatName : formatNames) {
			if (!formats.contains(formatName.toLowerCase())) {
				formats.add(formatName.toLowerCase());
			}
		}
	}
	
	public static String getFormatExtension(String path) {
		return FilenameUtils.getExtension(path).toLowerCase().trim();
	}
	
	public static String supportFormat(String path) throws EmptyExtensionException {
		String extension = getFormatExtension(path);
		if (StringUtils.isBlank(extension)) {
			throw new EmptyExtensionException();
		}
		if (formats.contains(extension)) {
			return extension;
		} else {
			return null;
		}
	}
	
	public static List<String> getSupportedFormats() {
		return formats;
	}
	
}
