package tracer;

import geom.*;
import geom.Shape;
import light.*;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

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
	
	int supersample = 1;
	
	
	BufferedImage img;
	JFrame frame;
	
	List<Shape> objects = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	
	
	public TracerMain() {
		frame = new JFrame("Ray Tracer") {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, width, height, null);
			}
		};
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		img = new BufferedImage(width * supersample, height * supersample, BufferedImage.TYPE_INT_RGB);
		
		// Initialize objects
		objects.add(new Sphere(new Vector3f(1, 0.0f, -4f), 1.0f,
				new Material(Color3f.red, 1.0f)));
		
		objects.add(new Sphere(new Vector3f(-1.0f, 0.0f, -4f), 1.0f,
				new Material(Color3f.blue, 1.0f)));
		
		objects.add(new Sphere(new Vector3f(0.0f, 2.0f, -4f), 1.0f,
				new Material(Color3f.green, 1.0f)));
		
		Plane ground = new Plane(new Vector3f(0.0f, 1.0f, 0.0f),
				new Vector3f(0.0f, -2.0f, 0.0f),
				new Material(new Color3f(0.3f, 0.7f, 0.2f), 0.3f));
		objects.add(ground);
		Plane back = new Plane(new Vector3f(0.0f, 0.0f, 1.0f),
				new Vector3f(0.0f, 0.0f, -10.0f),
				new Material(new Color3f(0.5f, 0.2f, 0.7f), 0.3f));
//		objects.add(back);
		Plane top = new Plane(new Vector3f(0.0f, -1.0f, 0.0f),
				new Vector3f(0.0f, 2.0f, 0.0f),
				new Material(new Color3f(0.2f, 0.5f, 0.5f), 0.3f));
//		objects.add(top);
		
		
		PointLight pl = new PointLight(new Vector3f(0, 1.0f, 3.0f), Color3f.white.mul(0.5f));
		lights.add(pl);
		
		Shape me = new Sphere(new Vector3f(0, 0, 0), 0.3f,
				new Material(Color3f.black, 0f));
		//objects.add(me);
		
//		lights.add(new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f)));
		
		DiskLight dl = new DiskLight(new Vector3f(0, 1.0f, -1.0f),
				new Vector3f(0, -1.0f, 1.0f).normalize(), 0.5f,
				Color3f.white);
		//lights.add(dl);
		
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
		sy = (height * supersample) - sy - 1;
		sx -= 0.5f;
		sy -= 0.5f;
		float x = sx/(width * supersample) * 2f - 1f;
		float y = sy/(height * supersample) * 2f - 1f;
		Vector3f dir = new Vector3f(x, y, -1).normalize();
		return new Ray(new Vector3f(), dir);
	}
	
	
	/**
	 * Renders the image
	 */
	public void render() {
		for (int x=0; x<width * supersample; x++) {
			for (int y=0; y<height * supersample; y++) {
				Color3f c = traceIterative(cast(x, y), 0.9f, 20);
				//if (x % 50 == 0 && y % 50 == 0)
					//System.out.println(c);
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
			lColor.addThis(l.traceLight(pos, normal, obj.m, objects));
			//lColor.addThis(traceLight(pos, normal, obj.m, l));
		}
		
		// do reflection
		Vector3f reflect = new Vector3f(ray.direction).reflect(normal);
		Color3f refColor = trace(new Ray(pos, reflect), att * 0.75f).mulThis(obj.m.reflectance);
		return lColor.mul(obj.m.color.mul(att).add(refColor));
	}
	
	public Color3f traceIterative(Ray ray, float att, int bounceLimit) {
		Color3f resColor = new Color3f();
		Stack<Ray> rays = new Stack<>();
		Stack<Integer> bounces = new Stack<>();
		Stack<Color3f> multipliers = new Stack<>();
		rays.add(ray);
		bounces.add(1);
		multipliers.add(new Color3f(1.0f));
		while(!rays.isEmpty()) {
			Ray r = rays.pop();
			int b = bounces.pop();
			Color3f multiplier = multipliers.pop();
			if (b > bounceLimit) return resColor;
			
			// find closest object
			float mint = -1;
			Shape obj = null;
			for(Shape s : objects) {
				float t = s.collides(r);
				if (t > 0) {
					if (t < mint || obj == null) {
						mint = t;
						obj = s;
					}
				}
			}
			if (obj == null) continue;
			
			// do light calculation
			Vector3f pos = new Vector3f(r.origin).add(new Vector3f(ray.direction).mul(mint));
			Vector3f normal = new Vector3f(obj.normalAt(pos));
			Color3f lColor = new Color3f();
			for (Light l : lights) {
				lColor.addThis(l.traceLight(pos, normal, obj.m, objects));
			}
			// multiply the base color by light color and attenuation, and
			// multiply the result by the parent's light times reflectance
			Color3f oColor = obj.m.color.mul((float) Math.pow(att, b));
			resColor.addThis(lColor.mul(oColor).mul(multiplier));
			
			// do reflection
			Vector3f reflect = new Vector3f(ray.direction).reflect(normal);
			rays.push(new Ray(pos, reflect));
			bounces.push(b + 1);
			// push the light color times reflectance onto the stack to multiply the next color
			multipliers.push(lColor.mul(obj.m.reflectance));
			
			// TODO: do refraction, same way
		}
		return resColor;
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
