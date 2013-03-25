package com.riekoff.swarovski.app;

import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.shader.CCCGShader;
import cc.creativecomputing.graphics.texture.CCTexture2D;
import cc.creativecomputing.graphics.texture.CCTextureIO;
import cc.creativecomputing.io.CCIOUtil;
import cc.creativecomputing.model.CCModel;

public class SModelDrawer {

	private CCModel _myModel;
	
	private CCTexture2D _myWallATex;
	private CCTexture2D _myWallBTex;
	
	private CCCGShader _myModelShader;
	
	
	public SModelDrawer(CCModel theModel){
		_myModel = theModel;
		
		_myModelShader = new CCCGShader(
			CCIOUtil.classPath(this, "model_display.vp"), 
			CCIOUtil.classPath(this, "model_display.fp")
		);
		_myModelShader.load();
		
		_myWallATex = new CCTexture2D(CCTextureIO.newTextureData("130223_wallmaskA2_highres.jpg"));
		_myWallBTex = new CCTexture2D(CCTextureIO.newTextureData("130223_wallmaskB2_highres.jpg"));
	}
	
	public void draw(CCGraphics g){
		_myModelShader.start();
		g.texture(_myWallBTex);
		_myModel.group("wallb_behind").draw(g);
		g.noTexture();
		g.texture(_myWallATex);
		_myModel.group("walla_behind").draw(g);
		g.noTexture();
		_myModelShader.end();
	}
}
