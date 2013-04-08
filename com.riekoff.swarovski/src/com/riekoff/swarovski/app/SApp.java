package com.riekoff.swarovski.app;

import com.riekoff.swarovski.app.SLightAnimation.STimeLineMode;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.CCAppContextShared;
import cc.creativecomputing.CCApplicationManager;
import cc.creativecomputing.CCApplicationSettings.CCCloseOperation;
import cc.creativecomputing.events.CCKeyEvent;
import cc.creativecomputing.graphics.CCColor;
import cc.creativecomputing.graphics.export.CCScreenCapture;
import cc.creativecomputing.math.util.CCArcball;

public class SApp extends CCApp {
	
	private CCArcball _myArcball;
    
	private SDVIOut _myOutputWindow;
	
	private SLightAnimation _myAnimation;
    
    
	@Override
	public void setup() {
		_myAnimation = new SLightAnimation(this, STimeLineMode.OFF);
		
		_myArcball = new CCArcball(this);
		
		CCApplicationManager myManager = new CCApplicationManager(SDVIOut.class);
		myManager.settings().size(1024, 768);
		myManager.settings().closeOperation(CCCloseOperation.HIDE_ON_CLOSE);
		myManager.settings().location(0,0);
		myManager.settings().display(0);
		myManager.settings().undecorated(true);
		myManager.settings().appContext(sharedContext);
		myManager.settings().background(CCColor.BLACK);
//		myManager.start();
		_myOutputWindow = (SDVIOut)myManager.app();
//		_myOutputWindow.mapper(_myPixelMapper);
//		addControls("output", "output", _myOutputWindow);
	}
	
	long time = System.currentTimeMillis();
	

	@Override
	public void update(final float theDeltaTime) {
//		_myTimeline.update(theDeltaTime);
		_myAnimation.update(theDeltaTime);
		
		System.out.println(frameRate);
	}

	public void draw() {
		g.clear();
		g.pushMatrix();
		_myArcball.draw(g);
		_myAnimation.drawSimulation(g);
		g.popMatrix();
		g.clearDepthBuffer();
		
		g.color(255);
		g.pushMatrix();
		g.translate(-width/2,-height/2);
		_myAnimation.drawOutput(g);
//		_myAnimation.drawLayers(g);
		g.popMatrix();
		
		g.strokeWeight(1);
	}
	
	@Override
	public void keyPressed(CCKeyEvent theKeyEvent) {
		switch(theKeyEvent.keyCode()){
		case CCKeyEvent.VK_C:
			CCScreenCapture.capture("export3/frame"+frameCount+".png", width, height);
			break;
		}
	}

	boolean bTexture = true;
	boolean bStroke = true;
	
	private static CCAppContextShared sharedContext = new CCAppContextShared();

	public static void main(String[] args) {
		final CCApplicationManager myManager = new CCApplicationManager(SApp.class);
		myManager.settings().size(1500, 800);
		myManager.settings().antialiasing(8);
		myManager.settings().vsync(false);
		myManager.settings().appContext(sharedContext);
		myManager.start();
	}
}
