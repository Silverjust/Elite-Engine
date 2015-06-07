package game;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ContentListHandler;
import shared.ref;
import entity.Entity;

public class ImageHandler {

	static String dataPath;
	public static ArrayList<PImage> imagesToLoad = new ArrayList<PImage>();

	public static int nImagesToLoad;

	public static ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

	public static boolean requestAllImages() {
		try {
			dataPath = "data/";

			/*
			 * Reflections reflections = new Reflections("entity"); Set<Class<?
			 * extends ImgLoading>> classes = reflections
			 * .getSubTypesOf(ImgLoading.class);
			 */

			classes.add(Entity.class);
			classes.add(GameDrawer.class);
			classes.add(HUD.class);
			classes.add(AimHandler.class);

			ContentListHandler.getEntityContent().keys();
			for (Object o : ContentListHandler.getEntityContent().keys()) {
				String s = ContentListHandler.getEntityContent().getString(
						(String) o);
				classes.add(Class.forName(s));
			}

			// PApplet.printArray(classes);

			for (Class<?> c : classes) {
				Method m = c.getDeclaredMethod("loadImages");
				m.invoke(null);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static float stateOfLoading() {
		int loadedImages = 0;
		boolean error = false;
		for (PImage i : imagesToLoad) {
			if (i.width == -1) {
				System.err.println("Image error");
				error = true;
			}
			if (i.width > 0) {
				loadedImages++;
			}

		}
		if (error)
			loadedImages = -1;
		return (float) (loadedImages) / nImagesToLoad;
	}

	public static PImage[][] load(String path, String name, char animation,
			byte directions, byte iterations) {
		PImage[][] imageArray = new PImage[directions][iterations];
		for (int d = 0; d < directions || directions == 0 && d == 0; d++) {
			for (int i = 0; i < iterations || iterations == 0 && i == 0; i++) {
				nImagesToLoad++;
				String s = dataPath + path + name
						+ (animation != 0 ? "_" + animation : "")
						+ (directions != 0 ? "_" + d : "")
						+ (iterations != 0 ? "_" + PApplet.nf(i, 4) : "")
						+ ".png";
				System.out.println(s);
				s = getPath(s);
				imageArray[d][i] = ref.app.requestImage(s);
				imagesToLoad.add(imageArray[d][i]);
			}
		}
		return imageArray;
	}

	public static PImage[] load(String path, String name, char animation,
			byte iterations) {
		PImage[] imageArray = new PImage[iterations];
		for (int i = 0; i < iterations || iterations == 0 && i == 0; i++) {
			nImagesToLoad++;
			String s = dataPath + path + name
					+ (animation != 0 ? "_" + animation : "")
					+ (iterations != 0 ? "_" + PApplet.nf(i, 4) : "") + ".png";
			System.out.println(s);
			s = getPath(s);
			imageArray[i] = ref.app.requestImage(s);
			imagesToLoad.add(imageArray[i]);
		}

		return imageArray;
	}

	public static PImage load(String path, String name, char animation) {
		PImage image;
		nImagesToLoad++;
		String s = dataPath + path + name
				+ (animation != 0 ? "_" + animation : "") + ".png";
		System.out.println(s);
		s = getPath(s);
		image = ref.app.requestImage(s);
		imagesToLoad.add(image);
		return image;
	}

	public static PImage load(String path, String name) {
		PImage image;
		nImagesToLoad++;
		String s = dataPath + path + name + ".png";
		System.out.println(s);
		s = getPath(s);
		image = ref.app.requestImage(s);
		imagesToLoad.add(image);
		return image;
	}

	private static String getPath(String path) {
		path = ref.app.getClass().getClassLoader().getResource(path).getFile();
		return path;
	}

	static String getClassPath(Object o) {
		String pkg = o.getClass().getEnclosingClass().getCanonicalName();

		int pos = pkg.lastIndexOf("."); // Slash before the class name
		if (pos == -1)
			return ""; // No package
		pkg = pkg.substring(0, pos + 1); // Keep the ending dot
		String cp = pkg.replaceAll("\\.", "/");
		return cp;

	}

	public static String getBinaryPath() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		File classpathRoot = new File(classLoader.getResource("").getPath());
		return classpathRoot.getPath() + "\\";
	}
}
