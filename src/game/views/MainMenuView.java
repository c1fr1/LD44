package game.views;

import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import game.UserControls;
import org.joml.Matrix4f;

import static game.Main.squareCam;
import static game.Shaders.buttonShader;
import static game.Shaders.textureShader;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MainMenuView extends EnigView {
	public static MainMenuView main;
	
	public Texture titleTexture;
	public Texture startTexture;
	public Texture tutorialTexture;
	public Texture quitTexture;
	
	public VAO titleVAO;
	public VAO buttonVAO;
	
	public float time = 0;
	
	public MainMenuView(EnigWindow window) {
		super(window);
		main = this;
		titleTexture = new Texture("res/textures/menu/title.png");
		startTexture = new Texture("res/textures/menu/launch.png");
		tutorialTexture = new Texture("res/textures/menu/training.png");
		quitTexture = new Texture("res/textures/menu/abort.png");
		titleVAO = new VAO(-25f, 25f, 50f, 25f);
		buttonVAO = new VAO(-50, -5f, 30, 10);
	}
	
	public boolean loop() {
		FBO.prepareDefaultRender();
		textureShader.enable();
		titleTexture.bind();
		textureShader.setUniform(0, 0, squareCam);
		titleVAO.fullRender();
		
		buttonShader.enable();
		buttonShader.setUniform(0, 0, squareCam.translate(0f, 20f, 0f, new Matrix4f()));
		if (	window.cursorXFloat * window.getAspectRatio() * 50 > -50 &&
				window.cursorXFloat * window.getAspectRatio() * 50 < -20 &&
				window.cursorYFloat * 50 > 15 &&
				window.cursorYFloat * 50 < 25) {
			buttonShader.setUniform(2, 0, time);
			if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
				window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] = 0;
				window.toggleCursorInput();
				MainView.main.reset();
				MainView.main.runLoop();
				window.toggleCursorInput();
				if (MainView.main.gameIsOver) {
					GameOverView.main.runLoop();
				}
				window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] = 0;
				return false;
			}
		} else {
			buttonShader.setUniform(2, 0, 0f);
		}
		startTexture.bind();
		buttonVAO.prepareRender();
		buttonVAO.drawTriangles();
		
		
		buttonShader.setUniform(0, 0, squareCam.translate(0f, 10f, 0f, new Matrix4f()));
		if (	window.cursorXFloat * window.getAspectRatio() * 50 > -50 &&
				window.cursorXFloat * window.getAspectRatio() * 50 < -20 &&
				window.cursorYFloat * 50 > 5 &&
				window.cursorYFloat * 50 < 15) {
			buttonShader.setUniform(2, 0, time);
			if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
				window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] = 0;
				window.toggleCursorInput();
				MainView.main.reset();
				MainView.main.tutorialIndex = -1;
				MainView.main.runLoop();
				window.toggleCursorInput();
				if (MainView.main.gameIsOver) {
					GameOverView.main.runLoop();
				}
				window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] = 0;
				return false;
			}
		} else {
			buttonShader.setUniform(2, 0, 0f);
		}
		tutorialTexture.bind();
		buttonVAO.prepareRender();
		buttonVAO.drawTriangles();
		
		
		buttonShader.setUniform(0, 0, squareCam.translate(0f, 0f, 0f, new Matrix4f()));
		if (	window.cursorXFloat * window.getAspectRatio() * 50 > -50 &&
				window.cursorXFloat * window.getAspectRatio() * 50 < -20 &&
				window.cursorYFloat * 50 > -5 &&
				window.cursorYFloat * 50 < 5) {
			buttonShader.setUniform(2, 0, time);
			if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
				return true;
			}
		} else {
			buttonShader.setUniform(2, 0, 0f);
		}
		quitTexture.bind();
		buttonVAO.drawTriangles();
		buttonVAO.unbind();
		
		time -= deltaTime;
		if (time < 0) {
			time = 1;
		}
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
}
