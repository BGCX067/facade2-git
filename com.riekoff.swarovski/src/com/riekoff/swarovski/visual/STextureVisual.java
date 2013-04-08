package com.riekoff.swarovski.visual;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.texture.CCTexture2D;
import cc.creativecomputing.graphics.texture.CCTextureIO;

public class STextureVisual extends SVisual{

	
	@CCControl(name = "scale", min = 0, max = 1)
	private float _cScale = 1f;
	@CCControl(name = "space", min = 0, max = 10)
	private float _cSpace = 0;
	@CCControl(name = "speed", min = -100, max = 100)
	private float _cSpeed = 0;
	@CCControl(name = "y", min = 0, max = 1)
	private float _cY = 1f;
	@CCControl(name = "alpha", min = 0, max = 1)
	private float _cAlpha = 1f;

	private CCTexture2D _myTypo;
	
	private float _myTypoPosition;
	
	public STextureVisual(int theContentWidth, int theContentHeight, String theTexture){
		super(theContentWidth, theContentHeight);
		_myTypo = new CCTexture2D(CCTextureIO.newTextureData(theTexture));
	}
	
	@Override
	public void update(float theDeltaTime) {
		_myTypoPosition += theDeltaTime * _cSpeed;
		if(_myTypoPosition >= 0){
			_myTypoPosition -= (float)_myContentHeight / _myTypo.height() * _myTypo.width() * _cScale * (1 + _cSpace);
		}
	}

	public void draw(CCGraphics g) {
		g.color(_cAlpha);
		float x = _myTypoPosition;
		float myWidth = (float)_myContentHeight / _myTypo.height() * _myTypo.width() * _cScale;
		float myHeight = _myContentHeight * _cScale;
		while(x < _myContentWidth){
			g.image(_myTypo, x, _cY * _myContentHeight, myWidth, myHeight);
			x+=10;
			x+= myWidth * (1 + _cSpace);
		}
	}
}
