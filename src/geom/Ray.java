package geom;

import org.joml.Vector3f;

/**
 * Represents a ray in 3D space with an origin and a direction.
 */
public class Ray {
	
	public Vector3f origin, direction;
	
	/**
	 *
	 * @param origin endpoint of the ray
	 * @param direction direction vector of the ray (normalized).
	 */
	public Ray(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction.normalize();
	}
}
