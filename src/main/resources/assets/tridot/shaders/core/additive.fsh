#version 150

#moj_import <tridot:common.glsl>

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor * ColorModulator;
    if (color.a == 0.0) {
        discard;
    }
    fragColor = applyFog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
