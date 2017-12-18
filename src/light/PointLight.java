package light;

import geom.Ray;
import geom.Shape;
import geom.Vector3f;

import java.util.List;

/**
 * Represents an omnidirectional point light
 */
public class PointLight extends Light {
	
	/**
	 *
	 * @param pos position of the light
	 * @param color color of the light
	 */
	public PointLight(Vector3f pos, Color3f color) {
		this.pos = pos;
		this.color = color;
	}
	
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		Vector3f toLight = this.pos.sub(pos).normalize();
		float diffuseFactor = Math.max(normal.dot(toLight), 0.0f);
		Color3f diffuseColor = this.color.mul(diffuseFactor);
		return diffuseColor;
	}
	
	@Override
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m,
	                           List<Shape> objects) {
		Vector3f toLightv = this.pos.sub(pos);
		float d = toLightv.length();
		Ray toLight = new Ray(pos, toLightv);
		
		for (Shape obj : objects) {
			float t = obj.collides(toLight);
			if (t > 0.0f && t < d && obj.m.refractionIndex == 0)
				return new Color3f(0);
		}
		return this.calcColor(pos, normal, m);
	}
}
