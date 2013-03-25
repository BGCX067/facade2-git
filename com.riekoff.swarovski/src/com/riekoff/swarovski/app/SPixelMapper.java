package com.riekoff.swarovski.app;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.cg.CGparameter;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCColor;
import cc.creativecomputing.graphics.CCDrawMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.CCVBOMesh;
import cc.creativecomputing.graphics.shader.CCCGShader;
import cc.creativecomputing.graphics.texture.CCTexture2D;
import cc.creativecomputing.graphics.texture.CCTextureData;
import cc.creativecomputing.graphics.texture.CCTextureIO;
import cc.creativecomputing.io.CCIOUtil;
import cc.creativecomputing.math.CCMath;
import cc.creativecomputing.math.CCVector2f;
import cc.creativecomputing.math.CCVector3f;
import cc.creativecomputing.model.CCModel;


public class SPixelMapper{
	@CCControl(name = "random pos min back", min = 0, max = 1, external = true)
	private float _cRandomPosMinBack = 1;
	@CCControl(name = "random pos max back", min = 0, max = 1, external = true)
	private float _cRandomPosMaxBack = 1;
	@CCControl(name = "random prob back", min = 0, max = 1)
	private float _cRandomProbBack = 1;
	@CCControl(name = "random pow back", min = 0, max = 10, external = true)
	private float _cRandomPowBack = 1;
	
	@CCControl(name = "random pos min fore", min = 0, max = 1, external = true)
	private float _cRandomPosMinFore = 1;
	@CCControl(name = "random pos max fore", min = 0, max = 1, external = true)
	private float _cRandomPosMaxFore = 1;
	@CCControl(name = "random prob fore", min = 0, max = 1)
	private float _cRandomProbFore = 1;
	@CCControl(name = "random pow fore", min = 0, max = 10, external = true)
	private float _cRandomPowFore = 1;
	
	@CCControl(name = "min bright", min = 0, max = 1)
	private float _cMinBright = 0;
	@CCControl(name = "max bright", min = 0, max = 10)
	private float _cMaxBright = 0;
	@CCControl(name = "pointsize", min = 1, max = 10)
	private float _cPointSize = 0;
	
	static class SPixelMapping{
    	float _myTexX;
    	float _myX;
    	float _myZ;
    	
    	private SPixelMapping(float theTexX, float theX, float theZ){
    		_myTexX = theTexX;
    		_myX = theX;
    		_myZ = theZ;
    	}
    }
	
	private CCCGShader _myShader;
	private CGparameter _myRandomMinBackParameter;
	private CGparameter _myRandomMaxBackParameter;
	private CGparameter _myRandomPowBackParameter;
	private CGparameter _myRandomProbBackParameter;
	
	private CGparameter _myRandomMinForeParameter;
	private CGparameter _myRandomMaxForeParameter;
	private CGparameter _myRandomPowForeParameter;
	private CGparameter _myRandomProbForeParameter;
	
	private CGparameter _myMinMaxParameter;

	private CCTexture2D _myInputBack;
	private CCTexture2D _myInputForeGround;
	
	private List<CCVector3f> _myPoints = new ArrayList<>();
	private List<CCVector2f> _myGridCoords = new ArrayList<>();
	private List<CCVector2f> _myTexCoords = new ArrayList<>();
	
	private CCModel _myModel;
	private CCVBOMesh _myVBO;
	private CCVBOMesh _myOutputGrid;
	
	private int _myMappingX;
	
	public SPixelMapper(CCModel theModel){
		_myModel = theModel;
		_myMappingX = 0;
		
		_myShader = new CCCGShader(
			CCIOUtil.classPath(this, "display.vp"),
			CCIOUtil.classPath(this, "display.fp")
		);
		_myRandomMinBackParameter  = _myShader.vertexParameter("randomPosMin1");
		_myRandomMaxBackParameter  = _myShader.vertexParameter("randomPosMax1");
		_myRandomPowBackParameter  = _myShader.vertexParameter("randomPow1");
		_myRandomProbBackParameter  = _myShader.vertexParameter("randomProb1");

		_myRandomMinForeParameter  = _myShader.vertexParameter("randomPosMin2");
		_myRandomMaxForeParameter  = _myShader.vertexParameter("randomPosMax2");
		_myRandomPowForeParameter  = _myShader.vertexParameter("randomPow2");
		_myRandomProbForeParameter  = _myShader.vertexParameter("randomProb2");
		
		_myMinMaxParameter  = _myShader.fragmentParameter("minMax");
		_myShader.load();
	}
	
	public void background(CCTexture2D theInput){
		_myInputBack = theInput;
	}
	
	public void foreground(CCTexture2D theInput){
		_myInputForeGround = theInput;
	}
	
