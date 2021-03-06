package game;

import engine.OpenGL.EnigWindow;

import static org.lwjgl.glfw.GLFW.*;

public class UserControls {
	public static int[] forward = new int[] {GLFW_KEY_W};
	public static int[] backward = new int[] {GLFW_KEY_S};
	public static int[] left = new int[] {GLFW_KEY_A};
	public static int[] right = new int[] {GLFW_KEY_D};
	public static int[] up = new int[] {GLFW_KEY_SPACE};
	public static int[] down = new int[] {GLFW_KEY_LEFT_SHIFT};
	public static int[] seeAsteroids = new int[] {GLFW_KEY_E};
	public static int[] quit = new int[] {GLFW_KEY_ESCAPE};
	public static int[] matchVelocity = new int[] {GLFW_KEY_Q};
	
	public static float sensitivity = 1f/500f;
	
	public static boolean forward(EnigWindow window) {
		for (int i:forward) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean backward(EnigWindow window) {
		for (int i:backward) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean left(EnigWindow window) {
		for (int i:left) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean right(EnigWindow window) {
		for (int i:right) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean up(EnigWindow window) {
		for (int i:up) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean down(EnigWindow window) {
		for (int i:down) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean matchVelocity(EnigWindow window) {
		for (int i: matchVelocity) {
			if (window.keys[i] == 1) {
				return true;
			}
		}
		return false;
	}
	public static boolean seeAsteroids(EnigWindow window) {
		for (int i: seeAsteroids) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean quit(EnigWindow window) {
		for (int i:quit) {
			if (window.keys[i] == 1) {
				window.keys[i] = 2;
				return true;
			}
		}
		return false;
	}
	public static boolean checkI(EnigWindow window, int i) {
		if (window.keys[GLFW_KEY_0 + i] == 1) {
			return true;
		}
		return false;
	}
}
	
	
	
/*
	if commonCamera is true, the camera rotation for most 3d games will be used, if it is false, this alternate method is used:

	every frame the new rotation matrix is set to the rotation matrix from relative mouse position changes (from the last frame),
	will be multiplied by the old rotation matrix.
	R(xnew-xold)*R(ynew-yold)*R(znew-zold)*oldRotationMatrix.
	
	oh btw
	this variable has been moved from this file, to the Camera file, so you can have different cameras with different types of cameras
																																		 */