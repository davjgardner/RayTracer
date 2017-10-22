package geom;

import org.joml.Vector3f;

public class Sphere extends Shape {
	
	public Vector3f center;
	public float radius;
	
	public Sphere() {
		center = new Vector3f();
		radius = 1f;
	}
	
	public Sphere(Vector3f pos, float radius) {
		this.center = pos;
		this.radius = radius;
	}
	
	/**
	 * Calculates the collision with the given ray
	 * @param ray
	 * @return minimum ray parameter, or -1 if no collision
	 */
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
//		System.out.println("Hit!");
		return t;
	}
	
	public Vector3f normalAt(Vector3f pos) {
		return new Vector3f(pos).sub(center).normalize();
	}
}
