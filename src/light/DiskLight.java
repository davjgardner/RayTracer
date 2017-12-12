package light;

import geom.Ray;
import geom.Shape;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

/**
 * Represents a thin disk-shaped light with real area
 */
public class DiskLight extends Light {
	private Vector3f normal;
	// Orthogonal basis vectors for the disk
	private Vector3f u, v;
	private float radius;
	// TODO: add spread (cone light)
	
	private Random rand;
	private int samples = 50;
	
	/**
	 *
	 * @param pos light position
	 * @param normal direction the light is pointed
	 * @param radius radius of the disk
	 * @param color color of the light
	 */
	public DiskLight(Vector3f pos, Vector3f normal, float radius,
	                 Color3f color) {
		this.pos = pos;
		this.normal = normal;
		this.radius = radius;
		this.color = color;
		
		// Calculate orthonormal basis for disk
		// Method from https://stackoverflow.com/questions/19337314/generate-random-point-on-a-2d-disk-in-3d-space-given-normal-vector
		if (Math.abs(normal.x) < Math.abs(normal.y) &&
				Math.abs(normal.x) < Math.abs(normal.z)) {
			u = new Vector3f(0, -normal.z, normal.y);
			v = new Vector3f(normal.y * normal.y + normal.z * normal.z,
					-normal.x * normal.y, -normal.x * normal.z);
		} else if (Math.abs(normal.y) < Math.abs(normal.x) &&
				Math.abs(normal.y) < Math.abs(normal.z)) {
			u = new Vector3f(-normal.z, 0, normal.x);
			v = new Vector3f(-normal.y * normal.x,
					normal.x * normal.x + normal.z * normal.z,
					-normal.y * normal.z);
		} else {
			u = new Vector3f(-normal.y, normal.x, 0);
			v = new Vector3f(-normal.z * normal.x, -normal.z * normal.y,
					normal.x * normal.x + normal.y * normal.y);
		}
		u.normalize();
		v.normalize();
		rand = new Random();
	}
	
	/**
	 *
	 * @return a uniformly-sampled random point on the disk
	 */
	private Vector3f samplePoint() {
		float r = rand.nextFloat();
		float theta = rand.nextFloat() * 2 * (float) Math.PI;
		float sr = radius * (float) Math.sqrt(r);
		float x = sr * (float) Math.cos(theta);
		float y = sr * (float) Math.sin(theta);
		Vector3f p = new Vector3f(u).mul(x).add(new Vector3f(v).mul(y)).add(pos);
		//System.out.println(p.sub(pos).dot(normal));
		return p;
		//return new Vector3f(pos);
	}
	
	@Override
	public Color3f calcColor(Vector3f pos, Vector3f normal, Material m) {
		return null;
	}
	
	/**
	 * Disk calcColor requires specific point on light
	 * @param pos object position
	 * @param normal object normal
	 * @param m object material
	 * @param lpos position on the disk
	 * @return color contribution at <code>pos</code>
	 */
	private Color3f calcColor(Vector3f pos, Vector3f normal, Material m,
	                         Vector3f lpos) {
		Vector3f toLight = new Vector3f(lpos).sub(pos).normalize();
		float diffuseFactor = Math.max(normal.dot(toLight), 0.0f);
		Color3f diffuseColor = this.color.mul(diffuseFactor);
		return diffuseColor;
	}
	
	@Override
	public Color3f traceLight(Vector3f pos, Vector3f normal, Material m, List
				<Shape> objects) {
		Color3f color = new Color3f(Color3f.black);
		for (int i = 0; i < samples; i++) {
			Vector3f lpos = samplePoint();
			Color3f c = this.calcColor(pos, normal, m, lpos);
			Ray toLight = new Ray(pos, lpos.sub(pos).normalize());
			for (Shape obj : objects) {
				float t = obj.collides(toLight);
				if (t > 0) {
					c = Color3f.black;
					break;
				}
			}
			color.addThis(c);
		}
		return color.mulThis(1f / samples);
	}
}