	public void addMappings(String theMask1, int theStart, int theEnd, int theTexMove){
    	List<SPixelMapping> myPixelMappings = new ArrayList<SPixelMapping>();
		for(int i = theStart; i < theEnd;i+=2){
			myPixelMappings.add(new SPixelMapping(_myModel.textureCoords().get(i).x, _myModel.vertices().get(i).x, _myModel.vertices().get(i).z));
		}

		CCTextureData _myMaskData = CCTextureIO.newTextureData(theMask1);
		for(int x = 0; x < _myMaskData.width();x++){
			for(int y = 0; y < _myMaskData.height();y++){
				CCColor myColor = _myMaskData.getPixel(x, y);
				if(myColor.r < 0.1f)continue;
				
				float myPosY = CCMath.map(y, 0, _myMaskData.height() - 1, 0, 70);
		    		
				float myX = x;
				float myTexX = x;
				if(y % 2 == 1){
					myX += 0.5f;
					myTexX += 0.5f;
				}
				
				myX /= (float)(_myMaskData.width() - 1);
				
				for(int i = 0; i < myPixelMappings.size();i++){
					SPixelMapping myMapping = myPixelMappings.get(i);
					
					if(myMapping._myTexX <= myX)continue;
		    			
					SPixelMapping myPrevMapping = myPixelMappings.get(i - 1);
					float myPosX = CCMath.map(myX, myPrevMapping._myTexX, myMapping._myTexX, myPrevMapping._myX, myMapping._myX);
					float myPosZ = CCMath.map(myX, myPrevMapping._myTexX, myMapping._myTexX, myPrevMapping._myZ, myMapping._myZ);
					_myPoints.add(new CCVector3f(myPosX, myPosY, myPosZ));
					_myTexCoords.add(new CCVector2f((_myMappingX + myTexX - theTexMove) * 3,y));
					
					int myOutX = (_myMappingX + x) * 2;
					int myOutY = y;
					if(y % 2 == 1){
						myOutX += 1;
					}
					_myGridCoords.add(new CCVector2f(myOutX,myOutY));
					break;
		    	}
    		}
		}
		_myMappingX += _myMaskData.width();
	}
	
	public void createMesh(){
		FloatBuffer myVertexBuffer = FloatBuffer.allocate( _myPoints.size() * 3);
		FloatBuffer myTex0Buffer = FloatBuffer.allocate( _myPoints.size() * 4);
		FloatBuffer myTex1Buffer = FloatBuffer.allocate( _myPoints.size() * 2);
		FloatBuffer myGridBuffer = FloatBuffer.allocate( _myPoints.size() * 3);
		
		for(CCVector3f myPoint:_myPoints){
			myVertexBuffer.put(myPoint.x);
			myVertexBuffer.put(myPoint.y);
			myVertexBuffer.put(myPoint.z);
			
			myTex0Buffer.put(CCMath.gaussianRandom());
			myTex0Buffer.put(CCMath.gaussianRandom());
			myTex0Buffer.put(CCMath.random());
			myTex0Buffer.put(1);
		}
		for(CCVector2f myTexCoords:_myTexCoords){
			myTex1Buffer.put(myTexCoords.x);
			myTex1Buffer.put(myTexCoords.y);
		}
		
		for(CCVector2f myTexCoords:_myGridCoords){
			myGridBuffer.put(myTexCoords.x);
			myGridBuffer.put(myTexCoords.y);
			myGridBuffer.put(0);
		}

		_myVBO = new CCVBOMesh(CCDrawMode.POINTS, _myPoints.size());
		_myVBO.vertices(myVertexBuffer);
		_myVBO.textureCoords(0, myTex0Buffer, 4);
		_myVBO.textureCoords(1, myTex1Buffer);
		
		_myOutputGrid = new CCVBOMesh(CCDrawMode.POINTS, _myPoints.size());
		_myOutputGrid.vertices(myGridBuffer);
		_myOutputGrid.textureCoords(0, myTex0Buffer, 4);
		_myOutputGrid.textureCoords(1, myTex1Buffer);
	}
	
	public void update(final float theDeltaTime){
		
	}
	
	public void draw(CCGraphics g){
		if(_myInputBack == null)return;
		g.pushMatrix();
//		g.translate(0,0,-_myRadius);

		g.pointSize(_cPointSize);
		g.color(255);
		_myShader.start();
		_myShader.parameter(_myRandomMinBackParameter, _cRandomPosMinBack);
		_myShader.parameter(_myRandomMaxBackParameter, _cRandomPosMaxBack);
		_myShader.parameter(_myRandomPowBackParameter, _cRandomPowBack);
		_myShader.parameter(_myRandomProbBackParameter, _cRandomProbBack);
		
		_myShader.parameter(_myRandomMinForeParameter, _cRandomPosMinFore);
		_myShader.parameter(_myRandomMaxForeParameter, _cRandomPosMaxFore);
		_myShader.parameter(_myRandomPowForeParameter, _cRandomPowFore);
		_myShader.parameter(_myRandomProbForeParameter, _cRandomProbFore);
		
		_myShader.parameter(_myMinMaxParameter, _cMinBright, _cMaxBright);
		g.texture(0,_myInputBack);
		g.texture(1,_myInputForeGround);
		_myVBO.draw(g);
		g.noTexture();
		_myShader.end();
		g.popMatrix();
	}
	
	public void drawOutput(CCGraphics g){
		if(_myInputBack == null)return;
		g.pushMatrix();
//		g.translate(0,0,-_myRadius);

		g.pointSize(1);
		g.color(255);
		_myShader.start();
		_myShader.parameter(_myRandomMinBackParameter, _cRandomPosMinBack);
		_myShader.parameter(_myRandomMaxBackParameter, _cRandomPosMaxBack);
		_myShader.parameter(_myRandomPowBackParameter, _cRandomPowBack);
		_myShader.parameter(_myRandomProbBackParameter, _cRandomProbBack);
		
		_myShader.parameter(_myRandomMinForeParameter, _cRandomPosMinFore);
		_myShader.parameter(_myRandomMaxForeParameter, _cRandomPosMaxFore);
		_myShader.parameter(_myRandomPowForeParameter, _cRandomPowFore);
		_myShader.parameter(_myRandomProbForeParameter, _cRandomProbFore);
		
		_myShader.parameter(_myMinMaxParameter, _cMinBright, _cMaxBright);
		g.texture(0,_myInputBack);
		g.texture(1,_myInputForeGround);
		_myOutputGrid.draw(g);
		g.noTexture();
		_myShader.end();
		g.popMatrix();
	}
}