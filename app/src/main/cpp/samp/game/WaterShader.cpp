#include "../main.h"
#include "game.h"
#include "../net/netgame.h"
#include "WaterShader.h"
#include "obfusheader.h"
#include <cmath>
#include <GLES2/gl2.h>

extern CGame *pGame;
extern CNetGame *pNetGame;

int shaderID = 0;
uintptr_t *shader;
int UniformLocationWaveness = -1;
float waterwave = 0.0;
int gotov = 0;
void setUnWav() {
	if(gotov == 1){
    if (shader[4] != -1) {
      glUniform1f(shader[4], waterwave); // waterwave - ваша глобальная переменная
    }
	}
  }
void updateWater() {
    waterwave += 0.03f; // Увеличиваем значение Waveness
    setUnWav(); // Обновляем шейдер 
  }

uintptr_t* BuildShaderSource(uintptr_t* a1, const char *a2, const char *a3, int a4){
    char *v7; // r6    
char *v8; // r0
    *(uintptr_t *)(a1 + 17) = 0;
    *(uintptr_t *)(a1 + 5) = a4; 
	*(uintptr_t *)(a1 + 9) = ((int (__fastcall *)(const char *))(g_libGTASA + 0x1BC269))(a2);
    *(uintptr_t *)(a1 + 13) = ((int (__fastcall *)(const char *))(g_libGTASA + 0x1BC269))(a3);  
	v7 = strdup(a2);
    v8 = strdup(a3);  
    *(char *)(a1 + 4) = 1;
    *(uintptr_t* *)a1 = ((uintptr_t *(__fastcall *)(char *, char *, uintptr_t))(g_libGTASA + 0x1A3A69))(v7, v8, *(uintptr_t *)(a1 + 5));  
	return a1;
}
int  WaterShader::EmuShader__Select3(uintptr_t* a1)
{
    return ((int ( *)(uintptr_t*, int))(g_libGTASA + 0x1984A5))(a1, 1);
}

int WaterShader::BuildShadersSource1(uintptr_t* a1)
{
    uintptr_t *v2; // r5
    int result; // r0
	
    v2 = a1;
	
	
	
    v2 = BuildShaderSource(
            (uintptr_t*)v2,
            OBF("#version 100\n"
    "precision highp float;\n"
    "uniform sampler2D EnvMap;\n"
    "uniform sampler2D DuDvMap;\n"
    "uniform sampler2D FogMap;\n"
	"uniform sampler2D Diffuse;\n"
    "uniform highp float Waveness;\n"
    "varying mediump vec2 Out_Tex0;\n"
    "varying mediump vec4 Out_ClipSpace;\n"
    "varying vec3 Out_CameraVector;\n"
    "varying mediump vec2 Out_DuDvTexCoord;\n"
    "varying mediump vec3 Out_Refl;\n"
    "varying mediump float Out_FogAmt;\n"
    "void main()\n"
    "{\n"
    "\tmediump vec2 ndc = (Out_ClipSpace.xy/Out_ClipSpace.w) / 2.0 + 0.5;\n"
    "\n"
    "\tlowp vec2 distortedTexCoords = texture2D(Diffuse, vec2(Out_DuDvTexCoord.x + Waveness, Out_DuDvTexCoord.y)).rg * 0."
    "1;\n"
    "\tdistortedTexCoords = Out_DuDvTexCoord + vec2(distortedTexCoords.x, distortedTexCoords.y + Waveness);\n"
    "\tmediump vec2 distSum = (texture2D(Diffuse, distortedTexCoords).rg * 2.0 - 1.0) * 0.01;\n"
    "\n"
    "\tmediump vec2 ReflPos = normalize(Out_Refl.xy) * (Out_Refl.z * 0.5 + 0.5); \n"
    "\tReflPos = (ReflPos * vec2(0.5, 0.5)) + vec2(0.5, 0.5); \n"
    "\tReflPos += distSum;\n"
    "\n"
    "\tReflPos = clamp(ReflPos, 0.001, 0.999); \n"
    "\n"
    "\tlowp vec4 reflectTex = texture2D(EnvMap, ReflPos); \n"
    "\n"
    "\tvec3 ViewVector = normalize(Out_CameraVector);\n"
    "\tfloat RefractionCoef = dot(ViewVector, vec3(0.0, 0.0, 1.0));\n"
    "\tRefractionCoef = pow(RefractionCoef, 1.3);\n"
    "\t\n"
    "\tgl_FragColor = reflectTex;\n"
    "\tgl_FragColor.a = 1.0 - RefractionCoef;\n"
    "\tgl_FragColor = mix(gl_FragColor, vec4(28.0 / 255.0, 163.0 / 255.0, 236.0 / 255.0, 1.0), 0.2);\n"
    "}\n"),
            OBF("#version 100\n"
						"precision highp float;\n"
						"uniform mat4 ProjMatrix;\n"
						"uniform mat4 ViewMatrix;\n"
						"uniform mat4 ObjMatrix;\n"
						"uniform vec3 CameraPosition;\n"
						"uniform vec3 SunPosition;\n"
						"varying mediump float Out_FogAmt;\n"
						"uniform vec3 FogDistances;\n"
						"attribute vec3 Position;\n"
						"attribute vec2 TexCoord0;\n"
						"varying mediump vec2 Out_Tex0;\n"
						"varying mediump vec4 Out_ClipSpace;\n"
						"varying vec3 Out_CameraVector;\n"
						"varying mediump vec2 Out_DuDvTexCoord;\n"
						"varying mediump vec3 Out_Refl;\n"
						"void main()\n"
						"{\n"
						"\tOut_Tex0 = TexCoord0;\n"
						"\tvec4 WorldPos = ObjMatrix * vec4(Position, 1.0);\n"
						"\tvec4 ViewPos = ViewMatrix * WorldPos;\n"
						"\tOut_ClipSpace = ProjMatrix * ViewPos;\n"
						"\tOut_CameraVector = CameraPosition - WorldPos.xyz;\n"
						"\tOut_DuDvTexCoord = vec2(Position.x/2.0 + 0.5, Position.y/2.0 + 0.5) * 0.2;\n"
						"\t\n"
						"\tvec3 WorldNormal = vec3(0.0, 0.0, 1.0);\n"
						"\tvec3 reflVector = normalize(WorldPos.xyz - CameraPosition.xyz);\n"
						"\treflVector = reflVector - 2.0 * dot(reflVector, WorldNormal) * WorldNormal;\n"
						"\tOut_Refl = reflVector;\n"
						"\tOut_FogAmt = clamp((length(WorldPos.xyz - CameraPosition.xyz) - FogDistances.x) * FogDistances.z, 0.0, 1.0);\n"
						"\t\n"
						"\tgl_Position = Out_ClipSpace;\n"
						"}\n"),
            0x1002422);
    result = 1;
    a1[1] = *v2;
    //LOG("Building Shaders");
//	WaterShader::GetUniformsLocation(a1);
    return result;
}

