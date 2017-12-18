package geom;

import light.Material;

/**
 * Represents an infinite plane in 3D space.
 */
public class Plane extends Shape {
	
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
		float d = ray.direction.dot(normal);
		if (Math.abs(d) < EPSILON) return -1;
		float t = - normal.dot(ray.origin.sub(pos)) / d;
		return (t > 0)? t : -1;
	}
	
	@Override
	public Vector3f normalAt(Vector3f pos) {
		return normal;
	}
	
	@Override
	public int planePartition(Plane p) {
		// Check if the plane is parallel
		if (Math.abs(p.normal.dot(this.normal)) - 1.0f < EPSILON) {
			return Shape.COLLIDE;
		} else if (this.pos.sub(p.pos).dot(p.normal) > 0) {
			return Shape.PLUS;
		} else {
			return Shape.MINUS;
		}
	}
}
