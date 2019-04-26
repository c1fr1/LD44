package game.entities;

import engine.Entities.Camera;
import engine.OpenGL.EnigWindow;
import game.UserControls;
import org.joml.Matrix4f;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static org.joml.Math.sin;

public class Player extends Camera {
	
	public Player(EnigWindow window) {
		super((float) PI * 0.25f, 0.01f, 1000f, window);
	}
	
	public void updateRotation(EnigWindow window) {
		yaw(-(float) window.cursorXOffset / 1000f);
		pitch(-(float) window.cursorYOffset / 1000f);
		if (getPitch() < (float) -PI / 2) {
			setPitch((float) -PI / 2);
		}
		if (getPitch() > (float) PI / 2) {
			setPitch((float) PI / 2);
		}
	}
}
