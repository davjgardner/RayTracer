package light;

import org.joml.Vector3f;

/**
 * Represents a directional light
 */
public class DirectionalLight {
	public Vector3f dir;
	public Color3f color;
	
	/**
	 *
	 * @param dir direction vector
	 * @param color
	 */
	public DirectionalLight(Vector3f dir, Color3f color) {
		this.dir = dir;
		this.color = color;
	}
}
