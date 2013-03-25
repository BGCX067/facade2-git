package com.riekoff.swarovski.visual;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCDrawMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.math.signal.CCSimplexNoise;

public class SNoiseCurveVisual extends SVisual{

	@CCControl(name = "scale", min = 0f, max = 1, external = true)
	private float _cScale = 0.1f;
	
	@CCControl(name = "sub scale", min = 0f, max = 1, external = true)
	private float _cSubScale = 0.1f;
	
	@CCControl(name = "offset", min = 0, max = 100, external = true)
	private float _cOffset = 0f;
	
	@CCControl(name = "noise x", min = 0, max = 1000, external = true)
	private float _cNoiseX = 1f;
	
	@CCControl(name = "noise y", min = 0, max = 1000, external = true)
	private float _cNoiseY = 1f;
	
	@CCControl(name = "bands", min = 1, max = 10, external = true)
	private float _cBands = 1f;
	
	@CCControl(name = "lacunarity", min = 1, max = 10, external = true)
	private float _cLacunarity= 1f;
	
	@CCControl(name = "gain", min = 0, max = 1, external = true)
	private float _cGain = 1f;
	
	@CCControl(name = "noise output", min = 0, max = 10, external = true)
	private float _cNoiseOutput = 1f;
	
	@CCControl(name = "stroke weight", min = 0, max = 10, external = true)
	private float _cStrokeWeight = 1f;
	
	@CCControl(name = "alpha", min = 0, max = 1, external = true)
	private float _cAlpha = 0;
	

	private CCSimplexNoise _myNoise;

	private float _myNoiseX = 0;
	private float _myNoiseY = 0;

	public SNoiseCurveVisual(int theContentWidth, int theContentHeight) {
		super(theContentWidth, theContentHeight);

		_myNoise = new CCSimplexNoise();
	}

	@Override
	public void update(float theDeltaTime) {
		_myNoiseX += theDeltaTime * _cNoiseX;
		_myNoiseY += theDeltaTime * _cNoiseY;

		_myNoise.scale(_cScale * _cSubScale);
		_myNoise.offset(_cOffset, 0, 0);
		_myNoise.bands(_cBands);
		_myNoise.lacunarity(_cLacunarity);
		_myNoise.gain(_cGain);
	}
	
	@Override
	public void draw(CCGraphics g) {
		g.color(1f,_cAlpha);
		g.strokeWeight(_cStrokeWeight);
		g.beginShape(CCDrawMode.LINE_STRIP);
		for(int x = 0; x < _myContentWidth;x++){
			g.vertex(x, _myNoise.value(x + _myNoiseX, _myNoiseY) * _myContentHeight * _cNoiseOutput);
		}
		g.endShape();
	}
}
