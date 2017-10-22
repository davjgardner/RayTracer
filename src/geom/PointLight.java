package geom;

import org.joml.Vector3f;

public class PointLight extends Light {
	public Vector3f pos;
	public Color3f color;
	
	public PointLight(Vector3f pos, Color3f color) {
		this.pos = pos;
		this.color = color;
	}
	
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		return color;
	}
}
