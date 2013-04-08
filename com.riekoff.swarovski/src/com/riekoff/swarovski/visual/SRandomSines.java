package com.riekoff.swarovski.visual;

import com.jogamp.opengl.cg.CGparameter;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCColor;
import cc.creativecomputing.graphics.CCDrawMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.shader.CCCGShader;
import cc.creativecomputing.graphics.texture.CCTexture.CCTextureTarget;
import cc.creativecomputing.graphics.texture.CCTexture2D;
import cc.creativecomputing.graphics.texture.CCTextureData;
import cc.creativecomputing.io.CCIOUtil;
import cc.creativecomputing.math.CCMath;

public class SRandomSines extends SVisual {
	
	@CCControl(name = "speed", min = 0, max = 20, external = true)
	private float _cSpeed = 0;
	@CCControl(name = "freq pow", min = 0, max = 20, external = true)
	private float _cFreqPow = 0;
	@CCControl(name = "dark time", min = 0, max = 200, external = true)
	private float _cDarkTime = 0;
	@CCControl(name = "minBright", min = 0, max = 1, external = true)
	private float _cMinBright = 0;
	@CCControl(name = "maxBright", min = 0, max = 1, external = true)
	private float _cMaxBright = 0;
	@CCControl(name = "bright pow", min = 0, max = 10, external = true)
	private float _cBrightPow = 0;
	
	private CCTexture2D _myRandom;
	private CCCGShader _mySineShader;
	private CGparameter _myTimeParameter;
	private CGparameter _myFreqPowParameter;
	private CGparameter _myPIParameter;
	private CGparameter _myDarkTimeParameter;
	
	private CGparameter _myMinBrightParameter;
	private CGparameter _myMaxBrightParameter;
	private CGparameter _myBrightPowParameter;
	
	private float _myTime = 0;

	public SRandomSines(int theContentWidth, int theContentHeight) {
		super(theContentWidth, theContentHeight);
		
		CCTextureData myData = new CCTextureData(theContentWidth, theContentHeight);
		for(int x = 0; x < theContentWidth;x++){
			for(int y = 0; y < theContentHeight; y++){
				myData.setPixel(x, y, new CCColor(CCMath.random(),CCMath.random(),CCMath.random()));
			}
		}
		_myRandom = new CCTexture2D(myData, CCTextureTarget.TEXTURE_RECT);
		
		_mySineShader = new CCCGShader(null, CCIOUtil.classPath(this, "randomsines.fp"));
		_myTimeParameter = _mySineShader.fragmentParameter("time");
		_myFreqPowParameter = _mySineShader.fragmentParameter("freqPow");
		_myPIParameter = _mySineShader.fragmentParameter("PI");
		_myDarkTimeParameter = _mySineShader.fragmentParameter("darkTime");
		
		_myMinBrightParameter = _mySineShader.fragmentParameter("minBright");
		_myMaxBrightParameter = _mySineShader.fragmentParameter("maxBright");
		_myBrightPowParameter = _mySineShader.fragmentParameter("brightPow");
		_mySineShader.load();
	}
	
	@Override
	public void update(float theDeltaTime) {
		_myTime += theDeltaTime * _cSpeed;
	}
	
	public CCTexture2D randomTexture(){
		return _myRandom;
	}
	
	public void draw(CCGraphics g){
		_mySineShader.start();
		_mySineShader.parameter(_myTimeParameter, _myTime);
		_mySineShader.parameter(_myFreqPowParameter, _cFreqPow);
		_mySineShader.parameter(_myPIParameter, CCMath.PI);
		_mySineShader.parameter(_myDarkTimeParameter, _cDarkTime);
		_mySineShader.parameter(_myMinBrightParameter, _cMinBright);
		_mySineShader.parameter(_myMaxBrightParameter, _cMaxBright);
		_mySineShader.parameter(_myBrightPowParameter, _cBrightPow);
		g.texture(0, _myRandom);
		g.beginShape(CCDrawMode.QUADS);
		g.textureCoords(0,0,0);
		g.vertex(0,0);
		g.textureCoords(0,_myContentWidth,0);
		g.vertex(_myContentWidth,0);
		g.textureCoords(0,_myContentWidth,_myContentHeight);
		g.vertex(_myContentWidth,_myContentHeight);
		g.textureCoords(0,0,_myContentHeight);
		g.vertex(0,_myContentHeight);
		g.endShape();
		g.noTexture();
		_mySineShader.end();
	}
}
