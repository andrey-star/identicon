import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Identicon {

	static int size = 180;
	static int divisionQuantifier = 40;

	static Random random;

	static class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x() {
			return this.x;
		}

		public int y() {
			return this.y;
		}
	}

	static class Polygon {

		ArrayList<Point> vertices = new ArrayList<Point>();
		String color = generateColor();
		String stroke = generateColor();

		public Polygon() {

		}

		@SuppressWarnings("unchecked")
		public Polygon(ArrayList<Point> vertices) {
			this.vertices = (ArrayList<Point>) vertices.clone();
		}

		public void addPoint(Point a) {
			this.vertices.add(a);
		}

		public String color() {
			return this.color;
		}
		
		public String stroke() {
			return this.stroke;
		}

		public ArrayList<Point> vertices() {
			return this.vertices;
		}
	}

	public static String generateColor() {
		int c = random.nextInt(16777215);
		String color = Integer.toHexString(c);
		return "#" + color;
	}

	public static Point generatePoint(int size, int xBound, int yBound) {
		int x = xBound + random.nextInt(size);
		int y = yBound + random.nextInt(size);
		Point a = new Point(x, y);
		return a;
	}

	public static Point generatePointOnXBound(int size, int xBound, int yBound) {
		int x = xBound;
		int y = yBound + random.nextInt(size);
		Point a = new Point(x, y);
		return a;
	}

	public static Point generatePointOnYBound(int size, int xBound, int yBound) {
		int x = xBound + random.nextInt(size);
		int y = yBound;
		Point a = new Point(x, y);
		return a;
	}

	public static Polygon generatePolygon(int size, int xBound, int yBound, int vertices) {
		Polygon polygon = new Polygon();
		if (vertices == 4) {
			polygon.addPoint(generatePointOnXBound(size, xBound, yBound));
			polygon.addPoint(generatePointOnYBound(size, xBound, yBound));
			polygon.addPoint(generatePointOnXBound(size, xBound + size, yBound));
			polygon.addPoint(generatePointOnYBound(size, xBound, yBound + size));
		}
		return polygon;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter(new File("identicon.svg"));
		String username = in.next();
		random = new SecureRandom(username.getBytes());

		out.println( "<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<body>");
		
		for (int k = 0; k < 1; k++) {
			ArrayList<Polygon> image = new ArrayList<Polygon>();
			String imageColor = generateColor();

			int vertices = 4;

			for (int i = 0; i < divisionQuantifier; i++) {
				for (int j = 0; j < divisionQuantifier; j++) {
					image.add(generatePolygon(size / (divisionQuantifier), j * (size / (divisionQuantifier)),
							i * (size / (divisionQuantifier)), vertices));
				}
			}
			String svg = "<svg height=\"";
			svg += 2 * size + "\" width=\"" + 2 * size + "\" style=\"background:" + imageColor + "\">\n";
			random = new SecureRandom();
			for (int i = 0; i < image.size(); i++) {
				Polygon polygon = image.get(i);
				svg += "<polygon points=\"";
				for (int j = 0; j < polygon.vertices.size(); j++) {
					Point point = polygon.vertices.get(j);
					svg += point.x + "," + point.y + " ";
				}
				svg += " \"style=\"fill:" + polygon.color + ";stroke:" + polygon.stroke + ";stroke-width:1\" />";
				svg += "\n";

				svg += "<polygon points=\"";
				for (int j = 0; j < polygon.vertices.size(); j++) {
					Point point = polygon.vertices.get(j);
					svg += (2 * size - point.x) + "," + point.y + " ";
				}
				svg += " \"style=\"fill:" + polygon.color + ";stroke:" + polygon.stroke + ";stroke-width:1\" />";
				svg += "\n";

				svg += "<polygon points=\"";
				for (int j = 0; j < polygon.vertices.size(); j++) {
					Point point = polygon.vertices.get(j);
					svg += (2 * size - point.x) + "," + (2 * size - point.y) + " ";
				}
				svg += " \"style=\"fill:" + polygon.color + ";stroke:" + polygon.stroke + ";stroke-width:1\" />";
				svg += "\n";

				svg += "<polygon points=\"";
				for (int j = 0; j < polygon.vertices.size(); j++) {
					Point point = polygon.vertices.get(j);
					svg += point.x + "," + (2 * size - point.y) + " ";
				}
				svg += " \"style=\"fill:" + polygon.color + ";stroke:" + polygon.stroke + ";stroke-width:1\" />";
				svg += "\n";
			}

			svg += "</svg>\n";
			out.println(svg);
		}
		out.println(" \r\n" + 
				"</body>\r\n" + 
				"</html>");
		out.close();
		in.close();
	}

}