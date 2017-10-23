package light;

import org.joml.Vector3f;

/**
 * Abstract superclass of all types of lights
 */
public abstract class Light {
	
	public Vector3f pos;
	public Color3f color;
	
	/**
	 * Calculates the color contribution of this light on an object
	 * @param pos position on the object
	 * @param normal normal of the surface at <code>pos</code>
	 * @param m material of the object
	 * @return the color of contribution of this light at the given position
	 */
	public abstract Color3f calcColor(Vector3f pos, Vector3f normal, Material m);
}
