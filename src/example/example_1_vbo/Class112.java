package example.example_1_vbo;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import util.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;

public class Class112 {
    public static float[] vertices = {
            -0.1f, -0.1f, 0, 0,
            0.0f, 0.1f, 0, 0,
            0.1f, -0.1f, 0, 0};

    public static float[] verticesWithColors = {
            0.4f, -0.1f, 0.2f, 0.2f, 0,
            0.5f, 0.1f, 0.2f, 0.2f, 0,
            0.6f, -0.1f, 0.2f, 0.2f, 0};

    public static void main(String[] args) throws LWJGLException {
        Display.setTitle("two triangles");
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();

        int vertexShader = Util.createVertexShader("src/example/res/vert_shader.glsl");
        int fragShader = Util.createFragmentShader("src/example/res/frag_shader.glsl");
        int program = Util.createProgram(vertexShader,fragShader);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo); // присвоили тип буфера
        glBufferData(GL_ARRAY_BUFFER, Util.createBuffer(verticesWithColors),GL_STATIC_DRAW); // скопировали данные в буфер
        glVertexAttribPointer(0,3,GL_FLOAT,false,5*4,0); // указали как считывать данные
        glEnableVertexAttribArray(0);

        long start = System.currentTimeMillis() - 1000;
        while (!Display.isCloseRequested()) {
            if (System.currentTimeMillis() - start > 1_000) {
                start = System.currentTimeMillis();
                System.out.println(start);

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glUseProgram(program);
                glDrawArrays(GL_TRIANGLES, 0, 3);
                Display.update();
            }
        }
    }
}
