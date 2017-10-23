package geom;

import org.joml.Vector3f;

/**
 * Created by david on 10/22/2017.
 */
public class Plane extends Shape {
	public static final float EPSILON = 0.00001f;
	
	public Vector3f normal, pos;
	
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
