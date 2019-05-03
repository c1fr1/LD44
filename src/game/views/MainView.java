package game.views;

import engine.*;
import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;
import engine.OpenGL.*;
import engine.Platform.Ray3f;
import game.Asteroid;
import game.Main;
import game.Shot;
import game.UserControls;
import game.entities.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static game.Main.squareCam;
import static game.Shaders.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;
	
	public static VAO skySphere;
	public VAO crosshairVAO;
	
	public VAO baseVAO;
	
	public VAO hudVAO;
	
	public Texture baseHUDTex;
	public Texture shipHUDTex;
	
	public float totalTime;
	
	public FBO mainFrameBuffer;
	
	public Asteroid[] asters;
	
	public Player player;
	
	public ArrayList<Shot> shots = new ArrayList<>();
	
	public Sound[] tutorialSounds;
	
	public Sound proximityWarningSound;
	public Sound lowSignalSound;
	public SoundSource proximityWarningSource;
	public SoundSource lowSignalSource;
	public float proximityWarningTimer;
	public float lowSignalTimer;
	
	public float baseIntensity = 10;
	
	public boolean gameIsOver = false;
	
	public int tutorialIndex = -2;
	private float tutorialTimer;
	private boolean tutorialActionDone = false;
	
	public MainView(EnigWindow window) {
		super(window);
		
		window.toggleCursorInput();
		
		skySphere = new VAO("res/objects/2Sphere.obj");
		crosshairVAO = new VAO(-5, -5, 10, 10);
		
		baseVAO = new VAO("res/objects/lightSource.obj");
		
		hudVAO = new VAO(-12.5f, -12.5f, 25, 25);
		
		baseHUDTex = new Texture("res/textures/HUDBase.png");
		shipHUDTex = new Texture("res/textures/HUDShip.png");
		
		asters = new Asteroid[400];
		for (int i = 0; i < asters.length; ++i) {
			asters[i] = new Asteroid(totalTime);
		}
		
		player = new Player(window);
		player.x = -20f;
		
		mainFrameBuffer = new FBO(new Texture(window.getWidth(), window.getHeight()));
		
		tutorialSounds = new Sound[] {
				new Sound("res/sounds/movement controls.wav"),
				new Sound("res/sounds/transport ship introduction.wav"),
				new Sound("res/sounds/confirm transport ship.wav"),
				new Sound("res/sounds/look around.wav"),
				new Sound("res/sounds/left hud.wav"),
				new Sound("res/sounds/right hud.wav"),
				new Sound("res/sounds/attack controls.wav"),
				new Sound("res/sounds/attack info.wav"),
				new Sound("res/sounds/astroids spawned.wav"),
				new Sound("res/sounds/start mission.wav"),
				new Sound("res/sounds/intro lock velocity.wav"),
				new Sound("res/sounds/intro radar.wav")
		};
		
		proximityWarningSource = new SoundSource();
		proximityWarningSource.setPitch(1.2f);
		lowSignalSource = new SoundSource();
		lowSignalSource.setPitch(1.2f);
		proximityWarningSound = new Sound("res/sounds/notTutorial/proximityWarning.wav");
		lowSignalSound = new Sound("res/sounds/notTutorial/lowSignal.wav");
	}
	
	public void reset() {
		for (int i = 0; i < asters.length; ++i) {
			asters[i] = new Asteroid(totalTime);
		}
		baseIntensity = 10;
		player.x = -20;
		player.y = 0;
		player.z = 0;
		player.velocity = new Vector3f();
		player.energy = 1;
		shots = new ArrayList<>();
		gameIsOver = false;
		tutorialIndex = -2;
		tutorialTimer = 0f;
		tutorialActionDone = false;
	}
	
	public boolean loop() {
		
		manageScene();
		
		renderScene();
		
		if (UserControls.quit(window) || gameIsOver) {
			return true;
		}
		return false;
	}
	
	public void manageScene() {
		totalTime += deltaTime;
		
		manageShots();
		
		if (tutorialIndex == -2 || tutorialIndex > 4) {
			manageAsters();
		}
		
		player.updateRotation(window);
		player.updateMovement(window, deltaTime);
		
		checkMatchVelocity();
		
		manageDeath();
		
		manageTutorial();
	}
	
	public void manageTutorial() {
		Main.source.setPitch(1.02f);
		if (tutorialIndex == -1) {
			if (tutorialTimer > 1) {
				++tutorialIndex;
				Main.source.playSound(tutorialSounds[0]);
			}
		} else if (tutorialIndex == 0) {
			if (player.velocity.lengthSquared() > 0.5f) {
				if (tutorialTimer > 12.5f) {
					++tutorialIndex;
					Main.source.playSound(tutorialSounds[1]);
					tutorialTimer = 0;
				}
			}
		}else if (tutorialIndex == 1) {
			if (tutorialTimer > 6.5f) {
				Ray3f playerRay = player.getRay();
				float tVal = playerRay.closestTTo(new Vector3f(0f, 0f, 0f));
				if (tVal > 0 && playerRay.getPointAt(tVal).lengthSquared() < 100) {
					++tutorialIndex;
					tutorialTimer = 0;
					Main.source.playSound(tutorialSounds[2]);
				} else {
					if (tutorialTimer > 10) {
						Main.source.playSound(tutorialSounds[3]);
						tutorialTimer = 4;
					}
				}
			}
		} else if (tutorialIndex == 2) {
			if (tutorialTimer > 2.2 && tutorialTimer < 2.4) {
				Main.source.playSound(tutorialSounds[4]);
				tutorialTimer = 2.5f;
			} else if (tutorialTimer > 11 && tutorialTimer < 11.1) {
				Main.source.playSound(tutorialSounds[5]);
				tutorialTimer = 11.2f;
			} else if (tutorialTimer > 22.5) {
				++tutorialIndex;
				tutorialTimer = 0;
				Main.source.playSound(tutorialSounds[6]);
			}
		} else if (tutorialIndex == 3) {
			if (shots.size() > 0) {
				if (tutorialTimer > 4.5) {
					++tutorialIndex;
					tutorialTimer = 0;
					Main.source.playSound(tutorialSounds[7]);
				}
			}
		} else if (tutorialIndex == 4) {
			if (tutorialTimer > 5) {
				++tutorialIndex;
				tutorialTimer = 0;
				Main.source.setPitch(1.2f);
				Main.source.playSound(tutorialSounds[8]);
			}
		} else if (tutorialIndex == 5) {
			if (tutorialTimer > 2.0f) {
				++tutorialIndex;
				tutorialTimer = 0;
				Main.source.setPitch(1.02f);
				Main.source.playSound(tutorialSounds[9]);
			}
		} else if (tutorialIndex == 6) {
			if (tutorialTimer > 4f) {
				++tutorialIndex;
				tutorialTimer = 0;
				Main.source.playSound(tutorialSounds[10]);
			}
		} else if (tutorialIndex == 7) {
			if (tutorialActionDone) {
				if (tutorialTimer > 7.2f) {
					++tutorialIndex;
					tutorialTimer = 0;
					Main.source.playSound(tutorialSounds[11]);
				}
			}
		}
		tutorialTimer += deltaTime;
	}
	
	public void manageDeath() {
		baseIntensity -= 0.05 * deltaTime;
		if (baseIntensity < 0) {
			endGame(1);
		}
		if (player.energy < 0) {
			endGame(2);
		}
		float playerLength = player.length();
		if (playerLength > 300f) {
			if (tutorialIndex > -2 && tutorialIndex < 4) {
				player.x = 20f;
				player.y = 0f;
				player.z = 0f;
				player.velocity = new Vector3f();
			} else {
				endGame(4);
			}
		} else if (playerLength > 250) {
			if (lowSignalTimer < 0) {
				lowSignalSource.playSound(lowSignalSound);
				lowSignalTimer = 10f;
			}
		}
		if (player.energy > 1) {
			player.energy = 1;
		}
		if (baseIntensity > 20) {
			baseIntensity = 20;
		}
		proximityWarningTimer -= deltaTime;
		lowSignalTimer -= deltaTime;
	}
	
	public void checkMatchVelocity() {
		if (UserControls.matchVelocity(window)) {
			Ray3f playerRay = player.getRay();
			float bestT = playerRay.closestTTo(new Vector3f(0f, 0f, 0f));
			if (bestT > 0 && playerRay.getPointAt(bestT).lengthSquared() < 100) {
				player.energy -= 0.05f + 0.001 * player.velocity.length();
				player.velocity.x = 0;
				player.velocity.y = 0;
				player.velocity.z = 0;
				if (tutorialIndex == 7) {
					tutorialActionDone = true;
				}
			} else {
				int bestIndex = -1;
				bestT = Float.POSITIVE_INFINITY;
				for (int i = 0; i < asters.length; ++i) {
					float tVal = playerRay.closestTTo(asters[i]);
					if (tVal > 0) {
						if (playerRay.getPointAt(tVal).distanceSquared(asters[i]) < asters[i].scale * asters[i].scale) {
							if (tVal < bestT) {
								bestT = tVal;
								bestIndex = i;
							}
						}
					}
				}
				if (bestIndex >= 0) {
					if (tutorialIndex == 6) {
						tutorialActionDone = true;
					}
					player.energy -= 0.05f + 0.001 * asters[bestIndex].velocity.distance(player.velocity);
					player.velocity = new Vector3f(asters[bestIndex].velocity);
				}
			}
		}
	}
	
	public void manageShots() {
		if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
			shots.add(new Shot(player));
			player.energy -= 0.01f;
		}
		for (int i = 0; i < shots.size(); ++i) {
			shots.get(i).add(shots.get(i).velocity.mul(deltaTime, new Vector3f()));
			if (shots.get(i).distanceSquared(player) > 1000000) {
				shots.remove(i);
				--i;
				continue;
			}
			if (shots.get(i).lengthSquared() < 100) {
				baseIntensity += 0.5f;
				shots.remove(i);
				--i;
				continue;
			}
			for (int j = 0; j < asters.length; ++j) {
				float scale = asters[j].scale;
				if (asters[j].impactTimer > -0.5) {
					scale = scale * (1 - asters[j].impactTimer / 11);
				}
				Ray3f ray = shots.get(i).getRay(deltaTime);
				float tVal = ray.closestTTo(asters[j]);
				Vector3f pointClose = ray.getPointAt(tVal);
				if (pointClose.distance(asters[j]) > scale + 0.1f) {
					continue;
				}
				if (tVal < 0 || tVal > 1) {
					pointClose = ray.start;
					if (pointClose.distance(asters[j]) > scale + 0.1f) {
						continue;
					}
				}
				if (shots.get(i).distance(asters[j]) < scale + 0.1f) {
					if (asters[j].impactTimer < 0) {
						asters[j].impactLocation = new Vector3f(shots.get(i)).sub(asters[j]).rotateX(-asters[j].rotation);
						asters[j].impactTimer = 0;
					}
					shots.remove(i);
					--i;
					break;
				}
			}
		}
	}
	
	public void manageAsters() {
		for (int i = 0; i < asters.length; ++i) {
			asters[i].add(asters[i].velocity.mul(deltaTime, new Vector3f()));
			if (asters[i].lengthSquared() > 250000) {
				asters[i] = new Asteroid(totalTime);
			}
			if (asters[i].impactTimer < -0.5 && asters[i].length() < 10 + asters[i].scale) {
				if (baseIntensity > 5) {
					baseIntensity -= 5;
					asters[i] = new Asteroid(totalTime);
				} else {
					endGame(0);
				}
			}
			if (asters[i].impactTimer < -0.5 && asters[i].distanceSquared(player) < asters[i].scale * asters[i].scale) {
				endGame(3);
			}
			asters[i].rotation += asters[i].omega * deltaTime;
			if (asters[i].impactTimer > -0.5f) {
				asters[i].impactTimer += deltaTime;
				if (asters[i].impactTimer > 10) {
					asters[i].impactTimer = 10;
					asters[i].hitPlayerTimer -= deltaTime;
					asters[i].velocity.x = (player.x + player.velocity.x * asters[i].hitPlayerTimer - asters[i].x) / asters[i].hitPlayerTimer;
					asters[i].velocity.y = (player.y + player.velocity.y * asters[i].hitPlayerTimer - asters[i].y) / asters[i].hitPlayerTimer;
					asters[i].velocity.z = (player.z + player.velocity.z * asters[i].hitPlayerTimer - asters[i].z) / asters[i].hitPlayerTimer;
					if (asters[i].hitPlayerTimer < 0) {
						asters[i] = new Asteroid(totalTime);
						player.energy += asters[i].scale / 15;
					}
				}
			}
		}
	}
	
	public void renderScene() {
		FBO.prepareDefaultRender();
		renderCrosshair();
		renderHUD();
		renderBase();
		if (tutorialIndex == -2 || tutorialIndex > 4) {
			renderAsteroids();
		}
		renderShots();
		renderStars();
	}
	
	public void renderCrosshair() {
		crosshairShader.enable();
		crosshairShader.setUniform(0, 0, squareCam);
		crosshairShader.setUniform(2, 0, player.energy);
		crosshairShader.setUniform(2, 1, player.energy);
		
		Ray3f playerRay = player.getRay();
		float bestT = playerRay.closestTTo(new Vector3f(0f, 0f, 0f));
		if (bestT > 0 && playerRay.getPointAt(bestT).lengthSquared() < 100) {
			crosshairShader.setUniform(2, 1, player.energy - 0.05f - 0.001f * player.velocity.length());
		} else {
			int bestIndex = -1;
			bestT = Float.POSITIVE_INFINITY;
			for (int i = 0; i < asters.length; ++i) {
				float tVal = playerRay.closestTTo(asters[i]);
				if (tVal > 0) {
					if (playerRay.getPointAt(tVal).distanceSquared(asters[i]) < asters[i].scale * asters[i].scale) {
						if (tVal < bestT) {
							bestT = tVal;
							bestIndex = i;
						}
					}
				}
			}
			if (bestIndex >= 0) {
				crosshairShader.setUniform(2, 1, player.energy - 0.05f - 0.001f * asters[bestIndex].velocity.distance(player.velocity));
			}
		}
		
		crosshairVAO.fullRender();
	}
	
	public void renderHUD() {
		baseHUDShader.enable();
		baseHUDTex.bind();
		baseHUDShader.setUniform(0, 0, squareCam.translate(-window.getAspectRatio() * 50f + 12.5f, -37.5f, 0f, new Matrix4f()));
		baseHUDShader.setUniform(2, 0, 1 - baseIntensity / 20f);
		
		float closestDist = Float.POSITIVE_INFINITY;
		for (int i = 0; i < asters.length; ++i) {
			if (asters[i].impactTimer < 0) {
				float dist = asters[i].length();
				if (dist < closestDist) {
					closestDist = dist;
				}
			}
		}
		baseHUDShader.setUniform(2, 1, 1 - closestDist / 200f);
		hudVAO.fullRender();
		if (closestDist < 50) {
			if (proximityWarningTimer < 0) {
				proximityWarningTimer = 10f;
				proximityWarningSource.playSound(proximityWarningSound);
			}
		}
		
		shipHUDShader.enable();
		shipHUDTex.bind();
		shipHUDShader.setUniform(0, 0, squareCam.translate(window.getAspectRatio() * 50f - 12.5f, -37.5f, 0f, new Matrix4f()));
		shipHUDShader.setUniform(2, 0, 1 - player.energy);
		baseHUDShader.setUniform(2, 1, player.length() / 400f);
		hudVAO.fullRender();
	}
	
	public void renderBase() {
		Matrix4f mat = player.getCameraMatrix().scale(10f);
		baseLightShader.enable();
		baseLightShader.setUniform(0, 0, mat);
		baseLightShader.setUniform(2, 0, 1 - 1 / baseIntensity);
		skySphere.fullRender();
		baseFrameShader.enable();
		baseFrameShader.setUniform(0, 0, mat);
		baseFrameShader.setUniform(2, 0, 2 - 2 / baseIntensity);
		baseVAO.fullRender();
	}
	
	public void renderShots() {
		shotShader.enable();
		skySphere.prepareRender();
		Matrix4f mat = player.getCameraMatrix();
		for (int i = 0; i < shots.size(); ++i) {
			shotShader.setUniform(0, 0, mat.translate(shots.get(i), new Matrix4f()).scale(0.1f));
			skySphere.drawTriangles();
		}
		skySphere.unbind();
	}
	
	public void renderAsteroids() {
		Asteroid.sphere.prepareRender();
		Matrix4f mat = player.getCameraMatrix();
		if (UserControls.seeAsteroids(window)) {
			player.energy -= deltaTime * 0.1f;
			asteroidEZShader.enable();
			for (int i = 0; i < asters.length; ++i) {
				Asteroid aster = asters[i];
				float scale = aster.scale;
				if (aster.impactTimer > -0.5) {
					scale = scale * (1 - aster.impactTimer / 11);
				}
				asteroidEZShader.setUniform(0, 0, mat.translate(aster, new Matrix4f()).scale(scale).rotateX(aster.rotation));
				asteroidEZShader.setUniform(0, 1, aster.randSeed);
				asteroidEZShader.setUniform(2, 0, player.sub(aster, new Vector3f()).normalize().rotateX(-aster.rotation));
				Asteroid.sphere.drawTriangles();
			}
		} else {
			asteroidShader.enable();
			for (int i = 0; i < asters.length; ++i) {
				Asteroid aster = asters[i];
				float scale = aster.scale;
				if (aster.impactTimer > -0.5) {
					scale = scale * (1 - aster.impactTimer / 11);
				}
				asteroidShader.setUniform(0, 0, mat.translate(aster, new Matrix4f()).scale(scale).rotateX(aster.rotation));
				asteroidShader.setUniform(0, 1, aster.randSeed);
				asteroidShader.setUniform(2, 0, aster.rotateX(-aster.rotation, new Vector3f()).normalize(-1));
				asteroidShader.setUniform(2, 1, 50 * baseIntensity / aster.length());
				if (aster.impactTimer > -0.5f) {
					asteroidShader.setUniform(2, 2, aster.impactLocation);
					asteroidShader.setUniform(2, 3, aster.impactTimer);
					asteroidShader.setUniform(2, 4, player.sub(aster, new Vector3f()).normalize().rotateX(-aster.rotation));
				} else {
					asteroidShader.setUniform(2, 3, 0f);
					asteroidShader.setUniform(2, 4, 0f, 1f, 0f);
				}
				Asteroid.sphere.drawTriangles();
			}
		}
	}
	
	public void renderStars() {
		glDisable(GL_CULL_FACE);
		starsShader.enable();
		starsShader.setUniform(0, 0, player.getCameraMatrix().translate(player.x, player.y, player.z).scale(9e4f));
		starsShader.setUniform(2, 0, totalTime);
		skySphere.fullRender();
		glEnable(GL_CULL_FACE);
	}
	
	public void endGame(int looseCondition) {
		GameOverView.deathReason = looseCondition;
		gameIsOver = true;
	}
}
