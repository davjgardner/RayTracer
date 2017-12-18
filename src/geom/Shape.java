package geom;

import light.Material;

/**
 * Superclass of all types of geometric object.
 */
public abstract class Shape {
	
	public static final float EPSILON = 0.00001f;
	
	public Material m;
	
	/**
	 * Calculates the minimum ray parameter at which a collision occurs with
	 * this object, if any
	 * @param ray
	 * @return minimum ray parameter, or -1 if no collision
	 */
	public abstract float collides(Ray ray);
	
	/**
	 * Calculates the surface normal at the given position
	 * @param pos
	 * @return
	 */
	public abstract Vector3f normalAt(Vector3f pos);
	
	
	public static final int COLLIDE = 0, PLUS = 1, MINUS = 2;
	/**
	 * Tests which side of the given plane this shape is on
	 * @param p partitioning plane
	 * @return Shape.COLLIDE for a collision, Shape.PLUS for +normal side of plane, Shape.MINUS for -normal side
	 */
	public abstract int planePartition(Plane p);
	
}

