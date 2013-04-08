package com.riekoff.swarovski.app;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.CCAppContextShared;
import cc.creativecomputing.CCApplicationManager;
import cc.creativecomputing.CCApplicationSettings.CCCloseOperation;
import cc.creativecomputing.events.CCKeyEvent;
import cc.creativecomputing.graphics.CCColor;
import cc.creativecomputing.graphics.export.CCScreenCapture;
import cc.creativecomputing.math.util.CCArcball;
import cc.creativecomputing.model.CCContent3dIO;
import cc.creativecomputing.model.CCModel;
import cc.creativecomputing.timeline.CCUITimelineConnector;
import cc.creativecomputing.timeline.view.TimelineContainer;
import cc.creativecomputing.timeline.view.swing.SwingTimelineContainer;

import com.riekoff.swarovski.SOutputWindow;
import com.riekoff.swarovski.visual.SNoiseCurveVisual;
import com.riekoff.swarovski.visual.SNoiseWavesVisual;
import com.riekoff.swarovski.visual.SQuadVisual;
import com.riekoff.swarovski.visual.SRandomSines;
import com.riekoff.swarovski.visual.STextureVisual;
import com.riekoff.swarovski.visual.SVisualManager;


public class CCOBJDemo extends CCApp {
	
	private CCModel _myModel;
	private CCArcball _myArcball;
    
	private SVisualManager _myBackgroundVisualManager;
	private SVisualManager _myForegroundVisualManager;
    
    private int ledMapWidth= 504;
    private int ledMapHeight= 63;
    
    SPixelMapper _myPixelMapper;
    SModelDrawer _myModelDrawer;

	private SDVIOut _myOutputWindow;
	
	private CCUITimelineConnector _myTimelineConnection;
//	private SwingTimelineContainer _myTimeline;
	private TimelineContainer _myTimeline;
    
    
	@Override
	public void setup() {
		
//		_myTimeline = new TimelineContainer();
//		_myTimeline.minZoomRange(5);
//		_myTimeline.maxZoomRange(120);
//		_myTimelineConnection = new CCUITimelineConnector(this, _myTimeline);
//		_myTimeline.setSize(1900, 500);
//		_myTimeline.loadFile("pres03.xml");
//		_myTimeline.play();
		
		_myModel = CCContent3dIO.createModel("simmodel02.obj");
		for(String myString:_myModel.groupNames()){
			System.out.println(myString);
		}
		_myModel.convert(true);
	
		_myPixelMapper = new SPixelMapper(_myModel);
		_myPixelMapper.addMappings("pixelmaskA5.jpg", 0, 434,0);
		_myPixelMapper.addMappings("pixelmaskB5.jpg", 435, 564,250);
		_myPixelMapper.createMesh();
		
		addControls("app", "geometry", 0, _myPixelMapper);
		_myBackgroundVisualManager = new SVisualManager(g, ledMapWidth * 3, ledMapHeight);
		addControls("background","manager", 0, _myBackgroundVisualManager);
		
		SNoiseCurveVisual myVisual = new SNoiseCurveVisual(ledMapWidth * 3, ledMapHeight);
//		_myBackgroundVisualManager.addVisual(myVisual);
		addControls("background","noise curve", 0, myVisual);
		
		SNoiseWavesVisual myWaveVisual = new SNoiseWavesVisual(ledMapWidth * 3, ledMapHeight);
		_myBackgroundVisualManager.addVisual(myWaveVisual);
		addControls("background","noise wave", 1, myWaveVisual);
		
		SRandomSines myRandomSines = new SRandomSines(ledMapWidth * 3, ledMapHeight);
		_myBackgroundVisualManager.addVisual(myRandomSines);
		addControls("background","randomSines", 2, myRandomSines);
		
		_myForegroundVisualManager = new SVisualManager(g, ledMapWidth * 3, ledMapHeight);
		addControls("foreground","manager", 0, _myForegroundVisualManager);
		
		STextureVisual myTypoVisual = new STextureVisual(ledMapWidth * 3, ledMapHeight, "typo.png");
		_myForegroundVisualManager.addVisual(myTypoVisual);
		addControls("foreground","typo", 2, myTypoVisual);
		
		STextureVisual mySwanVisual = new STextureVisual(ledMapWidth * 3, ledMapHeight, "swan2.png");
		_myForegroundVisualManager.addVisual(mySwanVisual);
		addControls("foreground","swan", 2, mySwanVisual);
		
		SQuadVisual myQuadVisual = new SQuadVisual(ledMapWidth * 3, ledMapHeight, "swan2.png");
		_myForegroundVisualManager.addVisual(myQuadVisual);
		addControls("foreground","quad", 3, myQuadVisual);
		
		SNoiseWavesVisual myWaveVisual2 = new SNoiseWavesVisual(ledMapWidth * 3, ledMapHeight);
		_myForegroundVisualManager.addVisual(myWaveVisual2);
		addControls("foreground","noise wave", 1, myWaveVisual2);
		
		
		_myModelDrawer = new SModelDrawer(_myModel);
		
		_myArcball = new CCArcball(this);
		
		_myPixelMapper.background(_myBackgroundVisualManager.output());
		_myPixelMapper.foreground(_myForegroundVisualManager.output());
		
		CCApplicationManager myManager = new CCApplicationManager(SDVIOut.class);
		myManager.settings().size(1024, 768);
		myManager.settings().closeOperation(CCCloseOperation.HIDE_ON_CLOSE);
		myManager.settings().location(0,0);
		myManager.settings().display(0);
		myManager.settings().undecorated(true);
		myManager.settings().appContext(sharedContext);
		myManager.settings().background(CCColor.BLACK);
//		myManager.start();
		_myOutputWindow = (SDVIOut)myManager.app();
//		_myOutputWindow.mapper(_myPixelMapper);
//		addControls("output", "output", _myOutputWindow);
	}
	
