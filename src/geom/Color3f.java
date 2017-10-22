package geom;

import java.awt.Color;

public class Color3f {
	public float r, g, b;
	
	public Color3f() {}
	
	public Color3f(float v) {
		r = v;
		g = v;
		b = v;
	}
	
	public Color3f(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color3f mul(Color3f c) {
		return new Color3f(r * c.r, g * c.g, b * c.b);
	}
	
	public Color3f mul(float f) {
		return new Color3f(r * f, g * f, b * f);
	}
	
	public Color3f add(Color3f c) {
		return new Color3f(r + c.r, g + c.g, b + c.b);
	}
	
	public int getRGB() {
		/*int ir = (int) (r * 255.0);
		int ig = (int) (g * 255.0);
		int ib = (int) (b * 255.0);*/
		Color3f c = this.clamp();
		return new Color(c.r, c.g, c.b).getRGB();
//		return ib + ig << 8 + ir << 16;
	}
	
	public Color3f clamp() {
		return new Color3f(Math.min(Math.abs(r), 1.0f),
				Math.min(Math.abs(g), 1.0f),
				Math.min(Math.abs(b), 1.0f));
	}
	
	public static Color3f black = new Color3f();
	public static Color3f white = new Color3f(1.0f);
	public static Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
	public static Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
	public static Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
	
}
