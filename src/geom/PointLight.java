package geom;

import org.joml.Vector3f;

public class PointLight extends Light {
	
	public PointLight(Vector3f pos, Color3f color) {
		this.pos = pos;
		this.color = color;
	}
	
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		Vector3f toLight = new Vector3f(this.pos).sub(pos).normalize();
		float diffuseFactor = Math.max(normal.dot(toLight), 0.0f);
		Color3f diffuseColor = this.color.mul(diffuseFactor);
		return diffuseColor;
	}
}
