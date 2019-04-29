package game.entities;

import engine.Entities.Camera;
import engine.OpenGL.EnigWindow;
import engine.Platform.Ray3f;
import game.UserControls;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static org.joml.Math.sin;

public class Player extends Camera {
	public Vector3f velocity = new Vector3f();
	
	public float energy = 1;
	
	public Player(EnigWindow window) {
		super((float) PI * 0.25f, 0.01f, 9e9f, window);
	}
	
	public void updateMovement(EnigWindow window, float timeStep) {
		if (UserControls.forward(window)) {
			velocity.add(getRotatedVector(0f, 0f, -1f, 10 * timeStep));
			energy -= 0.01f * timeStep;
		}
		if (UserControls.backward(window)) {
			velocity.add(getRotatedVector(0f, 0f, 1f, 10 * timeStep));
			energy -= 0.01f * timeStep;
		}
		if (UserControls.left(window)) {
			velocity.add(getRotatedVector(-1f, 0f, 0, 10 * timeStep));
			energy -= 0.01f * timeStep;
		}
		if (UserControls.right(window)) {
			velocity.add(getRotatedVector(1f, 0f, 0f, 10 * timeStep));
			energy -= 0.01f * timeStep;
		}
		if (UserControls.up(window)) {
			velocity.add(getRotatedVector(0f, 1f, 0, 10 * timeStep));
			energy -= 0.01f * timeStep;
		}
		if (UserControls.down(window)) {
			velocity.add(getRotatedVector(0f, -1f, 0f, 10 * timeStep));
			energy -= 0.01f * timeStep;
		}
		add(velocity.mul(timeStep, new Vector3f()));
	}
	
	public Ray3f getRay() {
		return new Ray3f(this, getRotatedVector(0, 0, -1f, 1f));
	}
	
	public void applyMovement(Vector3f acceleration, float timeStep) {
		add(velocity.mul(timeStep, new Vector3f()));
		add(acceleration.mul(0.5f * timeStep * timeStep, new Vector3f()));
		velocity.add(acceleration.mul(timeStep, new Vector3f()));
	}
	
	public void updateRotation(EnigWindow window) {
		yaw(-(float) window.cursorXOffset / 1000f);
		pitch(-(float) window.cursorYOffset / 1000f);
		updateRotations();
	}
}
