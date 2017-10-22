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
	
	int width = 512, height = 512;
	BufferedImage img;
	JFrame frame;
	
	Sphere sphere = new Sphere(new Vector3f(0, 0, -2f), 1.0f);
	PointLight light = new PointLight(new Vector3f(0, 5f, -1.0f), Color3f.white);
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
		
		sphere.m = new Material(Color3f.red, 0.3f);
		
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
				Color3f c = trace(cast(x, y), null, null, 1.0f);
				img.setRGB(x, y, c.getRGB());
			}
		}
	}
	
	/**
	 * Traces the given ray through the scene until att is less than ATT_MIN
	 * @param ray the ray to trace
	 * @param s list of shapes in the scene
	 * @param att current attenuation value
	 * @return the Color3f this ray supplies
	 */
	public Color3f trace(Ray ray, List<Shape> s, List<Light> l, float att) {
		/*
		 * for Object o in objects
		 *  collide ray with o
		 *  if no collision then continue
		 *  else
		 *   Color3f += cast rays to light sources
		 *   Color3f += trace reflection ray
		 *   return Color3f * att
		 */
		float t = sphere.collides(ray);
		if (t > 0) {
			Vector3f pos = new Vector3f(ray.origin).add(new Vector3f(ray.direction).mul(t));
			Color3f lColor = traceLight(pos, sphere.normalAt(pos), sphere.m, l);
			return lColor.mul(sphere.m.color);
		}
		else return Color3f.black;
	}
	
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m, List<Light> l) {
		Vector3f toLight = new Vector3f(light.pos).sub(pos).normalize();
//		System.out.println("toLight = " + toLight);
		float diffuseFactor = Math.max(normal.dot(toLight), 0.0f);
//		float diffuseFactor = Math.max(normal.dot(dlight.dir), 0.0f);
		System.out.println("diffuseFactor = " + diffuseFactor);
		Color3f diffuseColor = light.color.mul(diffuseFactor);
		return diffuseColor;
	}
	
	public static void main(String[] args) {
		new TracerMain();
	}
}
