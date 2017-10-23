package light;

/**
 * Encapsulates the material properties of an object
 */
public class Material {
	public Color3f color;
	public float reflectance;
	public float luminosity;
	
	/**
	 *
	 * @param color color of the object
	 * @param reflectance reflectance factor of the object
	 * @param luminosity [UNUSED] luminosity of the object
	 */
	public Material(Color3f color, float reflectance, float luminosity) {
		this.color = color;
		this.reflectance = reflectance;
		this.luminosity = luminosity;
	}
	
	/**
	 *
	 * @param color color of the object
	 * @param reflectance reflectance factor of the object
	 */
	public Material(Color3f color, float reflectance) {
		this.color = color;
		this.reflectance = reflectance;
	}
}
