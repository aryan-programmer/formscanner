package com.albertoborsetta.formscanner;

import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.api.commons.Constants;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ImageDrawer {
	static final         double          zoom      = 1;
	static final         double          border    = 0;
	private static final HashSet<String> prevPaths = new HashSet<>();

	public static void showPoints(Graphics2D g, ArrayList<FormPoint> points, FormScannerModel model) {
		if(!points.isEmpty()) {
			for(FormPoint point : points) {
				showPoint(g, point, model/*, isTemp*/);
			}
		}
	}

//	public static void showPoints(Graphics2D g, ArrayList<FormPoint> points, FormScannerModel model, boolean isTemp) {
//		if(!points.isEmpty()) {
//			for(FormPoint point : points) {
//				showPoint(g, point, model, isTemp);
//			}
//		}
//	}

	public static void showPoint(Graphics2D g, FormPoint point, FormScannerModel model/*, boolean isTemp*/) {
		if(point != null) {
			int x = (int) ((point.getX() * zoom) - border);
			int y = (int) ((point.getY() * zoom) - border);

			g.setColor(Color.RED);
			int marker = model.getShapeSize();

			if(model.getShapeType().equals(Constants.ShapeType.CIRCLE)) {
				g.fillArc(x - marker, y - marker, 2 * marker,
				          2 * marker, 0, 360
				);
			} else {
				g.fillRect(x - marker, y - marker, 2 * marker,
				           2 * marker
				);
			}
			//g.setColor(Color.BLACK);
		}
	}

	public static void showCorners(Graphics2D g, FormTemplate template) {
		HashMap<Constants.Corners, FormPoint> corners = template.getCorners();
		if(corners.isEmpty()) {
			return;
		}
		showArea(g, corners, Color.GREEN);
	}

	public static void showArea(Graphics2D g, HashMap<Constants.Corners, FormPoint> points, Color color) {
		g.setColor(color);

		for(int i = 0; i < Constants.Corners.values().length; i++) {
			FormPoint p1 = points.get(Constants.Corners.values()[i % Constants.Corners.values().length]);
			FormPoint p2 = points.get(Constants.Corners.values()[(i + 1) % Constants.Corners.values().length]);

			g.drawLine((int) (p1.getX() * zoom), (int) (p1.getY() * zoom),
			           (int) (p2.getX() * zoom), (int) (p2.getY() * zoom)
			);
		}

		g.setColor(Color.BLACK);
	}

	public static void saveImageRelativeTo(File imageFile, BufferedImage imageRes, String sfx) throws IOException {
		String filePath    = FilenameUtils.getFullPath(imageFile.getAbsolutePath());
		String extension   = FilenameUtils.getExtension(imageFile.getAbsolutePath());
		String imgName     = imageFile.getName();
		String newFilePath = filePath + "parsed_images_" + sfx + "/";
		if(prevPaths.add(newFilePath)) {
			Files.createDirectories(Paths.get(newFilePath));
		}
		String pathname = newFilePath + (
			"".equals(extension) ?
			imgName :
			imgName.substring(0, imgName.length() - extension.length() - 1)
		) + ".png";
		ImageIO.write(
			imageRes,
			"png",
			new File(pathname)
		);
	}

	public static BufferedImage getParsedImage(BufferedImage image, FormTemplate filledForm, FormScannerModel model) {
		BufferedImage imageRes = new BufferedImage(
			image.getWidth(),
			image.getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D graphics = imageRes.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		ImageDrawer.showPoints(graphics, filledForm.getFieldPoints(), model);
		ImageDrawer.showCorners(graphics, filledForm);
		return imageRes;
	}
}
