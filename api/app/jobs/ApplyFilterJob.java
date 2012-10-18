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
package jobs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;


import filters.BaseFilter;
import filters.FilterOptions;
import play.jobs.Job;

public class ApplyFilterJob extends Job<ApplyFilterResult> {

	private BaseFilter baseFilter;
	private URL url;
	private File file;
	private FilterOptions filterOptions;
	
	public ApplyFilterJob(BaseFilter baseFilter, URL url, FilterOptions filterOptions) {
		this(baseFilter, url);
		this.filterOptions = filterOptions;
	}
	
	public ApplyFilterJob(BaseFilter baseFilter, URL url) {
		this.baseFilter = baseFilter;
		this.url = url;
	}
	
	public ApplyFilterJob(BaseFilter baseFilter, File file, FilterOptions filterOptions) {
		this(baseFilter, file);
		this.filterOptions = filterOptions;
	}
	
	public ApplyFilterJob(BaseFilter baseFilter, File file) {
		this.baseFilter = baseFilter;
		this.file = file;
	}

	@Override
	public ApplyFilterResult doJobWithResult() throws Exception {
		ApplyFilterResult result = new ApplyFilterResult();
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			if (url == null) {
				baseFilter.filter(file, output, filterOptions);
			} else {
				baseFilter.filter(url, output, filterOptions);
			}
			result.setOutput(output);
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
}
