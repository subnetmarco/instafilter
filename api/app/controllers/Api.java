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
package controllers;

import helpers.BaseFilterController;
import helpers.Error;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import jobs.ApplyFilterJob;
import jobs.ApplyFilterResult;

import org.apache.commons.lang.StringUtils;
import org.imgscalr.Scalr.Rotation;
import org.omg.CORBA.DynAnyPackage.Invalid;

import com.jhlabs.image.CellularFilter;

import exceptions.EmptyExtensionException;
import exceptions.NotSupportedFormatException;
import filters.BaseFilter;
import filters.FilterOption;
import filters.FilterOptions;
import filters.collage.CropFilter;
import filters.collage.PaddingFilter;
import filters.collage.ResizeFilter;
import filters.collage.RotateFilter;
import filters.collage.RotationOption;
import filters.effects.BlackAndWhiteFilter;
import filters.effects.ChannelMixFilter;
import filters.effects.CrystallizeFilter;
import filters.effects.CustomHSBFilter;
import filters.effects.GainFilter;
import filters.effects.GammaFilter;
import filters.effects.LevelsFilter;
import filters.effects.MosaicFilter;
import filters.effects.NoiseFilter;

import play.data.validation.Max;
import play.data.validation.Min;
import play.libs.F.Promise;
import play.mvc.Controller;

public class Api extends BaseFilterController {

	public static void crop(File image, String url, int x, int y, int width,
			int height) {
		FilterOptions options = new FilterOptions().add(FilterOption.X, x)
				.add(FilterOption.Y, y).add(FilterOption.WIDTH, width)
				.add(FilterOption.HEIGHT, height);
		CropFilter cropFilter = new CropFilter();
		executeFilter(cropFilter, options, image, url);
	}

	public static void resize(File image, String url, int size) {
		FilterOptions options = new FilterOptions()
				.add(FilterOption.SIZE, size);
		ResizeFilter resizeFilter = new ResizeFilter();
		executeFilter(resizeFilter, options, image, url);
	}

	public static void padding(File image, String url, int padding, String color) {
		Color c = getRGB(color);
		FilterOptions options = new FilterOptions().add(FilterOption.SIZE,
				padding).add(FilterOption.COLOR, c);
		PaddingFilter paddingFilter = new PaddingFilter();
		executeFilter(paddingFilter, options, image, url);
	}

	private static Color getRGB(String hex) {
		Color c = new Color(Integer.valueOf(hex.substring(1, 3), 16),
				Integer.valueOf(hex.substring(3, 5), 16), Integer.valueOf(
						hex.substring(5, 7), 16));
		return c;
	}

	public static void rotate(File image, String url, String rotation) {
		try {
			if (rotation == null)
				rotation = "";
			Rotation rotationOption = null;
			if ("90".equals(rotation)) {
				rotationOption = Rotation.CW_90;
			} else if ("180".equals(rotation)) {
				rotationOption = Rotation.CW_180;
			} else if ("270".equals(rotation)) {
				rotationOption = Rotation.CW_270;
			} else if ("fliphorizontally".equals(rotation.toLowerCase())) {
				rotationOption = Rotation.FLIP_HORZ;
			} else if ("flipvertically".equals(rotation.toLowerCase())) {
				rotationOption = Rotation.FLIP_VERT;
			} else {
				throw new Exception("Invalid rotation parameter value");
			}

			FilterOptions options = new FilterOptions().add(
					FilterOption.ROTATION, rotationOption);
			RotateFilter rotateFilter = new RotateFilter();
			executeFilter(rotateFilter, options, image, url);
		} catch (Exception e) {
			handleError(e);
		}
	}

	public static void blackandwhite(File image, String url) {
		BlackAndWhiteFilter blackAndWhiteFilter = new BlackAndWhiteFilter();
		executeFilter(blackAndWhiteFilter, image, url);
	}

