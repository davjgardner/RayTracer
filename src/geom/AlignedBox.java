package geom;

import org.joml.Vector3f;

/**
 * Created by david on 12/15/2017.
 */
public class AlignedBox extends Shape {
	public Vector3f p1, p2;
	
	public AlignedBox(Vector3f p1, Vector3f p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Vector3f getCenter() {
		return new Vector3f(p1).add(new Vector3f(p2).sub(p1).mul(0.5f));
	}
	
	public Vector3f getSize() {
		return new Vector3f(p2).sub(p1);
	}
	
	@Override
	public float collides(Ray ray) {
		float tmin = (p1.x - ray.origin.x) / ray.direction.x;
		float tmax = (p2.x - ray.origin.x) / ray.direction.x;
		
		if (tmin > tmax) {
			float temp = tmin;
			tmin = tmax;
			tmax = temp;
		}
		
		float tymin = (p1.y - ray.origin.y) / ray.direction.y;
		float tymax = (p2.y - ray.origin.y) / ray.direction.y;
		
		if (tymin > tymax) {
			float temp = tymin;
			tymin = tymax;
			tymax = temp;
		}
		
		if (tmin > tymax || tymin > tmax) return -1;
		
		if (tymin > tmin)
			tmin = tymin;
		
		if (tymax < tmax)
			tmax = tymax;
		
		float tzmin = (p1.z - ray.origin.z) / ray.direction.z;
		float tzmax = (p2.z - ray.origin.z) / ray.direction.z;
		
		if (tzmin > tzmax) {
			float temp = tzmin;
			tzmin = tzmax;
			tzmax = temp;
		}
		
		if (tmin > tzmax || tzmin > tmax)
			return -1;
		
		if (tzmin > tmin)
			tmin = tzmin;
		
		if (tzmax < tmax)
			tmax = tzmax;
		
		return Math.min(tmin, tmax);
	}
	
	@Override
	public Vector3f normalAt(Vector3f pos) {
		return null;
	}
	
	@Override
	public int planePartition(Plane p) {
		return 0;
	}
}
