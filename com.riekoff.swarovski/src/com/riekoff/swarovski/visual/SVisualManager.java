package com.riekoff.swarovski.visual;

import java.util.ArrayList;
import java.util.List;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCAbstractGraphics.CCBlendMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.CCRenderBuffer;
import cc.creativecomputing.graphics.shader.imaging.CCGPUSeperateGaussianBlur;
import cc.creativecomputing.graphics.texture.CCTexture.CCTextureFilter;
import cc.creativecomputing.graphics.texture.CCTexture.CCTextureTarget;
import cc.creativecomputing.graphics.texture.CCTexture.CCTextureWrap;
import cc.creativecomputing.graphics.texture.CCTexture2D;

public class SVisualManager {
	
	@CCControl(name = "trace", min = 0, max = 1)
	private float _cTrace = 0;
	
	private CCRenderBuffer _myRenderBuffer;
	private CCRenderBuffer _myTraceRenderBuffer;
	
	private List<SVisual> _myVisuals = new ArrayList<SVisual>();
	public final static float MAXIMUM_BLUR_RADIUS = 50;
	
	@CCControl(name = "blur radius", min = 1, max = MAXIMUM_BLUR_RADIUS, external = true)
	private float _cBlurRadius = MAXIMUM_BLUR_RADIUS;
	
	private CCGPUSeperateGaussianBlur _myBlur;
	
	public SVisualManager(CCGraphics g, int theWidth, int theHeight){

		_myRenderBuffer = new CCRenderBuffer(g, CCTextureTarget.TEXTURE_RECT,theWidth,theHeight);
		_myRenderBuffer.attachment(0).wrap(CCTextureWrap.REPEAT);
		_myRenderBuffer.attachment(0).textureFilter(CCTextureFilter.LINEAR);
		_myRenderBuffer.beginDraw();
		g.clear();
		_myRenderBuffer.endDraw();
		
		_myTraceRenderBuffer = new CCRenderBuffer(g, CCTextureTarget.TEXTURE_RECT,theWidth,theHeight);
		_myTraceRenderBuffer.attachment(0).wrap(CCTextureWrap.REPEAT);
		_myTraceRenderBuffer.attachment(0).textureFilter(CCTextureFilter.LINEAR);
		_myTraceRenderBuffer.beginDraw();
		g.clear();
		_myTraceRenderBuffer.endDraw();
		
		_myBlur = new CCGPUSeperateGaussianBlur(20, theWidth, theHeight, 1);
		_myBlur.beginDraw(g);
		g.clear();
		_myBlur.endDraw(g);
	}
	
	public void addVisual(SVisual theVisual){
		_myVisuals.add(theVisual);
	}
	
	public int width(){
		return _myRenderBuffer.width();
	}
	
	public int height(){
		return _myRenderBuffer.height();
	}
	
	public CCTexture2D output(){
		return _myRenderBuffer.attachment(0);
	}
	
	public void update(float theDeltaTime){
		_myBlur.radius(_cBlurRadius);
		for(SVisual myVisual:_myVisuals){
			myVisual.update(theDeltaTime);
		}
	}
	
	public void draw(CCGraphics g){
//		g.pushAttribute();
//		_myTraceRenderBuffer.beginDraw();
//		g.clearDepthBuffer();
		
//		g.clearColor(0,0,0,0);
//		g.clear();
//		g.pushMatrix();
//		g.translate(-_myRenderBuffer.width() / 2, - _myRenderBuffer.height()/2);
//		g.color(0,_cTrace);
//		g.rect(0,0, _myRenderBuffer.width(), _myRenderBuffer.height());
//		g.blendMode(CCBlendMode.ADD);
//		for(SVisual myVisual:_myVisuals){
//			g.clearDepthBuffer();
//			myVisual.draw(g);
//		}
//		g.popMatrix();
//		_myTraceRenderBuffer.endDraw();
//
//		g.popAttribute();
//		_myBlur.beginDraw(g);
//		
//		g.clearColor(0,0,0);
//		g.clear();
//		g.pushMatrix();
//		g.translate(-_myRenderBuffer.width() / 2, - _myRenderBuffer.height()/2);
//		g.image(_myTraceRenderBuffer.attachment(0), 0,0);
//		g.popMatrix();
//		_myBlur.endDraw(g);
		
		_myRenderBuffer.beginDraw();
		g.clear();
		g.pushMatrix();
		g.translate(-_myRenderBuffer.width() / 2, - _myRenderBuffer.height()/2);
		g.color(0,_cTrace);
		g.rect(0,0, _myRenderBuffer.width(), _myRenderBuffer.height());
		g.blendMode(CCBlendMode.ADD);
		for(SVisual myVisual:_myVisuals){
			g.clearDepthBuffer();
			myVisual.draw(g);
		}
		g.popMatrix();
		
		_myRenderBuffer.endDraw();
		
		
	}
}
