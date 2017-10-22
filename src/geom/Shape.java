package geom;

import org.joml.Vector3f;

public class Shape {
	
	public Material m;
	
	/**
	 * Calculates the collision with the given ray
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