uintptr_t WaterShader::GetUniformsLocation(uintptr_t *a1)
    {
        int v2; // r5
         // r1
        int v4; // r0
        int result; // r0
		
		shaderID = glCreateProgram();
		glAttachShader(shaderID, a1[1]);

        v2 = *(uintptr_t*)(a1[1]);
        a1[2] = glGetUniformLocation(shaderID, "DuDvMap");
        a1[3] = glGetUniformLocation(shaderID, "FogMap");
        UniformLocationWaveness = glGetUniformLocation(shaderID, "Waveness");
	int UniformLocationWaveness2 = glGetUniformLocation(*(uintptr_t *)(v2 + 0x3E8 + 4), "Waveness");
		
        v4 = a1[2];
        a1[4] = UniformLocationWaveness;
		shader = a1;
		//Log("v2 + nul: %d", shader[4]);
		//Log("v2 + 1000: %d", shader[2]);
		//setUnWav(v2);
		
        if ( v4 != -1 ) glUniform1i(shader[2], 3);
       // result = a1[3];
     //   if ( result != -1 ){
			
      //      glUniform1i(result, 2);
      //      return result;
     //   }
	 gotov = 1;
        return result;
    }
int WaterShader::BuildShadersSource2(uintptr_t* a1)
{
    uintptr_t *v2; // r5
    int result; // r0
    v2 = a1;
    v2 = BuildShaderSource(
            (uintptr_t*)v2,
            OBF("#version 100\n"
    "precision mediump float;\n"
    "varying mediump vec2 Out_Tex0;\n"
    "uniform sampler2D Diffuse;\n"
    "uniform sampler2D EnvMap;\n"
    "uniform float SkyMixCoef;\n"
    "void main()\n"
    "{\n"
    "\tlowp vec4 diffuseColor = texture2D(Diffuse, Out_Tex0);\n"
    "\tlowp vec4 nextColor = texture2D(EnvMap, Out_Tex0);\n"
    "\tgl_FragColor = mix(diffuseColor, nextColor, SkyMixCoef);\n"
    "\t\n"
    "}\n"),
    OBF("#version 100\n"
    "precision highp float;\n"
    "uniform mat4 ProjMatrix;\n"
    "uniform mat4 ViewMatrix;\n"
    "uniform mat4 ObjMatrix;\n"
    "attribute vec3 Position;\n"
    "attribute vec2 TexCoord0;\n"
    "varying mediump vec2 Out_Tex0;\n"
    "void main()\n"
    "{\n"
    "\tvec4 WorldPos = ObjMatrix * vec4(Position, 1.0);\n"
    "\tvec4 ViewPos = ViewMatrix * WorldPos;\n"
    "\tOut_Tex0 = TexCoord0;\n"
    "\tgl_Position = ProjMatrix * ViewPos;\n"
    "}\n"),
            0);
    result = 1;
    a1 = v2;
    //Log("Building Shaders2");
    return result;
}






