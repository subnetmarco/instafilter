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

public enum FilterOption {
	WIDTH, HEIGHT, X, Y, SIZE, ROTATION, GAMMA, COLOR, HUE, SATURATION, BRIGHTNESS, AMOUNT, DENSITY, MONOCHROME, RANGE, DISTRIBUTION, RANDOMNESS, THICKNESS, FADE_EDGES, GRID_TYPE,
	BLUE_GREEN, RED_BLUE, GREEN_RED, RED, GREEN, BLUE, HIGH_LEVEL, LOW_LEVEL, HIGH_OUTPUT, LOW_OUTPUT, BIAS, GAIN
}
