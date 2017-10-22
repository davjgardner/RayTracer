package geom;

import org.joml.Vector3f;

public class Ray {
	public Vector3f origin, direction;

	public Ray(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction;
	}
}