	public static void gamma(File image, String url, Float gamma) {
		try {
			if (gamma == null || gamma < 0) {
				throw new Exception("Invalid gamma parameter value");
			}
			FilterOptions options = new FilterOptions().add(FilterOption.GAMMA,
					gamma);
			GammaFilter gammaFilter = new GammaFilter();
			executeFilter(gammaFilter, options, image, url);
		} catch (Exception e) {
			handleError(e);
		}
	}

	public static void mosaic(File image, String url, Integer size) {
		if (size == null || size < 1) {
			size = 1;
		}
		FilterOptions options = new FilterOptions()
				.add(FilterOption.SIZE, size);
		MosaicFilter mosaicFilter = new MosaicFilter();
		executeFilter(mosaicFilter, options, image, url);
	}

	public static void invert(File image, String url) {
		filters.effects.InvertFilter invertFilter = new filters.effects.InvertFilter();
		executeFilter(invertFilter, image, url);
	}

	public static void oil(File image, String url, Integer amount) {
		if (amount == null || amount < 0)
			amount = 1;
		if (amount > 10)
			amount = 10;
		FilterOptions options = new FilterOptions().add(FilterOption.RANGE,
				amount);
		filters.effects.OilFilter oilFilter = new filters.effects.OilFilter();
		executeFilter(oilFilter, options, image, url);
	}

	public static void contrast(File image, String url, Float gain, Float bias) {
		if (gain == null || gain < 0)
			gain = 0f;
		if (bias == null || bias < 0)
			bias = 0f;
		if (gain > 100)
			gain = 100f;
		if (bias > 100)
			bias = 100f;

		FilterOptions options = new FilterOptions()
				.add(FilterOption.BIAS, bias).add(FilterOption.GAIN, gain);
		GainFilter gainFilter = new GainFilter();
		executeFilter(gainFilter, options, image, url);
	}

	public static void colorededge(File image, String url) {
		filters.effects.ColoredEdgeFilter bumpFilter = new filters.effects.ColoredEdgeFilter();
		executeFilter(bumpFilter, image, url);
	}

	public static void edge(File image, String url) {
		filters.effects.EdgeFilter edgeFilter = new filters.effects.EdgeFilter();
		executeFilter(edgeFilter, image, url);
	}

	public static void hsb(File image, String url, Float hue, Float saturation,
			Float brightness) {
		if (hue == null)
			hue = 0f;
		if (saturation == null)
			saturation = 0f;
		if (brightness == null)
			brightness = 0f;
		FilterOptions options = new FilterOptions().add(FilterOption.HUE, hue)
				.add(FilterOption.SATURATION, saturation)
				.add(FilterOption.BRIGHTNESS, brightness);
		CustomHSBFilter customHSBFilter = new CustomHSBFilter();
		executeFilter(customHSBFilter, options, image, url);
	}

	// public static void crystallize(String url, Float amount, Float
	// randomness, String edgeColor, Float edgeThickness, String shape, boolean
	// fadeEdges) {
	// try {
	// if (amount == null) amount = 0f;
	// if (randomness == null) randomness = 0f;
	// if (edgeThickness == null) edgeThickness = 0f;
	// if (StringUtils.isBlank(edgeColor)) edgeColor = "#000000";
	// int gridType = 2;
	// if (StringUtils.isBlank(shape)) shape = "";
	// if ("random".equals(shape.toLowerCase())) {
	// gridType = 0;
	// } else if ("square".equals(shape.toLowerCase())) {
	// gridType = 1;
	// } else if ("hexagonal".equals(shape.toLowerCase())) {
	// gridType = 2;
	// } else if ("octagonal".equals(shape.toLowerCase())) {
	// gridType = 3;
	// } else if ("triangular".equals(shape.toLowerCase())) {
	// gridType = 4;
	// } else {
	// throw new Exception("Invalid shape parameter value");
	// }
	//
	// new CrystallizeFilter().filter(
	// new URL(url),
	// response.out,
	// new FilterOptions().add(FilterOption.AMOUNT, amount).add(
	// FilterOption.RANDOMNESS, randomness).add(FilterOption.COLOR,
	// getRGB(edgeColor)).add(FilterOption.THICKNESS,
	// edgeThickness).add(FilterOption.GRID_TYPE,
	// gridType).add(FilterOption.FADE_EDGES, fadeEdges));
	// } catch (Exception e) {
	// handleError(e);
	// }
	// }

