package geom;

/**
 * Basic vector math implementation
 */
public class Vector3f {
	
	public float x, y, z;
	
	/**
	 * Create a new vector with all components initialized to zero
	 */
	public Vector3f() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	
	/**
	 * Create a new vector with components x, y, and z
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Create a clone of v
	 * @param v
	 */
	public Vector3f(Vector3f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	/**
	 * Create a new vector with all components equal to f
	 * @param f
	 */
	public Vector3f(float f) {
		this.x = f;
		this.y = f;
		this.z = f;
	}
	
	// Vector operations:
	
	/**
	 * Add v to this vector
	 * @param v
	 * @return
	 */
	public Vector3f add(Vector3f v) {
		return new Vector3f(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	/**
	 * Add v to this vector and store the result in this
	 * @param v
	 * @return this
	 */
	public Vector3f addThis(Vector3f v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}
	
	/**
	 * Subtract v from this vector
	 * @param v
	 * @return
	 */
	public Vector3f sub(Vector3f v) {
		return new Vector3f(this.x - v.x, this.y - v.y, this.z - v.z);
	}
	
	/**
	 * Subtract v from this vector and store the result in this
	 * @param v
	 * @return this
	 */
	public Vector3f subThis(Vector3f v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}
	
	/**
	 * Multiply this vector with f and return the result
	 * @param f
	 * @return
	 */
	public Vector3f mul(float f) {
		return new Vector3f(this.x * f, this.y * f, this.z * f);
	}
	
	/**
	 * Multiply this vector with f and store the result in this
	 * @param f
	 * @return this
	 */
	public Vector3f mulThis(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
		return this;
	}
	
	/**
	 *
	 * @return the normalized form of this vector
	 */
	public Vector3f normalize() {
		float d = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);
		return this.mul(d);
	}
	
	/**
	 *
	 * @return the negative of this vector
	 */
	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}
	
	/**
	 *
	 * @return length of this vector
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 *
	 * @return length squared of this vector
	 */
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	/**
	 * Dot this vector with v
	 * @param v
	 * @return dot product of this vector with v
	 */
	public float dot(Vector3f v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}
	
	/**
	 * Reflect this vector across the given normal
	 * @param normal normalized normal vector
	 * @return reflected vector
	 */
	public Vector3f reflect(Vector3f normal) {
		return this.sub(normal.mul(2 * this.dot(normal)));
	}
	
}
