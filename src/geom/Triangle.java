package geom;

import light.Material;

/**
 * [UNFINISHED]
 * Represents a planar triangle in 3D space.
 */
public class Triangle extends Shape {
	
	Vector3f p1, p2, p3;
	Vector3f v1, v2, normal;
	
	
	public Triangle(Vector3f p1, Vector3f p2, Vector3f p3, Material m) {
		this.m = m;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		v1 = new Vector3f(p2).sub(p1);
		v2 = new Vector3f(p3).sub(p1);
		//normal = new Vector3f(v1).cross(v2); // Didn't implement cross product, not needed elsewhere
	}
	
	@Override
	public float collides(Ray ray) {
		float d = ray.direction.dot(normal);
		if (Math.abs(d) < EPSILON) return -1;
		float t = - normal.dot(ray.origin.sub(p1))
				/ normal.dot(ray.direction);
		return (t >= 0)? t : -1;
		// TODO: check if in bounds
	}
	
	@Override
	public Vector3f normalAt(Vector3f pos) {
		return normal;
	}
	
	@Override
	public int planePartition(Plane p) {
		return 0;
	}
}
