package light;

import geom.Ray;
import geom.Shape;
import geom.Vector3f;

import java.util.List;

/**
 * Represents a constant light component
 */
public class AmbientLight extends Light {
	
	public Color3f color;
	
	/**
	 *
	 * @param color color of the light
	 */
	public AmbientLight(Color3f color) {
		this.color = color;
	}
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		return color;
	}
	
	@Override
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m,
	                          List<Shape> objects) {
		return this.calcColor(pos, normal, m);
	}
}
