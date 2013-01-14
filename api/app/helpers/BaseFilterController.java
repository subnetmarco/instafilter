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
package helpers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.transaction.NotSupportedException;

import org.apache.commons.lang.StringUtils;

import jobs.ApplyFilterJob;
import jobs.ApplyFilterResult;

import exceptions.FileTooBigException;
import exceptions.NotSupportedFormatException;
import filters.BaseFilter;
import filters.FilterOptions;
import filters.SupportedFormats;

import play.Play;
import play.libs.F.Promise;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Controller;

public class BaseFilterController extends Controller {

	private static final String X_TIME_ELAPSED = "X-Time-Elapsed";

	@Before
	static void checkIp() {
		if (Play.mode.isProd() && !FirewallConfiguration.isAuthorized(request.remoteAddress)) {
			forbidden("Consume the API through Mashape at https://www.mashape.com/thefosk/instafilter-io");
		}
	}
	
	@Before
	static void startTime() {
		response.setHeader(X_TIME_ELAPSED, ((Long)new Date().getTime()).toString());
	}
	
	@After
	static void endTime() {
		String header = response.getHeader(X_TIME_ELAPSED);
		long duration = new Date().getTime() - Long.parseLong(header);
		response.setHeader(X_TIME_ELAPSED, ((Long)duration).toString());
		if (response.status == 200) {
			response.setHeader("X-Mashape-Billing", "filters=1");
		}
	}
	
	protected static void executeFilter(BaseFilter baseFilter, File image, String url) {
		executeFilter(baseFilter, null, image, url);
	}
	
	protected static void executeFilter(BaseFilter baseFilter, FilterOptions options, File image, String url) {
		try {
			Promise<ApplyFilterResult> now = null;
			if (image == null) {
				now = new ApplyFilterJob(baseFilter, new URL(url), options).now();
			} else {
				now = new ApplyFilterJob(baseFilter, image, options).now();
			}
			ApplyFilterResult output = await(now);
			if (output.getException() != null) {
				throw output.getException();
			}
			String format = null;
			if (image == null) {
				format = SupportedFormats.getFormatExtension(url.toString());
			} else {
				format = SupportedFormats.getFormatExtension(image.getAbsolutePath());
			}
			if (StringUtils.isNotBlank(format)) {
				response.setHeader("Content-Type", "image/" + format);
			}
			response.out = output.getOutput();
		} catch (Exception e) {
			handleError(e);
		}
	}
	
	protected static void handleError(Exception e) {
		response.status = 400;
		String message = e.getMessage();
		if (e instanceof MalformedURLException) {
			message = "Invalid URL";
		} else if (e instanceof NotSupportedFormatException) {
			message = "Format not supported";
		} else if (e instanceof FileTooBigException) {
			message = "File is too big. Limit is " + BaseFilter.FILE_SIZE_LIMIT / 1024 / 1024 + "MB.";
		}
 		renderJSON(new Error(message));
	}
	
}
