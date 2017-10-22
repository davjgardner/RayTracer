package geom;

import org.joml.Vector3f;

public class AmbientLight extends Light {
	
	public Color3f color;
	
	public AmbientLight(Color3f color) {
		this.color = color;
	}
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		return color;
	}
}
