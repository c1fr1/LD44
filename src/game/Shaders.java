package game;

import engine.OpenGL.ShaderProgram;

public class Shaders {
	public static ShaderProgram flipShader;
	public static ShaderProgram textureShader;
	public static ShaderProgram shotShader;
	public static ShaderProgram asteroidShader;
	public static ShaderProgram asteroidEZShader;
	public static ShaderProgram starsShader;
	public static ShaderProgram crosshairShader;
	public static ShaderProgram baseFrameShader;
	public static ShaderProgram baseLightShader;
	public static ShaderProgram baseHUDShader;
	public static ShaderProgram shipHUDShader;
	public static ShaderProgram buttonShader;
	public static void createMainShaders() {
		flipShader = new ShaderProgram("flipShader");
		textureShader = new ShaderProgram("textureShader");
		shotShader = new ShaderProgram("shotShader");
		asteroidShader = new ShaderProgram("asteroidShader");
		asteroidEZShader = new ShaderProgram("asteroidEZShader");
		starsShader = new ShaderProgram("starsShader");
		crosshairShader = new ShaderProgram("crosshairShader");
		baseFrameShader = new ShaderProgram("baseFrameShader");
		baseLightShader = new ShaderProgram("baseLightShader");
		baseHUDShader = new ShaderProgram("baseHUDShader");
		shipHUDShader = new ShaderProgram("shipHUDShader");
		buttonShader = new ShaderProgram("buttonShader");
	}
}