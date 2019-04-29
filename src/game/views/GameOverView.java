package game.views;

import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import game.UserControls;

import static game.Main.squareCam;
import static game.Shaders.textureShader;

public class GameOverView extends EnigView {
	public static GameOverView main;
	public static Texture[] gameOverScreens;
	public static int deathReason = 0;
	public static VAO textVAO;
	public GameOverView(EnigWindow window) {
		super(window);
		main = this;
		gameOverScreens = new Texture[] {
			new Texture("res/textures/gameOver/baseDestroyed.png"),  //0
			new Texture("res/textures/gameOver/bEnergyDepleted.png"),//1
			new Texture("res/textures/gameOver/dEnergyDepleted.png"),//2
			new Texture("res/textures/gameOver/droneDestroyed.png"), //3
			new Texture("res/textures/gameOver/outOfRange.png")      //4
		};
		textVAO = new VAO(-50, -50, 100, 100);
	}
	
	public boolean loop() {
		FBO.prepareDefaultRender();
		textureShader.enable();
		textureShader.setUniform(0, 0, squareCam);
		gameOverScreens[deathReason].bind();
		textVAO.fullRender();
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
}
