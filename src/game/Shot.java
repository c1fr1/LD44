package game;

import engine.Platform.Ray3f;
import game.entities.Player;
import org.joml.Vector3f;

public class Shot extends Vector3f {
	public static boolean left = true;
	public Vector3f velocity = new Vector3f();
	public Shot(Player player) {
		x = player.x;
		y = player.y;
		z = player.z;
		if (left) {
			add(player.getRotatedVector(-1, 0, 0, 0.5f));
		} else {
			add(player.getRotatedVector(1, 0, 0, 0.5f));
		}
		left = !left;
		velocity = player.getRotatedVector(0, 0, -1, 100);
		velocity.add(player.velocity);
	}
	
	public Ray3f getRay(float timeStep) {
		Ray3f ray = new Ray3f(this, velocity.mul(-timeStep, new Vector3f()));
		return ray;
	}
}
