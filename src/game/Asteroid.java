package game;

import engine.OpenGL.VAO;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import static java.lang.Math.E;
import static java.lang.Math.PI;
import static java.lang.Math.random;

public class Asteroid extends Vector3f {
	public static VAO sphere;
	
	public Vector3f velocity = new Vector3f();
	
	public float rotation;
	public float omega;
	
	public float randSeed;
	
	public float mass;
	public float scale;
	
	public float hitPlayerTimer = 1;
	
	public Vector3f impactLocation;
	public float impactTimer = -1;
	
	public Asteroid(float totalTimeElapsed) {
		x = (float) random() * 10f - 5f;
		y = (float) random() * 10f - 5f;
		z = (float) random() * 10f - 5f;
		normalize((float) Math.random() * 100f + 350f);
		scale = (float) random() * 2f + 0.5f;
		randSeed = (float) (E / random());
		velocity = new Vector3f((float) random(), (float) random(), (float) random()).normalize(20f * (float) random());
		
		velocity.x += 10 * (float) (random() * (totalTimeElapsed + 50f) / 50f) - 1f;
		Vector3f rAxis = new Vector3f((float) random(), (float) random(), (float) random()).normalize((float) random());
		//rotation = new AxisAngle4f((float) random(), rAxis);
		omega = (float) (PI * (random() - 0.5f));
	}
	
	public void applyImpulse(Vector3f dMoment) {
		velocity.add(dMoment.mul(1f/mass, new Vector3f()));
	}
	
	public void applyMovement(Vector3f acceleration, float timeStep) {
		add(velocity.mul(timeStep, new Vector3f()));
		add(acceleration.mul(0.5f * timeStep * timeStep, new Vector3f()));
		velocity.add(acceleration.mul(timeStep, new Vector3f()));
	}
	
	public static void loadRes() {
		sphere = new VAO("res/objects/4Sphere.obj");
	}
}
