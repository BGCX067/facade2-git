uniform samplerRECT infoTexture : TEXUNIT0;

uniform float2 minMax;

void main(
	in float4 iColor : COLOR,
	in float4 iTexCoord : TEXCOORD0,
	in float2 iTexCoord1 : TEXCOORD1,
	in float2 iTexCoord2 : TEXCOORD2,
	out float4 oColor : COLOR0
){
	float4 myValues = texRECT(infoTexture, iTexCoord1);
	myValues.rgb *= iColor.rgb;
	oColor = float4(lerp(minMax.r, minMax.g, myValues.r));
	oColor.a = 1.0;
	//oColor = float4(1,1,1, 1);
}