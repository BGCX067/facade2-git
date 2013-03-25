package com.riekoff.swarovski;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.CCAppContextShared;
import cc.creativecomputing.CCApplicationManager;
import cc.creativecomputing.CCApplicationSettings.CCCloseOperation;
import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.demo.CCTwoAppsTest.CCInnerApp;
import cc.creativecomputing.events.CCKeyEvent;
import cc.creativecomputing.graphics.CCColor;
import cc.creativecomputing.graphics.export.CCScreenCapture;
import cc.creativecomputing.math.util.CCArcball;

import com.riekoff.swarovski.app.SLimiter;
import com.riekoff.swarovski.visual.SNoiseCurveVisual;
import com.riekoff.swarovski.visual.SVisualManager;

public class SApp extends CCApp {
	
//	@CCControl(name = "steps", min = 0, max = 10000)
	private int _cSteps = 2400;
//	@CCControl(name = "rows", min = 0, max = 100)
	private int _cRows = 120;
//	@CCControl(name = "rad x", min = 0, max = 1000)
	private int _cRadX = 500;
	@CCControl(name = "rad y", min = 0, max = 1000)
	private int _cRadY = 1000;
	

	private CCArcball _myArcball;
	
	private SGeometry _myGeometry;
	private SVisualManager _myVisualManager;
	private SLimiter _myLimiter;
	
	private SOutputWindow _myOutputWindow;
	
	
	@Override
	public void setup() {
//		CCApplicationManager myManager = new CCApplicationManager(SOutputWindow.class);
//		myManager.settings().size(1024, 768);
//		myManager.settings().closeOperation(CCCloseOperation.HIDE_ON_CLOSE);
//		myManager.settings().location(0,0);
////		myManager.settings().display(1);
//		myManager.settings().undecorated(true);
//		myManager.settings().appContext(sharedContext);
//		myManager.settings().background(CCColor.BLACK);
//		myManager.start();
		
		_myArcball = new CCArcball(this);
		
		_myGeometry = new SGeometry(_cRadX, _cSteps, _cRows);
		addControls("app", "geometry", 0, _myGeometry);
		_myVisualManager = new SVisualManager(g, _cSteps, _cRows);
		SNoiseCurveVisual myVisual = new SNoiseCurveVisual(_cSteps, _cRows);
		_myVisualManager.addVisual(myVisual);
		addControls("visual","noise curve", 0, myVisual);
		
		_myLimiter = new SLimiter(g, _myVisualManager);
		addControls("app", "limit", 1, _myLimiter);
		_myGeometry.input(_myLimiter.output());
		
		
//		_myOutputWindow = (SOutputWindow)myManager.app();
//		_myOutputWindow.geometry(_myGeometry);
//		addControls("output", "output", _myOutputWindow);
	}
	

	@Override
	public void update(final float theDeltaTime) {
		_myVisualManager.update(theDeltaTime);
		_myLimiter.update(theDeltaTime);
	}

	@Override
	public void draw() {
		_myVisualManager.draw(g);
		g.clearColor(55);
		g.clear();
		g.pushMatrix();
		_myArcball.draw(g);
		_myGeometry.draw(g);
		g.clearDepthBuffer();
		
//		_myGeometry.drawOutput(g);
		
		g.popMatrix();
		g.clearDepthBuffer();
		

		g.color(255);
		g.image(_myVisualManager.output(), -width/2,-height/2 + _cRows);
		g.image(_myLimiter.output(), -width/2,-height/2);
		
		System.out.println(frameRate);
		
	}
	
	@Override
	public void keyPressed(CCKeyEvent theKeyEvent) {
		switch(theKeyEvent.keyCode()){
		case CCKeyEvent.VK_C:
			CCScreenCapture.capture("export/frame"+frameCount+".png", width, height);
			break;
		}
	}

	private static CCAppContextShared sharedContext = new CCAppContextShared();

	public static void main(String[] args) {
		sharedContext = new CCAppContextShared();
		CCApplicationManager myManager = new CCApplicationManager(SApp.class);
		myManager.settings().size(1500, 900);
		myManager.settings().antialiasing(8);
		myManager.settings().appContext(sharedContext);
		myManager.start();
	}
}
