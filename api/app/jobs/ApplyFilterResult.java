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

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;


import filters.BaseFilter;
import filters.FilterOptions;
import play.jobs.Job;
import sun.security.action.PutAllAction;

public class ApplyFilterResult extends Job<ByteArrayOutputStream> {

	private ByteArrayOutputStream output;
	private Exception exception;
	
	public ByteArrayOutputStream getOutput() {
		return output;
	}
	public void setOutput(ByteArrayOutputStream output) {
		this.output = output;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
