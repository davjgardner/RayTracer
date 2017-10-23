package geom;

import org.joml.Vector3f;

/**
 * Represents a sphere in 3D space.
 */
public class Sphere extends Shape {
	
	public Vector3f center;
	public float radius;
	
	/**
	 * Creates a sphere at (0, 0, 0) with <code>radius</code> 1.
	 */
	public Sphere() {
		center = new Vector3f();
		radius = 1f;
	}
	
	/**
	 *
	 * @param pos position of the Sphere
	 * @param radius radius of the Sphere
	 */
	public Sphere(Vector3f pos, float radius) {
		this.center = pos;
		this.radius = radius;
	}
	
	/**
	 *
	 * @param pos position of the Sphere
	 * @param radius radius of the Sphere
	 * @param material material properties of the Sphere
	 */
	public Sphere(Vector3f pos, float radius, Material material) {
		this(pos, radius);
		this.m = material;
	}
	
	@Override
	public float collides(Ray ray) {
		Vector3f v = new Vector3f(ray.origin).sub(this.center);
		float vdotd = v.dot(ray.direction);
		float disc = vdotd * vdotd - (v.lengthSquared() - this.radius * this.radius);
		if (disc < 0) return -1;
		float sdisc = (float) Math.sqrt(disc);
		float t1 = -vdotd + sdisc;
		float t2 = -vdotd - sdisc;
		float t = Math.min(t1, t2);
		if (t < 0) return -1;
		return t;
	}
	
	@Override
	public Vector3f normalAt(Vector3f pos) {
		return new Vector3f(pos).sub(center).normalize();
	}
}
