package light;

import org.joml.Vector3f;

/**
 * Represents a constant light component
 */
public class AmbientLight extends Light {
	
	public Color3f color;
	
	/**
	 *
	 * @param color color of the light
	 */
	public AmbientLight(Color3f color) {
		this.color = color;
	}
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		return color;
	}
}
