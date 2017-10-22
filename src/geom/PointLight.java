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
	public Color3f getColor(Vector3f pos) {
		return color;
	}
}
