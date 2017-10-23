package tracer;

import geom.*;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class TracerMain {
	
	public static final float ATT_MIN = 0.0001f;
	public static final float FOV = (float) Math.toRadians(60);
	
	int width = 1000, height = 1000;
	BufferedImage img;
	JFrame frame;
	
	List<Shape> objects = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	
	
	DirectionalLight dlight = new DirectionalLight(new Vector3f(0.0f, 0.0f, 1.0f), Color3f.white);
	
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
		float aspectRatio = (float) width / (float) height;
		sy = height - sy - 1;
		sx -= 0.5f;
		sy -= 0.5f;
		float x = sx/width * 2f - 1f;
		float y = sy/height * 2f - 1f;
		float fly = 1f / (2f * (float) Math.tan(FOV / 2f));
		float flx = 1f / (2f * (float) Math.tan(FOV * aspectRatio / 2f));
		float thetax = (float) Math.atan2(x, flx);
		float thetay = (float) Math.atan2(y, fly);
		Vector3f dir = new Vector3f((float) Math.cos(thetax), (float) Math.cos(thetay), -1).normalize();
		dir = new Vector3f(x, y, -1).normalize();
//		System.out.println("(" + x + ", " + y + "): dir = " + dir);
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
	 * Traces the given ray through the scene until att is less than ATT_MIN
	 * @param ray the ray to trace
	 * @param att current attenuation value
	 * @return the Color3f this ray supplies
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
	
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m, Light light) {
		Ray toLight = new Ray(pos, new Vector3f(light.pos).sub(pos).normalize());
		for (Shape obj : objects) {
			float t = obj.collides(toLight);
			if (t > 0) return Color3f.black;
		}
		//TODO: make the light do more work so that DirectionalLight and AmbientLight work
		return light.calcColor(pos, normal, m);
	}
	
	public static void main(String[] args) {
		new TracerMain();
	}
}
