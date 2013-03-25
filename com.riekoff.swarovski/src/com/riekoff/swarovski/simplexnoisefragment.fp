#include "shader/util/simplex.fp"

uniform float2 noiseScale = float2(1,1);
uniform float3 noiseOffset = float3(0,0,0);
	
void main(
	in 		float2 		coords	: WPOS,
	out 	float4 		output0 : COLOR0
) { 
	output0 = pow((snoise(float3(coords.xy * noiseScale,0) * 0.01  + noiseOffset) + 1)/2,1);
}     