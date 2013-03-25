package com.riekoff.swarovski.app;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCDrawMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.CCRenderBuffer;
import cc.creativecomputing.graphics.shader.CCCGShader;
import cc.creativecomputing.graphics.texture.CCTexture2D;
import cc.creativecomputing.graphics.texture.CCTexture.CCTextureTarget;
import cc.creativecomputing.graphics.texture.CCTexture.CCTextureWrap;
import cc.creativecomputing.io.CCIOUtil;

import com.jogamp.opengl.cg.CGparameter;
import com.riekoff.swarovski.visual.SVisualManager;

public class SLimiter {

	@CCControl(name = "rise", min = 0, max = 10)
	private float _cRise = 1;
	@CCControl(name = "fall", min = 0, max = 10)
	private float _cFall = 1;
	@CCControl(name = "speed", min = 0, max = 1)
	private float _cSpeed = 1;
	
	private CCRenderBuffer _myLimitedColorsDST;
	private CCRenderBuffer _myLimitedColorsSRC;
	private CCCGShader _myLimitShader;
	private CGparameter _myRiseParameter;
	private CGparameter _myFallParameter;
	
	private CCGraphics g;
	private SVisualManager _myVisualManager;
	
	public SLimiter(CCGraphics theGraphics, SVisualManager theVisualManager){
		g = theGraphics;
		_myVisualManager = theVisualManager;
		
		_myLimitedColorsDST = new CCRenderBuffer(theGraphics, CCTextureTarget.TEXTURE_RECT, _myVisualManager.width(), _myVisualManager.height());
		_myLimitedColorsDST.attachment(0).wrap(CCTextureWrap.REPEAT);
		_myLimitedColorsSRC = new CCRenderBuffer(theGraphics, CCTextureTarget.TEXTURE_RECT, _myVisualManager.width(), _myVisualManager.height());
		_myLimitedColorsSRC.attachment(0).wrap(CCTextureWrap.REPEAT);
		_myLimitedColorsSRC.beginDraw();
		g.clearColor(0);
		g.clear();
		_myLimitedColorsSRC.endDraw();
		
		_myLimitShader = new CCCGShader(
			CCIOUtil.classPath(this, "limit.vp"),
			CCIOUtil.classPath(this, "limit.fp")
		);
		_myLimitShader.load();
		_myRiseParameter = _myLimitShader.fragmentParameter("rise");
		_myFallParameter = _myLimitShader.fragmentParameter("fall");
	}
	
	public CCTexture2D output(){
		return _myLimitedColorsSRC.attachment(0);
	}
	
	public void update(float theDeltaTime){
		_myLimitedColorsDST.beginDraw();
		g.clearColor(0);
		g.clear();
		g.color(255);
		g.pushMatrix();
		g.translate(-_myVisualManager.width() / 2, - _myVisualManager.height()/2);
		g.texture(0,_myVisualManager.output());
		g.texture(1,_myLimitedColorsSRC.attachment(0));
		
		_myLimitShader.start();
		_myLimitShader.parameter(_myRiseParameter, _cRise * theDeltaTime);
		_myLimitShader.parameter(_myFallParameter, _cFall * theDeltaTime);
		g.beginShape(CCDrawMode.QUADS);
		
		g.textureCoords(0,0,0);
		g.vertex(0,0);
		
		g.textureCoords(0,_myVisualManager.width(),0);
		g.vertex(_myVisualManager.width(),0);
		
		g.textureCoords(0,_myVisualManager.width(),_myVisualManager.height());
		g.vertex(_myVisualManager.width(),_myVisualManager.height());
		
		g.textureCoords(0,0,_myVisualManager.height());
		g.vertex(0,_myVisualManager.height());
		
		g.endShape();
		_myLimitShader.end();
		
		g.noTexture();
		g.popMatrix();
		_myLimitedColorsDST.endDraw();
		
		CCRenderBuffer myTmp = _myLimitedColorsDST;
		_myLimitedColorsDST = _myLimitedColorsSRC;
		_myLimitedColorsSRC = myTmp;
	}
}
