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

import static geom.Shape.EPSILON;

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
	static final int width = 1000;
	/**
	 * Screen height (pixels)
	 */
	static final int height = 1000;
	
	static final float supersample = 1f;
	
	private static final int MAX_BOUNCES = 10;
	
	BufferedImage img;
	JFrame frame;
	
	List<Shape> objects = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	
	SpaceTree tree;
	
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
		objects.add(new Sphere(new Vector3f(1.0f, 0.0f, -6f), 1.0f,
				new Material(Color3f.red, 1.0f)));
		
		objects.add(new Sphere(new Vector3f(-1.0f, 0.0f, -6f), 1.0f,
				new Material(Color3f.blue, 1.0f)));
		
		objects.add(new Sphere(new Vector3f(0.0f, 2.0f, -6f), 1.0f,
				new Material(Color3f.green, 1.0f)));
		
		
		Plane ground = new Plane(new Vector3f(0.0f, 1.0f, 0.0f),
				new Vector3f(0.0f, -2.0f, 0.0f),
//				new Material(new Color3f(0.3f, 0.7f, 0.2f), 0.3f));
				new Material(new Color3f(0.9f), 1.0f));
		objects.add(ground);
		Plane back = new Plane(new Vector3f(0.0f, 0.0f, 1.0f),
				new Vector3f(0.0f, 0.0f, -10.0f),
				new Material(new Color3f(0.5f, 0.2f, 0.7f), 1.0f));
		objects.add(back);
		Plane top = new Plane(new Vector3f(0.0f, -1.0f, 0.0f),
				new Vector3f(0.0f, 4.0f, 0.0f),
				//new Material(new Color3f(0.2f, 0.5f, 0.5f), 0.0f));
				new Material(new Color3f(1.0f, 0.0f, 0.0f), 1.0f));
		objects.add(top);
		Plane front = new Plane(new Vector3f(0.0f, 0.0f, -1.0f),
				new Vector3f(0.0f, 0.0f, 10.0f),
				new Material(new Color3f(0.2f, 0.5f, 0.5f), 1.0f));
		objects.add(front);
		Plane right = new Plane(new Vector3f(-1.0f, 0.0f, 0.0f),
				new Vector3f(2.0f, 0.0f, 0.0f),
				new Material(new Color3f(0.2f, 0.7f, 0.2f), 1.0f));
		objects.add(right);
		Plane left = new Plane(new Vector3f(1.0f, 0.0f, 0.0f),
				new Vector3f(-2.0f, 0.0f, 0.0f),
				new Material(new Color3f(0.8f, 0.7f, 0.2f), 1.0f));
		objects.add(left);
		
		/*Plane water = new Plane(new Vector3f(0.0f, 0.0f, 1.0f),
				new Vector3f(0.0f, 0.0f, -4.0f),
				new Material(new Color3f(0), 1.0f, 1.5f));
		objects.add(water);*/
		
		objects.add(new Sphere(new Vector3f(0.0f, 0.3f, -2.0f), 0.4f,
				new Material(Color3f.black, 1.0f, 1.5f)));
				
		
		PointLight pl = new PointLight(new Vector3f(0, 1.0f, -2.0f), new Color3f(0.8f));
		lights.add(pl);
		
		/*Sphere s1 = new Sphere(new Vector3f(-2.0f, 0.0f, -3.0f), 0.5f,
				new Material(Color3f.red, 0.0f));
		objects.add(s1);
		
		Sphere s2 = new Sphere(new Vector3f(2.0f, -1.0f, -3.0f), 0.5f,
				new Material(Color3f.blue, 0.0f));
		objects.add(s2);
		
		Sphere s3 = new Sphere(new Vector3f(2.0f, 1.0f, -3.0f), 0.5f,
				new Material(Color3f.green, 0.0f));
//		objects.add(s3);
		
		Sphere s4 = new Sphere(new Vector3f(2.0f, 0.0f, -3.0f), 0.5f,
				new Material(Color3f.purple, 0.0f));
		//objects.add(s4);
		
		Sphere s5 = new Sphere(new Vector3f(0.0f, 0.0f, -3.0f), 0.5f,
				new Material(Color3f.purple, 0.0f));*/
