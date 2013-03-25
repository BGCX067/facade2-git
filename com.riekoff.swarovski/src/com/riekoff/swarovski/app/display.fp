uniform samplerRECT infoTexture1 : TEXUNIT0;
uniform samplerRECT infoTexture2 : TEXUNIT1;

uniform float2 minMax;

void main(
	in float4 iColor : COLOR,
	in float4 iTexCoord : TEXCOORD0,
	in float2 iTexCoord1 : TEXCOORD1,
	in float2 iTexCoord2 : TEXCOORD2,
	out float4 oColor : COLOR0
){
	float4 myValues = texRECT(infoTexture1, iTexCoord1.xy);
	myValues += texRECT(infoTexture2, iTexCoord2.xy);
	//myValues.rgb *= iColor.rgb;
	oColor = lerp(minMax.r, minMax.g, myValues.r * iTexCoord.a);
	oColor.a = 1.0;
	//oColor = float4(myValues.r, myValues.r, myValues.r, 1.0);
	//oColor = float4(iTexCoord1.x / 502 , iTexCoord1.y / 52 ,1, 1);
}