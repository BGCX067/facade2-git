uniform samplerRECT randomIn : TEXUNIT0;
uniform float time;
uniform float freqPow;
uniform float PI;
uniform float darkTime;

uniform float minBright;
uniform float maxBright;
uniform float brightPow;
	
void main(
	in float2 coords : TEXCOORD0,
	out float4 output0 : COLOR0
) { 
	float4 myRandoms = texRECT(randomIn,coords);
	float myFreq = pow(myRandoms.x, freqPow);
	float myVal = myFreq * time + myRandoms.y * (PI * 2 + darkTime);
	myVal = fmod(myVal, PI * 2 + darkTime);
	if(myVal > darkTime){
		myVal -= darkTime;
		float mySine = (sin(myVal - PI / 2) + 1) / 2;
		float localMaxBright = maxBright * pow(myRandoms.y, brightPow);
		mySine = lerp(minBright, localMaxBright, mySine);
		output0 = float4(mySine,mySine,mySine,1.0);
	} else {
		output0 = float4(minBright,minBright,minBright,1.0);
	}
	
}     