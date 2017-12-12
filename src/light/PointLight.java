package light;

import geom.Ray;
import geom.Shape;
import org.joml.Vector3f;

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
		Vector3f toLight = new Vector3f(this.pos).sub(pos).normalize();
		float diffuseFactor = Math.max(normal.dot(toLight), 0.0f);
		Color3f diffuseColor = this.color.mul(diffuseFactor);
		return diffuseColor;
	}
	
	@Override
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m,
	                           List<Shape> objects) {
		Ray toLight = new Ray(pos, new Vector3f(this.pos).sub(pos).normalize());
		for (Shape obj : objects) {
			float t = obj.collides(toLight);
			if (t > 0) return Color3f.black;
		}
		return this.calcColor(pos, normal, m);
	}
}
