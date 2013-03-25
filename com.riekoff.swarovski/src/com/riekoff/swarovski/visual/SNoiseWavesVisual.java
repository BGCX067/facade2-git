package com.riekoff.swarovski.visual;

import com.jogamp.opengl.cg.CGparameter;

import cc.creativecomputing.control.CCControl;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.shader.CCCGShader;
import cc.creativecomputing.graphics.shader.util.CCGPUNoise;
import cc.creativecomputing.io.CCIOUtil;
import cc.creativecomputing.math.CCVector3f;

public class SNoiseWavesVisual extends SVisual {

	@CCControl(name = "noiseScaleX", min = 0, max = 100, external = true)
	private float _cNoiseScaleX = 1;
	@CCControl(name = "noiseScaleY", min = 0, max = 100, external = true)
	private float _cNoiseScaleY = 1;
	@CCControl(name = "speed x", min = 0, max = 1, external = true)
	private float _cNoiseSpeedX = 1;
	@CCControl(name = "speed y", min = 0, max = 1, external = true)
	private float _cNoiseSpeedY = 1;
	@CCControl(name = "speed z", min = 0, max = 1, external = true)
	private float _cNoiseSpeedZ = 1;
	@CCControl(name = "pow", min = 0, max = 10, external = true)
	private float _cNoisePow = 1;
	@CCControl(name = "alpha", min = 0, max = 1, external = true)
	private float _cAlpha = 1;

	private CCCGShader _myNoiseShader;
	private CGparameter _myNoiseScaleParameter;
	private CGparameter _myNoiseOffsetParameter;
	private CGparameter _myNoisePowParameter;
	private CGparameter _myNoiseAlphaParameter;

	private CCVector3f _myOffset;

	public SNoiseWavesVisual(int theContentWidth, int theContentHeight) {
		super(theContentWidth, theContentHeight);

		_myNoiseShader = new CCCGShader(null, CCIOUtil.classPath(this, "simplexnoisefragment.fp"));
		_myNoiseScaleParameter = _myNoiseShader.fragmentParameter("noiseScale");
		_myNoiseOffsetParameter = _myNoiseShader.fragmentParameter("noiseOffset");
		_myNoisePowParameter = _myNoiseShader.fragmentParameter("noisePow");
		_myNoiseAlphaParameter = _myNoiseShader.fragmentParameter("alpha");
		_myNoiseShader.load();

		CCGPUNoise.attachFragmentNoise(_myNoiseShader);

		_myOffset = new CCVector3f();
	}

	@Override
	public void update(float theDeltaTime) {
		_myOffset.x += theDeltaTime * _cNoiseSpeedX;
		_myOffset.y += theDeltaTime * _cNoiseSpeedY;
		_myOffset.z += theDeltaTime * _cNoiseSpeedZ;
	}

	@Override
	public void draw(CCGraphics g) {
		_myNoiseShader.start();
		_myNoiseShader.parameter(_myNoiseScaleParameter, _cNoiseScaleX, _cNoiseScaleY);
		_myNoiseShader.parameter(_myNoiseOffsetParameter, _myOffset);
		_myNoiseShader.parameter(_myNoisePowParameter, _cNoisePow);
		_myNoiseShader.parameter(_myNoiseAlphaParameter, _cAlpha);
		g.rect(0, 0, _myContentWidth, _myContentHeight);
		_myNoiseShader.end();
	}

}