//		objects.add(s5);
		
		/*Sphere s6 = new Sphere(new Vector3f(1.0f, 1.0f, -3.0f), 0.5f,
				new Material(Color3f.purple, 0.0f));
		objects.add(s6);
		
		Sphere s7 = new Sphere(new Vector3f(1.0f, -1.0f, -3.0f), 0.5f,
				new Material(Color3f.purple, 0.0f));
		objects.add(s7);
		
		Sphere s8 = new Sphere(new Vector3f(-1.0f, 1.0f, -3.0f), 0.5f,
				new Material(Color3f.purple, 0.0f));
		objects.add(s8);
		
		Sphere s9 = new Sphere(new Vector3f(-1.0f, -1.0f, -3.0f), 0.5f,
				new Material(Color3f.purple, 0.0f));
		objects.add(s9);*/
		
		AlignedBox box = new AlignedBox(new Vector3f(5.0f, 5.0f, 0f), 10.0f, 10.0f, 20.0f,
				new Material(Color3f.white, 0.0f));
//		objects.add(box);
		
		//		lights.add(new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f)));
		
		DiskLight dl = new DiskLight(new Vector3f(0, 3.0f, -2.0f),
				new Vector3f(0, -1.0f, 1.0f).normalize(), 0.5f,
				Color3f.white);
