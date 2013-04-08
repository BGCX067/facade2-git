package com.riekoff.swarovski.app;

import com.riekoff.swarovski.app.SLightAnimation.STimeLineMode;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.CCApplicationManager;
import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCColor;

public class SPlayerApp extends CCApp{
	
	@CCControl(name = "x Offset", min = -1000, max = 1000)
	private int _cXOffset = 0;
	@CCControl(name = "y Offset", min = -1000, max = 1000)
	private int _cYOffset = 0;
	
	private SPixelMapper _myMapper;

	private SLightAnimation _myAnimation;
	
	public void setup(){
		_myAnimation = new SLightAnimation(this, STimeLineMode.OFF);
		addControls("app", "output", this);
	}
	
	@Override
	public void update(float theDeltaTime) {
		_myAnimation.update(theDeltaTime);
	}
	
	public void draw(){

		_myAnimation.drawSimulation(g);
		g.clear();
		g.color(255);
		g.pushMatrix();
		g.translate(-width/2 + _cXOffset, height/2 - _cYOffset);
		_myAnimation.drawOutput(g);
//		_myAnimation.drawLayers(g);
		g.popMatrix();
	}
	
	public static void main(String[] args) {
		CCApplicationManager myManager = new CCApplicationManager(SPlayerApp.class);
		myManager.settings().size(1024, 768);
		myManager.settings().location(0,30);
		myManager.settings().display(0);
		myManager.settings().undecorated(true);
		myManager.settings().background(CCColor.BLACK);
		myManager.start();
	}
}
