
precision mediump float;

varying vec4 v_color;
varying vec2 v_texCoords;




uniform sampler2D u_texture;
uniform sampler2D noiseTex;
uniform sampler2D darkblueTex;
uniform sampler2D high;
uniform float time;

const float threshold = 0.5;
const float range = 0.5;


void main()
{
    vec2 pos = v_texCoords;

    vec4 color2 = texture2D(darkblueTex, pos + time);
    vec4 noise1 = texture2D(noiseTex, fract(pos*2.0 + time * 0.005));
    noise1.rgb = vec3((noise1.r + noise1.g + noise1.b)/3.0);
    vec4 noise2 = texture2D(noiseTex, fract(pos*2.0 - time * 0.005));
    noise2.rgb = vec3((noise2.r + noise2.g + noise2.b)/3.0);

    vec4 color5 = v_color * texture2D(u_texture, pos);

    gl_FragColor = (noise1* + noise2)*0.2 + color5;
}