//		lights.add(dl);
		
		tree = new SpaceTree(objects, new Vector3f(), new Vector3f(20.0f));
		tree.createTree(SpaceTree.X, 0);
		tree.print(0);
		
		img.getGraphics().drawRect(0, 0, img.getWidth(), img.getHeight());
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
		float z = -1 / (float) Math.tan(FOV / 2);
		Vector3f dir = new Vector3f(x, y, z).normalize();
		return new Ray(new Vector3f(), dir);
	}
	
	
	/**
	 * Renders the image
	 */
	public void render() {
		float k = 0;
		float total = width * height;
		for (int x=0; x<width * supersample; x++) {
			for (int y=0; y<height * supersample; y++) {
//				Color3f c = traceIterative(cast(x, y), 0.9f, 20);
				Color3f c = trace(cast(x, y), 1.0f, 1);
				//if (x % 50 == 0 && y % 50 == 0)
					//System.out.println(c);
				img.setRGB(x, y, c.getRGB());
				
				k++;
			}
			int p;
			if ((p = (int) (((float) x / (float) (width * supersample)) * 100f)) % 10 == 0) {
				System.out.println(p + "%");
			}
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
		normal = new Vector3f(normal).normalize();
		Vector3f dir = new Vector3f(in.direction).normalize();
		float r = n1 / n2;
		float c = - normal.dot(dir);
		if (c < EPSILON) {
			normal = new Vector3f(normal).negate();
			c = - normal.dot(dir);
		}
		// Check for total internal reflection
		float tir = 1 - r * r * (1 - c * c);
//		if (tir < 0) return new Vector3f(in.direction).reflect(new Vector3f(normal).negate());
//		System.out.println("r = " + r);
//		System.out.println("c = " + c);
		if (tir < EPSILON) {
			System.out.println("TIR");
			System.out.println("r = " + r);
			System.out.println("c = " + c);
			return null;
		}
		return new Vector3f(dir).mul(r).add(
				new Vector3f(normal).mul(r * c - (float) Math.sqrt(tir)));
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
		Vector3f dir = new Vector3f(in.direction).normalize();
		normal = new Vector3f(normal).normalize();
		// r0 = reflection coefficient for a ray parallel to normal
		if (normal.dot(dir) < EPSILON) normal = normal.negate();
		float r0 = (n1 - n2) / (n1 + n2) * (n1 - n2) / (n1 + n2);
		float f = 1 - normal.dot(dir); // 1 - cos(theta)
		float r = r0 + (1 - r0) * f * f * f * f * f; // (1 - cos(theta))^5
//		System.out.println("r0 = " + r0);
//		System.out.println("f = " + f);
//		System.out.println("r = " + r);
		return r;
	}
	
	private float fresnel(Ray in, Vector3f normal, float n1, float n2) {
		float n = n1 / n2;
		float cosi = in.direction.dot(normal);
		float sini = (float) Math.sqrt(1 - cosi * cosi);
		float rad = 1 - n * n  * (1 - cosi * cosi);
		if (rad < EPSILON) System.out.println("TIR");
		float cost = (float) Math.sqrt(rad);
		/*float rs = (n1 * cosi - n2 * (float) Math.sqrt(1 - (n * sini) * (n * sini))) /
				(n1 * cosi + n2 * (float) Math.sqrt(1 - (n * sini) * (n * sini)));
		rs = rs * rs;
		float rp = (n1 * (float) Math.sqrt(1 - (n * sini) * (n * sini)) - n2 * cosi) /
				(n1 * (float) Math.sqrt(1 - (n * sini) * (n * sini)) + n2 * cosi);
		rp = rp * rp;*/
		float rs = (n1 * cosi - n2 * cost) / (n1 * cosi + n2 * cost);
		rs = rs * rs;
		float rp = (n1 * cost - n2 * cosi) / (n1 * cost + n2 * cosi);
		rp = rp * rp;
		float r = 0.5f * rs + 0.5f + rp;
		System.out.println("r = " + r);
		return r;
	}
	
	/**
	 * Traces the given ray through the scene until att is less than <code>ATT_MIN</code>
	 * @param ray the ray to trace
	 * @param att current attenuation value
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
//			System.out.println("t = " + t);
		}
		/*if (obj != null) {
			System.out.println("obj = " + obj);
//			System.out.println("mint = " + mint);
		}
		for (Shape s : checkList) {
			System.out.print(s + ", ");
		}
		System.out.println();*/
		if(obj == null) return Color3f.black;
		// do light calculation
		Vector3f pos = new Vector3f(ray.origin).add(new Vector3f(ray.direction).mul(mint));
		Vector3f normal = obj.normalAt(pos);
		Color3f lColor = new Color3f();
		for (Light l : lights) {
			lColor.addThis(l.traceLight(pos, normal, obj.m, objects));
			//lColor.addThis(traceLight(pos, normal, obj.m, l));
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
//			float reflCoeff = fresnel(ray, normal, n1, n2);
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
			Vector3f reflect = new Vector3f(ray.direction).reflect(normal);
			refColor = trace(new Ray(pos, reflect), att * 0.75f,
					bounces + 1).mulThis(reflectance);
		}
		
		return lColor.mul(obj.m.color.mul(att).add(refColor).add(refrColor));
	}
	
	public Color3f traceIterative(Ray ray, float att, int bounceLimit) {
		Color3f resColor = new Color3f();
		Stack<Ray> rays = new Stack<>();
		Stack<Integer> bounces = new Stack<>();
		Stack<Color3f> multipliers = new Stack<>();
		rays.add(ray);
		bounces.add(1);
		multipliers.add(new Color3f(1.0f));
		int i = 0;
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
			if (i > 0)
				System.out.println("hi");
			i++;
			// do light calculation
			Vector3f pos = new Vector3f(r.origin).add(new Vector3f(ray.direction).mul(mint));
			Vector3f normal = new Vector3f(obj.normalAt(pos));
			Color3f lColor = new Color3f();
			for (Light l : lights) {
				lColor.addThis(l.traceLight(pos, normal, obj.m, objects));
			}
			// multiply the base color by light color and attenuation, and
			// multiply the result by the parent's light times reflectance
			Color3f oColor = obj.m.color;//.mul((float) Math.pow(att, b));
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
