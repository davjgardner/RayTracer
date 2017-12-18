package geom;

import light.Material;
import org.joml.Vector3f;

/**
 * Represents an axis-aligned box
 */
public class AlignedBox extends Shape {
	
	public Vector3f p1, p2;
	
	public AlignedBox(Vector3f p1, Vector3f p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public AlignedBox(Vector3f p1, Vector3f p2, Material m) {
		this.p1 = p1;
		this.p2 = p2;
		this.m = m;
	}
	
	public AlignedBox(Vector3f center, float sx, float sy, float sz, Material m) {
		Vector3f halfSize = new Vector3f(sx/2, sy/2, sz/2);
		this.p1 = new Vector3f(center).sub(halfSize);
		this.p2 =  new Vector3f(center).add(halfSize);
		this.m = m;
	}
	
	public Vector3f getCenter() {
		return new Vector3f(p1).add(new Vector3f(p2).sub(p1).mul(0.5f));
	}
	
	public Vector3f getSize() {
		return new Vector3f(p2).sub(p1);
	}
	
	public float collides(Ray ray) {
		// from: https://tavianator.com/fast-branchless-raybounding-box-intersections/
		float tx1 = (p1.x - ray.origin.x) / ray.direction.x;
		float tx2 = (p2.x - ray.origin.x) / ray.direction.x;
		
		float tmin = Math.min(tx1, tx2);
		float tmax = Math.max(tx1, tx2);
		
		float ty1 = (p1.y - ray.origin.y) / ray.direction.y;
		float ty2 = (p2.y - ray.origin.y) / ray.direction.y;
		
		tmin = Math.max(tmin, Math.min(ty1, ty2));
		tmax = Math.min(tmax, Math.max(ty1, ty2));
		
		float tz1 = (p1.z - ray.origin.z) / ray.direction.z;
		float tz2 = (p2.z - ray.origin.z) / ray.direction.z;
		
		tmin = Math.max(tmin, Math.min(tz1, tz2));
		tmax = Math.min(tmax, Math.max(tz1, tz2));
		
		if (tmax < tmin) return -1;
		
		if (tmin < 0.0f) return tmax;
		else return tmin;
	}
	
	@Override
	public Vector3f normalAt(Vector3f pos) {
		Vector3f v = new Vector3f(pos).sub(getCenter());
		if (Math.abs(v.x) > Math.abs(v.y) && Math.abs(v.x) > Math.abs(v.z)) {
			return new Vector3f(v.x / Math.abs(v.x), 0.0f, 0.0f);
		} else if (Math.abs(v.y) > Math.abs(v.x) && Math.abs(v.y) > Math.abs(v.z)) {
			return new Vector3f(0.0f, v.y / Math.abs(v.y), 0.0f);
		} else {
			return new Vector3f(0.0f, 0.0f,v.z / Math.abs(v.z));
		}
	}
	
	/**
	 * [UNIMPLEMENTED]
	 * @param p partitioning plane
	 * @return
	 */
	@Override
	public int planePartition(Plane p) {
		return 0;
	}
}
