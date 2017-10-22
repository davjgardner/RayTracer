package geom;

import org.joml.Vector3f;

public abstract class Light {
	
	public abstract Color3f calcColor(Vector3f pos, Vector3f normal);
}
