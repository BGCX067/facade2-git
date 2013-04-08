package com.riekoff.swarovski.app;

import com.riekoff.swarovski.visual.SRandomSines;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.CCApplicationManager;

public class SRandomSinesDemo extends CCApp {
	
	private SRandomSines _myRandomSines;

	@Override
	public void setup() {
		_myRandomSines = new SRandomSines(width,height);
		addControls("app", "sines", _myRandomSines);
	}

	@Override
	public void update(final float theDeltaTime) {
		_myRandomSines.update(theDeltaTime);
	}

	@Override
	public void draw() {
		g.clear();
//		g.image(_myRandomSines.randomTexture(), 0,0);
		g.pushMatrix();
		g.translate(-width / 2, -height/2);
		_myRandomSines.draw(g);
		g.popMatrix();
	}

	public static void main(String[] args) {
		CCApplicationManager myManager = new CCApplicationManager(SRandomSinesDemo.class);
		myManager.settings().size(500, 500);
		myManager.settings().undecorated(true);
		myManager.settings().location(0, 0);
		myManager.settings().display(0);
		myManager.start();
	}
}