	long time = System.currentTimeMillis();
	

	@Override
	public void update(final float theDeltaTime) {
//		_myTimeline.update(theDeltaTime);
		_myBackgroundVisualManager.update(theDeltaTime);
		_myForegroundVisualManager.update(theDeltaTime);
		
		System.out.println(frameRate);
	}

	public void draw() {
		_myBackgroundVisualManager.draw(g);
		_myForegroundVisualManager.draw(g);
		g.clear();
		g.pushMatrix();
		_myArcball.draw(g);
		g.color(255);
		_myPixelMapper.draw(g);
		g.color(30);
		_myModelDrawer.draw(g);
		g.popMatrix();
		g.clearDepthBuffer();
		
		g.color(255);
		g.pushMatrix();
		g.translate(-width/2,-height/2);
		_myPixelMapper.drawOutput(g);
		g.image(_myBackgroundVisualManager.output(), 0, ledMapHeight * 2);
		g.image(_myForegroundVisualManager.output(), 0, ledMapHeight * 1);
		g.popMatrix();
		
		g.strokeWeight(1);
	}
	
	@Override
	public void keyPressed(CCKeyEvent theKeyEvent) {
		switch(theKeyEvent.keyCode()){
		case CCKeyEvent.VK_C:
			CCScreenCapture.capture("export3/frame"+frameCount+".png", width, height);
			break;
		}
	}

	boolean bTexture = true;
	boolean bStroke = true;
	
	private static CCAppContextShared sharedContext = new CCAppContextShared();

	public static void main(String[] args) {
		final CCApplicationManager myManager = new CCApplicationManager(CCOBJDemo.class);
		myManager.settings().size(1500, 800);
		myManager.settings().antialiasing(8);
		myManager.settings().vsync(false);
		myManager.settings().appContext(sharedContext);
		myManager.start();
	}
}
