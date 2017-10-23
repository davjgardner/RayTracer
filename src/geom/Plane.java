package geom;

import light.Material;
import org.joml.Vector3f;

/**
 * Represents an infinite plane in 3D space.
 */
public class Plane extends Shape {
	public static final float EPSILON = 0.00001f;
	
	public Vector3f normal, pos;
	
	/**
	 *
	 * @param normal normal vector of this plane
	 * @param pos a point on this plane
	 * @param m the material of this plane
	 */
	public Plane(Vector3f normal, Vector3f pos, Material m) {
		this.normal = normal;
		this.pos = pos;
		this.m = m;
	}
	
	@Override
	public float collides(Ray ray) {
		// For now, assume any parallel rays do not intersect
		float d = ray.direction.dot(normal);
		if (Math.abs(d) < EPSILON) return -1;
		float t = - normal.dot(new Vector3f(ray.origin).sub(pos))
				/ normal.dot(ray.direction);
		return (t >= 0)? t : -1;
	}
	
	@Override
	public Vector3f normalAt(Vector3f pos) {
		return normal;
	}
}
