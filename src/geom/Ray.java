package geom;

/**
 * Represents a ray in 3D space with an origin and a direction.
 */
public class Ray {
	// added to each ray origin to prevent collision with surface of origin
	private static final float ERR_DELTA = 0.01f;
	public Vector3f origin, direction;
	
	/**
	 *
	 * @param origin endpoint of the ray
	 * @param direction direction vector of the ray (normalized).
	 */
	public Ray(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction.normalize();
		this.origin.addThis(this.direction.mul(ERR_DELTA));
	}
	
	/**
	 * Calculates the point <code>t</code> units along the ray
	 * @param t
	 * @return
	 */
	public Vector3f getPoint(float t) {
		return origin.add(direction.mul(t));
	}
	
	@Override
	public String toString() {
		return "o=" + origin + ", d=" + direction;
	}
}
