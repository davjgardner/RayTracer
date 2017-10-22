package geom;

import org.joml.Vector3f;

public class DirectionalLight {
	public Vector3f dir;
	public Color3f color;
	
	public DirectionalLight(Vector3f dir, Color3f color) {
		this.dir = dir;
		this.color = color;
	}
}
