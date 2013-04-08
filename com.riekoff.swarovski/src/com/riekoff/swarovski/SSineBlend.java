package com.riekoff.swarovski;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.CCAppContextShared;
import cc.creativecomputing.CCApplicationManager;
import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.math.CCMath;
import cc.creativecomputing.math.signal.CCSawSignal;
import cc.creativecomputing.math.signal.CCTriSignal;

public class SSineBlend extends CCApp{
	
	@CCControl(name = "speed", min = 0, max = 10)
	private float _cSpeed = 0;
	
	@CCControl(name = "pow", min = 0, max = 10)
	private float _cPow = 0;
	
	@CCControl(name = "min", min = 0, max = 1)
	private float _cMin = 0;
	
	@CCControl(name = "max", min = 0, max = 1)
	private float _cMax = 0;
	
	private float _myColor = 0;
	private float _myAngle = 0;
	
	private CCTriSignal _myTriSignal;
	
	@Override
	public void setup() {
		addControls("app", "app", this);
		
		_myTriSignal = new CCTriSignal();
	}
	
	//2 0.026
	
	@Override
	public void update(float theDeltaTime) {
		_myAngle += theDeltaTime * _cSpeed;
		_myColor = _myTriSignal.value(_myAngle);
		_myColor = CCMath.pow(_myColor, _cPow);
		_myColor = CCMath.blend(_cMin, _cMax, _myColor);
	}
	
	
	public void draw(){
		g.clearColor(_myColor);
		g.clear();
	}
	
	public static void main(String[] args) {
		CCApplicationManager myManager = new CCApplicationManager(SSineBlend.class);
		myManager.settings().size(1024, 768);
		myManager.settings().location(0, 0);
		myManager.settings().antialiasing(8);
		myManager.settings().undecorated(true);
		myManager.settings().display(0);
		myManager.settings().uiTranslation(100, 100);
		myManager.start();
	}
}