	public static void noise(File image, String url, Integer amount,
			Float density, String distribution, boolean monochrome) {
		int d = 0;
		if ("gaussian".equals(distribution)) {
			d = 0;
		} else if ("uniform".equals(distribution)) {
			d = 1;
		} else {
			d = 1;
		}
		if (amount == null)
			amount = 0;
		if (density == null)
			density = 0f;
		FilterOptions options = new FilterOptions()
				.add(FilterOption.AMOUNT, amount)
				.add(FilterOption.DENSITY, density)
				.add(FilterOption.DISTRIBUTION, d)
				.add(FilterOption.MONOCHROME, monochrome);
		NoiseFilter noiseFilter = new NoiseFilter();
		executeFilter(noiseFilter, options, image, url);
	}

	public static void mixchannels(File image, String url, Integer greenRed,
			Integer blueGreen, Integer redBlue, Integer intoRed,
			Integer intoGreen, Integer intoBlue) {
		if (blueGreen == null || blueGreen < 0)
			blueGreen = 0;
		if (greenRed == null || greenRed < 0)
			greenRed = 0;
		if (redBlue == null || redBlue < 0)
			redBlue = 0;
		if (intoRed == null || intoRed < 0)
			intoRed = 0;
		if (intoGreen == null || intoGreen < 0)
			intoGreen = 0;
		if (intoBlue == null || intoBlue < 0)
			intoBlue = 0;

		if (blueGreen > 255)
			blueGreen = 255;
		if (greenRed > 255)
			greenRed = 255;
		if (redBlue > 255)
			redBlue = 255;
		if (intoRed > 255)
			intoRed = 255;
		if (intoGreen > 255)
			intoGreen = 255;
		if (intoBlue > 255)
			intoBlue = 255;

		FilterOptions options = new FilterOptions()
				.add(FilterOption.BLUE_GREEN, blueGreen)
				.add(FilterOption.RED_BLUE, redBlue)
				.add(FilterOption.GREEN_RED, greenRed)
				.add(FilterOption.GREEN, intoGreen)
				.add(FilterOption.RED, intoRed)
				.add(FilterOption.BLUE, intoBlue);
		ChannelMixFilter channelMixFilter = new ChannelMixFilter();
		executeFilter(channelMixFilter, options, image, url);
	}

	public static void levels(File image, String url, Float highLevel,
			Float lowLevel, Float highOutputLevel, Float lowOutputLevel) {
		if (highLevel == null || highLevel < 0)
			highLevel = 0f;
		if (lowLevel == null || lowLevel < 0)
			lowLevel = 0f;
		if (highOutputLevel == null || highOutputLevel < 0)
			highOutputLevel = 0f;
		if (lowOutputLevel == null || lowOutputLevel < 0)
			lowOutputLevel = 0f;

		if (highLevel > 10)
			highLevel = 10f;
		if (lowLevel > 10)
			lowLevel = 10f;
		if (highOutputLevel > 10)
			highOutputLevel = 10f;
		if (lowOutputLevel > 10)
			lowOutputLevel = 10f;

		FilterOptions options = new FilterOptions()
				.add(FilterOption.HIGH_LEVEL, highLevel)
				.add(FilterOption.LOW_LEVEL, lowLevel)
				.add(FilterOption.HIGH_OUTPUT, highOutputLevel)
				.add(FilterOption.LOW_OUTPUT, lowOutputLevel);
		LevelsFilter levelsFilter = new LevelsFilter();
		executeFilter(levelsFilter, options, image, url);
	}

}
