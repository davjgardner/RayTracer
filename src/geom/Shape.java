package geom;

import light.Material;
import org.joml.Vector3f;

/**
 * Superclass of all types of geometric object.
 */
public class Shape {
	
	public Material m;
	
	/**
	 * Calculates the minimum ray parameter at which a collision occurs with this object, if any
	 * @param ray
	 * @return minimum ray parameter, or -1 if no collision
	 */
	public float collides(Ray ray) {
		return -1;
	}
	
	/**
	 * Calculates the surface normal at the given position
	 * @param pos
	 * @return
	 */
	public Vector3f normalAt(Vector3f pos) {return null;}
	
	
}
