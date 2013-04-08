package com.riekoff.swarovski.app;

import com.riekoff.swarovski.visual.SNoiseWavesVisual;
import com.riekoff.swarovski.visual.SQuadVisual;
import com.riekoff.swarovski.visual.SRandomSines;
import com.riekoff.swarovski.visual.SVisualManager;

import cc.creativecomputing.CCApp;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.model.CCContent3dIO;
import cc.creativecomputing.model.CCModel;
import cc.creativecomputing.timeline.CCUITimelineConnector;
import cc.creativecomputing.timeline.view.TimelineContainer;
import cc.creativecomputing.timeline.view.swing.SwingTimelineContainer;

public class SLightAnimation {
	
	private CCUITimelineConnector _myTimelineConnection;
//	private SwingTimelineContainer _myTimeline;
	private TimelineContainer _myTimeline;
	
	public static enum STimeLineMode{
		EDIT, AUTO, OFF
	}

	private CCModel _myModel;
	
	private SVisualManager _myBackgroundVisualManager;
	private SVisualManager _myForegroundVisualManager;
	
	private int ledMapWidth= 504;
    private int ledMapHeight= 63;
    
    private SPixelMapper _myPixelMapper;
    private SModelDrawer _myModelDrawer;
    
    private CCGraphics _myGraphics;

	public SLightAnimation(CCApp theApp, STimeLineMode theMode){
		_myGraphics = theApp.g;
		
		initTimeline(theApp,theMode);
		
		_myModel = CCContent3dIO.createModel("simmodel02.obj");
		for(String myString:_myModel.groupNames()){
			System.out.println(myString);
		}
		_myModel.convert(true);
		
		_myPixelMapper = new SPixelMapper(_myModel);
		_myPixelMapper.addMappings("pixelmaskA5.jpg", 0, 434,0);
		_myPixelMapper.addMappings("pixelmaskB5.jpg", 435, 564,250);
		_myPixelMapper.createMesh();
		
		theApp.addControls("app", "geometry", 0, _myPixelMapper);
		_myBackgroundVisualManager = new SVisualManager(theApp.g, ledMapWidth * 3, ledMapHeight);
		theApp.addControls("background","manager", 0, _myBackgroundVisualManager);
		
		SRandomSines myRandomSines = new SRandomSines(ledMapWidth * 3, ledMapHeight);
		_myBackgroundVisualManager.addVisual(myRandomSines);
		theApp.addControls("background","randomSines", 0, myRandomSines);
		
		_myForegroundVisualManager = new SVisualManager(theApp.g, ledMapWidth * 3, ledMapHeight);
		theApp.addControls("foreground","manager", 0, _myForegroundVisualManager);
		
		SQuadVisual myQuadVisual = new SQuadVisual(ledMapWidth * 3, ledMapHeight, "swan2.png");
		_myForegroundVisualManager.addVisual(myQuadVisual);
		theApp.addControls("foreground","quad", 3, myQuadVisual);
		
		SNoiseWavesVisual myWaveVisual1 = new SNoiseWavesVisual(ledMapWidth * 3, ledMapHeight);
		_myForegroundVisualManager.addVisual(myWaveVisual1);
		theApp.addControls("foreground","noise wave 1", 1, myWaveVisual1);
		
		SNoiseWavesVisual myWaveVisual2 = new SNoiseWavesVisual(ledMapWidth * 3, ledMapHeight);
		_myForegroundVisualManager.addVisual(myWaveVisual2);
		theApp.addControls("foreground","noise wave 2", 1, myWaveVisual2);
		
		_myModelDrawer = new SModelDrawer(_myModel);
	
		_myPixelMapper.background(_myBackgroundVisualManager.output());
		_myPixelMapper.foreground(_myForegroundVisualManager.output());
	}
	
	public SPixelMapper mapper(){
		return _myPixelMapper;
	}
	
	private void initTimeline(CCApp theApp, STimeLineMode theMode){
		
		switch(theMode){
		case OFF:
			return;
		case AUTO:
			_myTimeline = new TimelineContainer();
			_myTimeline.loadFile("130408_pres01.xml");
			_myTimeline.play();
			break;
		case EDIT:
			_myTimeline = new SwingTimelineContainer();
			_myTimeline.minZoomRange(5);
			_myTimeline.maxZoomRange(120);
			_myTimeline.setSize(1900, 500);
			break;
		}
//		
		_myTimelineConnection = new CCUITimelineConnector(theApp, _myTimeline);
	}
	
	public void update(final float theDeltaTime) {
		if(_myTimeline != null)_myTimeline.update(theDeltaTime);
		_myBackgroundVisualManager.update(theDeltaTime);
		_myForegroundVisualManager.update(theDeltaTime);
		
		_myBackgroundVisualManager.draw(_myGraphics);
		_myForegroundVisualManager.draw(_myGraphics);
	}
	
	public void drawSimulation(CCGraphics g){
		g.color(255);
		_myPixelMapper.draw(g);
		g.color(30);
//		_myModelDrawer.draw(g);
	}
	
	public void drawOutput(CCGraphics g){
		_myPixelMapper.drawOutput(g);
	}
	
	public void drawLayers(CCGraphics g){
		g.image(_myBackgroundVisualManager.output(), 0, ledMapHeight * 2);
		g.image(_myForegroundVisualManager.output(), 0, ledMapHeight * 1);
	}
}
