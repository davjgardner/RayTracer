package tracer;

import geom.*;
import geom.Shape;
import light.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import static geom.Shape.EPSILON;

/**
 * Implements a basic ray-tracer.
 */
public class TracerMain {
	
	/**
	 * Field of view (not currently used)
	 */
	private static final float FOV = (float) Math.toRadians(60);
	
	/**
	 * Screen width (pixels)
	 */
	private static final int width = 1000;
	/**
	 * Screen height (pixels)
	 */
	private static final int height = 1000;
	
	/**
	 * Supersampling constant: multiplied by width and height
	 */
	private static final float supersample = 1f;
	
	/**
	 * Maximum times a ray is allowed to reflect or refract
	 */
	private static final int MAX_BOUNCES = 10;
	
	private BufferedImage img;
	private JFrame frame;
	
	private List<Shape> objects = new ArrayList<>();
	private List<Light> lights = new ArrayList<>();
	
	private SpaceTree tree;
	
	public TracerMain() {
		frame = new JFrame("Ray Tracer") {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, width, height, null);
			}
		};
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		img = new BufferedImage((int) (width * supersample), (int) (height * supersample), BufferedImage.TYPE_INT_RGB);
		
		// Initialize objects
		objects.add(new Sphere(new Vector3f(1.0f, 0.0f, -9f), 1.0f,
				new Material(Color3f.red, 1.0f)));
		
		objects.add(new Sphere(new Vector3f(-1.0f, 0.0f, -9f), 1.0f,
				new Material(Color3f.blue, 1.0f)));
		
		objects.add(new Sphere(new Vector3f(0.0f, 2.0f, -9f), 1.0f,
				new Material(Color3f.green, 1.0f)));
		
		Plane back = new Plane(new Vector3f(0.0f, 0.0f, 1.0f),
				new Vector3f(0.0f, 0.0f, -10.0f),
				new Material(new Color3f(0.5f, 0.2f, 0.7f), 1.0f));
		objects.add(back);
		
		// Glass Sphere
		objects.add(new Sphere(new Vector3f(0.5f, 0.4f, -3.0f), 0.3f,
				new Material(Color3f.black, 1.0f, 1.3f)));
		
		
		// Depth of Field images (takes several minutes to render due to number of objects)
		/*objects.add(new Sphere(new Vector3f(1.0f, 1.5f, -6f), 0.8f,
				new Material(Color3f.black, 1.0f, 1.1f)));
		
		objects.add(new Sphere(new Vector3f(-1.0f, 1.0f, -6f), 0.5f,
				new Material(Color3f.purple, 1.0f)));
		
		Plane ground = new Plane(new Vector3f(0.0f, 1.0f, 0.0f),
				new Vector3f(0.0f, -2.0f, 0.0f),
				new Material(new Color3f(0.9f), 0.0f));
		objects.add(ground);
		
		Random rand = new Random();
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 20; j++) {
				objects.add(new Sphere(new Vector3f(i * 0.3f - 4.5f, j * 0.3f, -11.0f), 0.15f,
						new Material(new Color3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())
								, 0.0f)));
			}
		}*/
		
		// Point Lights for hard shadows
		/*PointLight pl = new PointLight(new Vector3f(0, 6.0f, -2.0f), new Color3f(1.0f));
		lights.add(pl);
		
		PointLight pl2 = new PointLight(new Vector3f(4.0f, 0.5f, -2.0f), new Color3f(0.3f));
		lights.add(pl2);*/
		
		//lights.add(new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f)));
		
		// Add this light instead of the PointLights for soft shadows
		DiskLight dl = new DiskLight(new Vector3f(0, 0.0f, -5.0f),
				new Vector3f(0, -1.0f, 1.0f).normalize(), 0.5f,
				Color3f.white);
		lights.add(dl);
		
		// Create spatial partitioning tree
		tree = new SpaceTree(objects, new Vector3f(), new Vector3f(20.0f));
		tree.createTree(SpaceTree.X, 0);
		tree.print(0);
		
		// Normal render call
		render();
		
		// Depth-of-Field render call
		//renderDOF(0.6f, 6f, 20);
		
		frame.repaint();
		frame.setVisible(true);
	}
	
	/**
	 * Casts a ray passing through the given point in screen coordinates
	 * @param sx screen coord x
	 * @param sy screen coord y
	 * @return the Ray passing through (x, y)
	 */
	public Ray cast(float sx, float sy) {
		sy = (height * supersample) - sy - 1;
		sx -= 0.5f;
		sy -= 0.5f;
		float x = sx/(width * supersample) * 2f - 1f;
		float y = sy/(height * supersample) * 2f - 1f;
		float z = -1 / (float) Math.tan(FOV / 2);
		Vector3f dir = new Vector3f(x, y, z).normalize();
		return new Ray(new Vector3f(), dir);
	}
	
	
	/**
	 * Renders the image using a simple pinhole camera model
	 */
	public void render() {
		for (int x=0; x<width * supersample; x++) {
			for (int y=0; y<height * supersample; y++) {
				Color3f c = trace(cast(x, y), 1.0f, 1);
				img.setRGB(x, y, c.getRGB());
			}
			int p = (int) (((float) x / (width * supersample)) * 100f);
			System.out.println(p + "%");
		}
	}
	
	/**
	 * Renders the image using a camera with a real aperture
	 * @param aperture radius of lens disk
	 * @param dof distance to focal plane
	 * @param samples number of secondary rays
	 */
	public void renderDOF(float aperture, float dof, int samples) {
		DiskLight lens = new DiskLight(new Vector3f(), new Vector3f(0.0f, 0.0f, -1.0f),
				aperture, null);
		float invSamples = 1.0f / (float) samples;
		for (int x = 0; x < width * supersample; x++) {
			for (int y = 0; y < height * supersample; y++) {
				// Calculate and cast the primary ray (same as pinhole model)
				Ray primRay = cast(x, y);
				Color3f primColor = trace(primRay, 1.0f, 1);
				
				Vector3f focalPoint = primRay.getPoint(dof);
				Color3f secColor = new Color3f();
				// Cast out [samples] secondary rays and average their colors
				for (int i = 0; i < samples; i++) {
					Vector3f pos = lens.samplePoint();
					Ray secRay = new Ray(pos, focalPoint.sub(pos));
					Color3f c = trace(secRay, 1.0f, 1);
					secColor.addThis(c.mul(invSamples));
				}
				// blend of primary and secondary colors - tunable
				Color3f outColor = primColor.mul(0.3f).add(secColor.mul(0.7f));
				img.setRGB(x, y, outColor.getRGB());
			}
			int p = (int) (((float) x / (width * supersample)) * 100f);
			System.out.println(p + "%");
			
		}
	}
	
	/**
	 * Use Snell's law to calculate the direction of refraction
	 * @param in incoming ray
	 * @param normal surface normal
	 * @param n1 origin material index of refraction
	 * @param n2 destination material index of refraction
	 * @return direction of refracted ray
	 */
	private Vector3f refract(Ray in, Vector3f normal, float n1, float n2) {
		normal = normal.normalize();
		Vector3f dir = in.direction.normalize();
		float r = n1 / n2;
		float c = - normal.dot(dir);
		if (c < EPSILON) {
			normal = normal.negate();
			c = - normal.dot(dir);
		}
		// Check for total internal reflection
		float tir = 1 - r * r * (1 - c * c);
		if (tir < EPSILON) {
			System.out.println("TIR");
			System.out.println("r = " + r);
			System.out.println("c = " + c);
			return null;
		}
		return dir.mul(r).add(normal.mul(r * c - (float) Math.sqrt(tir)));
	}
	
	/**
	 * Use Schlick's approximation of the Fresnel factor to calculate reflectance
	 * @param in incoming ray
	 * @param normal surface normal
	 * @param n1 origin material index of refraction
	 * @param n2 destination material index of refraction
	 * @return reflectance coefficient
	 */
	private float calcReflectance(Ray in, Vector3f normal, float n1, float n2) {
		Vector3f dir = in.direction.normalize();
		normal = normal.normalize();
		// r0 = reflection coefficient for a ray parallel to normal
		if (normal.dot(dir) < EPSILON) normal = normal.negate();
		float r0 = (n1 - n2) / (n1 + n2) * (n1 - n2) / (n1 + n2);
		float f = 1 - normal.dot(dir); // 1 - cos(theta)
		float r = r0 + (1 - r0) * f * f * f * f * f; // (1 - cos(theta))^5
		return r;
	}
	
	/**
	 * Traces the given ray through the scene until att is less than <code>ATT_MIN</code>
	 * @param ray the ray to trace
	 * @param att current attenuation value
	 * @param bounces number of bounces this ray has taken already
	 * @return the color this ray yields
	 */
	public Color3f trace(Ray ray, float att, int bounces) {
		if (bounces > MAX_BOUNCES) return new Color3f(0);
		/*
		 * for Object o in objects
		 *  collide ray with o
		 *  if no collision then continue
		 *  else
		 *   color += cast rays to light sources
		 *   color += trace reflection ray
		 *   return Color3f * att
		 */
		float mint = -1;
		Shape obj = null;
		// find closest object
		List<Shape> checkList = tree.getCheckList(ray);
		for (Shape s : checkList) {
			//System.out.print(s + ", ");
			float t = s.collides(ray);
			if (t > 0) {
				if (t < mint || obj == null) {
					mint = t;
					obj = s;
				}
			}
		}
		if(obj == null) return Color3f.black;
		// do light calculation
		Vector3f pos = ray.origin.add(ray.direction.mul(mint));
		Vector3f normal = obj.normalAt(pos);
		Color3f lColor = new Color3f();
		for (Light l : lights) {
			lColor.addThis(l.traceLight(pos, normal, obj.m, objects));
		}
		
		float reflectance = obj.m.reflectance;
		
		// do refraction
		Color3f refrColor = new Color3f(0.0f);
		if (obj.m.refractionIndex != 0) {
			float n1, n2;
			// check if light is going into or out of the object
			if (ray.direction.dot(normal) < EPSILON) {
				// going out of the object
				n2 = obj.m.refractionIndex;
				n1 = 1.0f; // air
			} else {
				// going into the object
				n2 = 1.0f;
				n1 = obj.m.refractionIndex;
			}
			float reflCoeff = calcReflectance(ray, normal, n1, n2);
			reflectance *= reflCoeff;
			Vector3f refract = refract(ray, normal, n1, n2);
			if (refract != null) {
				refrColor = trace(new Ray(pos, refract), att * 0.75f,
						bounces + 1).mulThis(1 - reflCoeff);
			} else refrColor = Color3f.red;
			lColor = new Color3f(1); // transparent objects don't care about light
		}
		
		// do reflection
		Color3f refColor = new Color3f(0.0f);
		if (obj.m.reflectance > 0) {
			Vector3f reflect = ray.direction.reflect(normal);
			refColor = trace(new Ray(pos, reflect), att * 0.75f,
					bounces + 1).mulThis(reflectance);
		}
		
		return lColor.mul(obj.m.color.mul(att).add(refColor).add(refrColor));
	}
	
	public static void main(String[] args) {
		new TracerMain();
	}
}
