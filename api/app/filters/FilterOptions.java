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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FilterOptions {

	private Map<FilterOption, Object> options = new HashMap<FilterOption, Object>();
	
	public FilterOptions add(FilterOption option, Object value) {
		options.put(option, value);
		return this;
	}
	
	public <T> T get(FilterOption option, Class<T> clazz) {
		return (T) options.get(option);
	}
	
}
