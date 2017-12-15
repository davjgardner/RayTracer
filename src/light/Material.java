package light;

/**
 * Encapsulates the material properties of an object
 */
public class Material {
	public Color3f color;
	public float reflectance;
	public float refractionIndex;
	
	/**
	 *
	 * @param color color of the object
	 * @param reflectance reflectance factor of the object
	 */
	public Material(Color3f color, float reflectance) {
		this.color = color;
		this.reflectance = reflectance;
		this.refractionIndex = 0;
	}
	
	/**
	 *
	 * @param color color of the object
	 * @param reflectance reflectance factor of the object
	 * @param refractionIndex index of refraction
	 */
	public Material(Color3f color, float reflectance, float refractionIndex) {
		this.color = color;
		this.reflectance = reflectance;
		this.refractionIndex = refractionIndex;
	}
}
