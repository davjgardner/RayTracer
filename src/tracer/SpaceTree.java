package tracer;

import geom.AlignedBox;
import geom.Plane;
import geom.Ray;
import geom.Shape;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Spacial partitioning tree
 */
public class SpaceTree {
	private List<Shape> shapes;
	
	SpaceTree left, right;
	Vector3f center;
	Vector3f size;
	AlignedBox bounds;
	
	static final int X = 0, Y = 1, Z = 2;
	
	private static final int MAX_LEVEL = 4;
	
	SpaceTree(List<Shape> shapes, Vector3f center, Vector3f size) {
		this.shapes = new LinkedList<>();
		this.shapes.addAll(shapes);
		this.center = center;
		this.size = size;
		Vector3f halfSize = new Vector3f(size).mul(0.5f);
		this.bounds = new AlignedBox(new Vector3f(center).sub(halfSize), new Vector3f(center).add(halfSize));
	}
	
	void createTree(int axis, int level) {
		if (shapes.size() <= 1 || level >= MAX_LEVEL) return;
//		System.out.println("center = " + center);
//		System.out.println("size = " + size);
		Plane part = null;
		AlignedBox leftBox = null, rightBox = null;
		switch(axis) {
			case X:
				rightBox = new AlignedBox(bounds.p1, new Vector3f(bounds.p2.x - size.x / 2, bounds.p2.y, bounds.p2.z));
				leftBox = new AlignedBox(new Vector3f(bounds.p1.x + size.x / 2, bounds.p1.y, bounds.p1.z), bounds.p2);
				part = new Plane(new Vector3f(-1.0f, 0.0f, 0.0f), bounds.getCenter(), null);
				break;
			case Y:
				rightBox = new AlignedBox(bounds.p1, new Vector3f(bounds.p2.x, bounds.p2.y - size.y / 2, bounds.p2.z));
				leftBox = new AlignedBox(new Vector3f(bounds.p1.x, bounds.p1.y + size.y / 2, bounds.p1.z), bounds.p2);
				part = new Plane(new Vector3f(0.0f, -1.0f, 0.0f), bounds.getCenter(), null);
				break;
			case Z:
				rightBox = new AlignedBox(bounds.p1, new Vector3f(bounds.p2.x, bounds.p2.y, bounds.p2.z - size.z / 2));
				leftBox = new AlignedBox(new Vector3f(bounds.p1.x, bounds.p1.y, bounds.p1.z + size.z / 2), bounds.p2);
				part = new Plane(new Vector3f(0.0f, 0.0f, -1.0f), bounds.getCenter(), null);
				break;
		}
		List<Shape> leftList = new LinkedList<>(), rightList = new LinkedList<>();
		for (Shape s : shapes) {
			// sort
			switch (s.planePartition(part)) {
				case Shape.COLLIDE:
					leftList.add(s);
					rightList.add(s);
					break;
				case Shape.PLUS:
					rightList.add(s);
					break;
				case Shape.MINUS:
					leftList.add(s);
					break;
				default:
					System.out.println("Problem in collision detection");
			}
		}
		
		if ((leftList.size() == shapes.size() && rightList.size() == shapes.size())
				|| (leftList.isEmpty() || rightList.isEmpty())) {
			// partition did nothing, don't create children
		} else {
			shapes.removeAll(leftList);
			shapes.removeAll(rightList);
			
			left = new SpaceTree(leftList, leftBox.getCenter(), leftBox.getSize());
			left.createTree((axis + 1) % 3, level + 1);
			right = new SpaceTree(rightList, rightBox.getCenter(), rightBox.getSize());
			right.createTree((axis + 1) % 3, level + 1);
		}
	}
	
	List<Shape> getList() {
		return shapes;
	}
	
	List<Shape> getCheckList(Ray ray) {
		List<Shape> checkList = new LinkedList<>();
		if (bounds.collides(ray) >= 0) {
			checkList.addAll(shapes);
			if (left != null) checkList.addAll(left.getCheckList(ray));
			if (right != null) checkList.addAll(right.getCheckList(ray));
		}
		return checkList;
	}
	
	void print(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append(' ');
		}
		String i = sb.toString();
		System.out.println(i + "Center: " + bounds.getCenter());
		System.out.println(i + "Size: " + bounds.getSize());
		System.out.print(i + "Shapes: ");
		for (Shape s : shapes) {
			System.out.print(s + ", ");
		}
		System.out.println();
		if (left != null) {
			left.print(indent + 1);
		}
		if (right != null) {
			right.print(indent + 1);
		}
	}
}
