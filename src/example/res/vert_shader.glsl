#version 330 core

layout (location=0) in vec3 aPos;
out vec4 color;
uniform mat4 tr_matrix;

void main(){
    //gl_Position = vec4(aPos.x,aPos.y,aPos.z,1.0);
    color = vec4(clamp(aPos,0.0, 1.0), 1.0);
    gl_Position = tr_matrix * vec4(0.5*aPos,1.0);
}