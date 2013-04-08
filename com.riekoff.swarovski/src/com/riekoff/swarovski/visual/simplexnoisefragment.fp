#include "shader/util/simplex.fp"

uniform float2 noiseScale = float2(1,1);
uniform float3 noiseOffset = float3(0,0,0);
uniform float noisePow = 0;
uniform float alpha = 0;
uniform float2 minMax;
	
void main(
	in 		float2 		coords	: WPOS,
	out 	float4 		output0 : COLOR0
) { 
	output0 = pow((snoise(float3(coords.xy * noiseScale,0) * 0.01  + noiseOffset) + 1)/2,noisePow);
	output0 = lerp(minMax.x, minMax.y, output0);
	output0.a = alpha;
}     