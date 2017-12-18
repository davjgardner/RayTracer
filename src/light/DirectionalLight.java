package light;

import geom.Ray;
import geom.Shape;
import geom.Vector3f;

import java.util.List;

/**
 * Represents a directional light
 */
public class DirectionalLight extends Light {
	public Vector3f dir;
	
	/**
	 *
	 * @param dir direction vector
	 * @param color
	 */
	public DirectionalLight(Vector3f dir, Color3f color) {
		this.dir = dir;
		this.color = color;
	}
	
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		float diffuseFactor = Math.max(normal.dot(dir), 0.0f);
		Color3f diffuseColor = this.color.mul(diffuseFactor);
		return diffuseColor;
		}
	
	@Override
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m, List<Shape> objects) {
		Ray toLight = new Ray(pos, this.dir.negate().normalize());
		for (Shape obj : objects) {
			float t = obj.collides(toLight);
			if (t > 0) return Color3f.black;
		}
		return this.calcColor(pos, normal, m);
	}
}
