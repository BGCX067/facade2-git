package com.riekoff.swarovski;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCGraphics;

public class SOutputWindow extends CCApp{
	
	@CCControl(name = "x Offset", min = -1000, max = 1000)
	private int _cXOffset = 0;
	@CCControl(name = "y Offset", min = -1000, max = 1000)
	private int _cYOffset = 0;
	
	private SGeometry _myGeometry;

	public SOutputWindow(){
		
	}
	
	public void geometry(SGeometry theGeometry){
		_myGeometry = theGeometry;
	}
	
	public void draw(){
		if(_myGeometry == null)return;
		
		g.clear();
		g.pushMatrix();
		g.translate(-width/2 + _cXOffset, height/2 - _cYOffset);
		_myGeometry.drawOutput(g);
		g.popMatrix();
	}
}
