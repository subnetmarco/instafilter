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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import exceptions.EmptyExtensionException;
import exceptions.FileTooBigException;
import exceptions.NotSupportedFormatException;

public abstract class BaseFilter {
	
	public static final int FILE_SIZE_LIMIT = 4194304;

	public void filter(URL url, OutputStream output, FilterOptions options) throws IOException, EmptyExtensionException, NotSupportedFormatException, FileTooBigException {
		String format = SupportedFormats.supportFormat(url.toString());
		if (StringUtils.isBlank(format)) {
			throw new NotSupportedFormatException();
		}
		
		// Calculate size
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		int contentLength = openConnection.getContentLength();
		if (contentLength > FILE_SIZE_LIMIT) {
			throw new FileTooBigException();
		}
		openConnection.disconnect();
		
		BufferedImage read = ImageIO.read(url);
		doFilter(read, output, options, format);
	}
	
	public void filter(URL url, OutputStream output) throws IOException, EmptyExtensionException, NotSupportedFormatException, FileTooBigException {
		filter(url, output, null);
	}
	
	public void filter(File file, OutputStream output, FilterOptions options) throws IOException, EmptyExtensionException, NotSupportedFormatException, FileTooBigException {
		if (file.length() > FILE_SIZE_LIMIT) { // 
			throw new FileTooBigException();
		}
		String format = SupportedFormats.supportFormat(file.getAbsolutePath());
		if (StringUtils.isBlank(format)) {
			throw new NotSupportedFormatException();
		}
		doFilter(ImageIO.read(file), output, options, format);
	}
	
	public void filter(File file, OutputStream output) throws IOException, EmptyExtensionException, NotSupportedFormatException, FileTooBigException {
		filter(file, output, null);
	}
	
	protected abstract void doFilter(BufferedImage image, OutputStream output, FilterOptions options, String format) throws IOException;
	
}
