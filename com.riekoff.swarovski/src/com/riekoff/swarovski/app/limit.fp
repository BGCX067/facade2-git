uniform samplerRECT inputTexture : TEXUNIT0;
uniform samplerRECT sourceTexture : TEXUNIT1;

uniform float rise;
uniform float fall;

void main(
	in float2 iTexCoord : TEXCOORD0,
	out float4 oColor : COLOR0
){
	float4 myInput = texRECT(inputTexture, iTexCoord);
	float4 mySource = texRECT(sourceTexture, iTexCoord);
	
	float4 myAdd = myInput - mySource;
	if(myInput.r > mySource.r){
		oColor = mySource + rise;
	} else {
		oColor = mySource - fall;
	}
	
	//oColor = myInput * rise + mySource * (1 - rise);
	oColor.a = 1.0;
}