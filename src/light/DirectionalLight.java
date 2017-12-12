package light;

import org.joml.Vector3f;

/**
 * Represents a directional light
 */
public class DirectionalLight extends Light {
	public Vector3f dir;
	
	/**
	 *
	 * @param dir direction vector
	 * @param color
	 */
	public DirectionalLight(Vector3f dir, Color3f color) {
		this.dir = dir;
		this.color = color;
	}
	
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		float diffuseFactor = Math.max(normal.dot(dir), 0.0f);
		Color3f diffuseColor = this.color.mul(diffuseFactor);
		return diffuseColor;
		}
}
