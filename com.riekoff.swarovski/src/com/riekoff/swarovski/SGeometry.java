package com.riekoff.swarovski;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCDrawMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.CCVBOMesh;
import cc.creativecomputing.graphics.shader.CCCGShader;
import cc.creativecomputing.graphics.texture.CCTexture2D;
import cc.creativecomputing.graphics.texture.CCTextureIO;
import cc.creativecomputing.io.CCIOUtil;
import cc.creativecomputing.math.CCMath;

import com.jogamp.opengl.cg.CGparameter;

public class SGeometry {
	
	@CCControl(name = "random pos min", min = 0, max = 1)
	private float _cRandomPosMin = 1;
	@CCControl(name = "random pos max", min = 0, max = 1)
	private float _cRandomPosMax = 1;
	@CCControl(name = "random prob", min = 0, max = 1)
	private float _cRandomProb = 1;
	@CCControl(name = "random pow", min = 0, max = 10)
	private float _cRandomPow = 1;
	@CCControl(name = "min bright", min = 0, max = 1)
	private float _cMinBright = 0;
	@CCControl(name = "max bright", min = 0, max = 10)
	private float _cMaxBright = 0;
	
	private CCVBOMesh _myVBO;
	private CCVBOMesh _myBackVBO;
	
	private CCVBOMesh _myOutputGrid;
	
	private CCCGShader _myShader;
	private CGparameter _myRandomMinParameter;
	private CGparameter _myRandomMaxParameter;
	private CGparameter _myRandomPowParameter;
	private CGparameter _myRandomProbParameter;
	private CGparameter _myMinMaxParameter;
	
	private CCTexture2D _myInput;
	private CCTexture2D _mySpotTexture;
	
	private float _myRadius;
	
	public SGeometry(float theRadius, int theWidth, int theight){
		_myRadius = theRadius;
		_myVBO = new CCVBOMesh(CCDrawMode.POINTS, theWidth * theight);
		_myBackVBO = new CCVBOMesh(CCDrawMode.TRIANGLE_STRIP, theWidth * 2);
		
		_myOutputGrid = new CCVBOMesh(CCDrawMode.POINTS, theWidth * theight);
		
		float mySpace = CCMath.TWO_PI * theRadius / theWidth;
		for(int i = 0; i < theWidth;i++){
			float myAngle = CCMath.map(i, 0, theWidth, 0, CCMath.TWO_PI);
			float myX = CCMath.sin(myAngle);
			float myZ = CCMath.cos(myAngle);
			
			
			myX *= theRadius;
			myZ *= theRadius;
			
			int myOutputY = 0;
			
			for(int y = 0; y < theight;y++){
				float myY = y * mySpace - theight * mySpace / 2;
				if(i%2 == 1)myY += mySpace / 2;
				
				float myRand0 = CCMath.gaussianRandom();
				float myRand1 = CCMath.gaussianRandom();
				float myRand2 = CCMath.random();
				
				boolean myIsLight = i % 8 == 0 && y % 4 == 0 || i % 8 == 4 && y % 4 == 2;
				
				if(myIsLight){
					int myGridY = myOutputY * 2;
					if(i % 8 == 4)myGridY += 1;
					_myOutputGrid.addVertex(i / 4, myGridY);
					_myOutputGrid.addTextureCoords(0, myRand0, myRand1, myRand2, 1);
					_myOutputGrid.addTextureCoords(1, i, y);
					myOutputY++;
				}
				
					
					

					_myVBO.addVertex(myX, myY, myZ);
					_myVBO.addTextureCoords(1, i,y);
					
//					if(i % 4 == 0 && y % 2 == 0 || i % 4 == 2 && y % 2 == 1){
					if(myIsLight){
						_myVBO.addTextureCoords(0, myRand0, myRand1, myRand2, 1);
					}else{
						_myVBO.addTextureCoords(0, myRand0, myRand1, myRand2, 0);
					}
					
			}
			
			 myX = CCMath.sin(myAngle) * (theRadius - 2);
			 myZ = CCMath.cos(myAngle) * (theRadius - 2);
			_myBackVBO.addVertex(myX, -theight * mySpace / 2, myZ);
			_myBackVBO.addVertex(myX,  theight * mySpace / 2, myZ);
			
			_mySpotTexture = new CCTexture2D(CCTextureIO.newTextureData("sport.png"));
			_mySpotTexture.anisotropicFiltering(1f);
		}
		
		_myShader = new CCCGShader(
			CCIOUtil.classPath(this, "display.vp"),
			CCIOUtil.classPath(this, "display.fp")
		);
		_myRandomMinParameter  = _myShader.vertexParameter("randomPosMin");
		_myRandomMaxParameter  = _myShader.vertexParameter("randomPosMax");
		_myRandomPowParameter  = _myShader.vertexParameter("randomPow");
		_myRandomProbParameter  = _myShader.vertexParameter("randomProb");
		_myMinMaxParameter  = _myShader.fragmentParameter("minMax");
		_myShader.load();
	}
	
	public void input(CCTexture2D theInput){
		_myInput = theInput;
	}
	
	public void draw(CCGraphics g){
		if(_myInput == null)return;
		g.pushMatrix();
		g.translate(0,0,-_myRadius);
		g.color(55);
		_myBackVBO.draw(g);

		g.color(255);
		_myShader.start();
		_myShader.parameter(_myRandomMinParameter, _cRandomPosMin);
		_myShader.parameter(_myRandomMaxParameter, _cRandomPosMax);
		_myShader.parameter(_myRandomPowParameter, _cRandomPow);
		_myShader.parameter(_myRandomProbParameter, _cRandomProb);
		_myShader.parameter(_myMinMaxParameter, _cMinBright, _cMaxBright);
		g.texture(0,_myInput);
		_myVBO.draw(g);
//		_myOutputGrid.draw(g);
		g.noTexture();
		_myShader.end();
		g.popMatrix();
	}
	
	public void drawOutput(CCGraphics g){
		g.pushMatrix();
		g.translate(0,0);

		g.color(255);
		_myShader.start();
		_myShader.parameter(_myRandomMinParameter, _cRandomPosMin);
		_myShader.parameter(_myRandomMaxParameter, _cRandomPosMax);
		_myShader.parameter(_myRandomPowParameter, _cRandomPow);
		_myShader.parameter(_myRandomProbParameter, _cRandomProb);
		_myShader.parameter(_myMinMaxParameter, _cMinBright, _cMaxBright);
		g.texture(0,_myInput);
		g.texture(2,_mySpotTexture);
		_myOutputGrid.draw(g);
		g.noTexture();
		_myShader.end();
		g.popMatrix();
	}
}
