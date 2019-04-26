package game.views;

import engine.*;
import engine.OpenGL.*;
import game.UserControls;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MainView extends EnigView {
	public static MainView main;
	
	public FBO mainFrameBuffer;
	
	public MainView(EnigWindow window) {
		super(window);
		
		mainFrameBuffer = new FBO(new Texture(window.getWidth(), window.getHeight()));
		
	}
	
	public void reset() {
	
	}
	
	public boolean loop() {
		
		manageScene();
		
		renderScene();
		
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
	
	public void manageScene() {
	
	}
	
	public void renderScene() {
		FBO.prepareDefaultRender();
		//mainFrameBuffer.prepareForTexture();
	}
}
