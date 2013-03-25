uniform sampler2D alphaMask : TEXUNIT0;

void main(
	in float4 iColor : COLOR,
	in float4 iTexCoord : TEXCOORD0,
	out float4 oColor : COLOR0
){
	oColor = iColor;
	oColor.a = tex2D(alphaMask, float2(iTexCoord.x, 1 - iTexCoord.y)).x;
}