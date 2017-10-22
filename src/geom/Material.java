package geom;

import java.awt.*;

public class Material {
	public Color3f color;
	public float reflectance;
	public float luminosity;
	
	public Material(Color3f color, float reflectance, float luminosity) {
		this.color = color;
		this.reflectance = reflectance;
		this.luminosity = luminosity;
	}
	
	public Material(Color3f color, float reflectance) {
		this.color = color;
		this.reflectance = reflectance;
	}
}
