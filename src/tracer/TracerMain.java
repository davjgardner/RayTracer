package tracer;

import geom.*;
import light.Color3f;
import light.Light;
import light.Material;
import light.PointLight;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

/**
 * Implements a basic ray-tracer.
 */
public class TracerMain {
	
	/**
	 * Minimum attenuation before no further tracing is done
	 */
	static final float ATT_MIN = 0.0001f;
	/**
	 * Field of view (not currently used)
	 */
	static final float FOV = (float) Math.toRadians(60);
	
	/**
	 * Screen width (pixels)
	 */
	int width = 1000;
	/**
	 * Screen height (pixels)
	 */
	int height = 1000;
	
	
	BufferedImage img;
	JFrame frame;
	
	List<Shape> objects = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	
	
	public TracerMain() {
		frame = new JFrame("Ray Tracer") {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, width * 1, height * 1, null);
			}
		};
		frame.setSize(width * 1, height * 1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Initialize objects
		objects.add(new Sphere(new Vector3f(1, 0, -6f), 1.0f,
				new Material(Color3f.red, 1.0f)));
		
		objects.add(new Sphere(new Vector3f(-1.0f, 0.0f, -6f), 1.0f,
				new Material(Color3f.blue, 1.0f)));
		
		Plane ground = new Plane(new Vector3f(0.0f, 1.0f, 0.0f),
				new Vector3f(0.0f, -2.0f, 0.0f),
				new Material(new Color3f(0.3f, 0.3f, 0.2f), 0.3f));
		objects.add(ground);
		
		PointLight light = new PointLight(new Vector3f(0, 1.0f, -1.0f), Color3f.white);
		lights.add(light);
		
		//lights.add(new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f)));
		
		render();
		
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
		sy = height - sy - 1;
		sx -= 0.5f;
		sy -= 0.5f;
		float x = sx/width * 2f - 1f;
		float y = sy/height * 2f - 1f;
		Vector3f dir = new Vector3f(x, y, -1).normalize();
		return new Ray(new Vector3f(), dir);
	}
	
	
	/**
	 * Renders the image
	 */
	public void render() {
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				Color3f c = trace(cast(x, y), 1.0f);
				img.setRGB(x, y, c.getRGB());
			}
		}
	}
	
	/**
	 * Traces the given ray through the scene until att is less than <code>ATT_MIN</code>
	 * @param ray the ray to trace
	 * @param att current attenuation value
	 * @return the color this ray yields
	 */
	public Color3f trace(Ray ray, float att) {
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
		for (Shape s : objects) {
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
		Vector3f pos = new Vector3f(ray.origin).add(new Vector3f(ray.direction).mul(mint));
		Vector3f normal = obj.normalAt(pos);
		Color3f lColor = new Color3f();
		for (Light l : lights) {
			
			lColor.addThis(traceLight(pos, normal, obj.m, l));
		}
		
		// do reflection
		Vector3f reflect = new Vector3f(ray.direction).reflect(normal);
		Color3f refColor = trace(new Ray(pos, reflect), att * 0.75f).mulThis(obj.m.reflectance);
		return lColor.mul(obj.m.color.mul(att).add(refColor));
	}
	
	/**
	 * Calculate the color contribution of a light on a position.
	 * @param pos position to calculate for
	 * @param normal normal vector of the object at <code>pos</code>
	 * @param m material of the object at <code>pos</code>
	 * @param light light to calculate color for
	 * @return the color contribution of <code>light</code>
	 */
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m, Light light) {
		Ray toLight = new Ray(pos, new Vector3f(light.pos).sub(pos).normalize());
		for (Shape obj : objects) {
			float t = obj.collides(toLight);
			if (t > 0) return Color3f.black;
		}
		//TODO: make light.calcColor do more work so that DirectionalLight and AmbientLight work
		return light.calcColor(pos, normal, m);
	}
	
	public static void main(String[] args) {
		new TracerMain();
	}
}
