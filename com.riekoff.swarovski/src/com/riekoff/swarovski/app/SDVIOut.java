package com.riekoff.swarovski.app;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.control.CCControl;

public class SDVIOut extends CCApp{
	
	@CCControl(name = "x Offset", min = -1000, max = 1000)
	private int _cXOffset = 0;
	@CCControl(name = "y Offset", min = -1000, max = 1000)
	private int _cYOffset = 0;
	
	private SPixelMapper _myMapper;

	public SDVIOut(){
		
	}
	
	public void mapper(SPixelMapper theGeometry){
		_myMapper = theGeometry;
	}
	
	public void draw(){
		if(_myMapper == null)return;
		
		g.clear();
		g.pushMatrix();
		g.translate(-width/2 + _cXOffset, height/2 - _cYOffset);
		_myMapper.drawOutput(g);
		g.popMatrix();
	}
}
