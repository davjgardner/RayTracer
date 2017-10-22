package geom;

import org.joml.Vector3f;

public abstract class Light {
	
	public abstract Color3f getColor(Vector3f pos);
}
