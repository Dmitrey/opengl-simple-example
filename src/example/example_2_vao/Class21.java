package example.example_2_vao;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import util.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Class21 {
    public static float[] vertices = {
            -0.1f, -0.1f, 0,
            0.0f, 0.1f, 0,
            0.1f, -0.1f, 0};

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

        int vao1 = glGenVertexArrays();
        glBindVertexArray(vao1);
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo); // присвоили тип буфера
        glBufferData(GL_ARRAY_BUFFER, Util.createBuffer(vertices),GL_STATIC_DRAW); // скопировали данные в буфер
        glVertexAttribPointer(0,3,GL_FLOAT,false,3*4,0); // указали как считывать данные
        glEnableVertexAttribArray(0);

        int vao2 = glGenVertexArrays();
        glBindVertexArray(vao2);
        int vbo2 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo2); // присвоили тип буфера
        glBufferData(GL_ARRAY_BUFFER, Util.createBuffer(verticesWithColors),GL_STATIC_DRAW); // скопировали данные в буфер
        glVertexAttribPointer(0,2,GL_FLOAT,false,5*4,0); // указали как считывать данные
        glEnableVertexAttribArray(0);

        long start = System.currentTimeMillis() - 1000;
        while (!Display.isCloseRequested()) {
            if (System.currentTimeMillis() - start > 1_000) {

                start = System.currentTimeMillis();
                System.out.println(start);

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glUseProgram(program);

                glBindVertexArray(vao1);
                glDrawArrays(GL_TRIANGLES, 0, 3);

                glBindVertexArray(vao2);
                glDrawArrays(GL_TRIANGLES, 0, 3);

                Display.update();
            }
        }
    }
}
