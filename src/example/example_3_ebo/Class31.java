package example.example_3_ebo;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import util.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Class31 {
    public static float[] vertices = {
            -0.3f, 0,
            0.0f, 0.3f,
            0.3f, 0,
            0, -0.3f};

    public static int[] indices = {
            0,1,3,
            2,1,3};

    public static void main(String[] args) throws LWJGLException {
        Display.setTitle("two triangles");
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();

        glClearColor(0, 0.5f, 0.5f, 0);

        int vertexShader = Util.createVertexShader("src/example/res/vert_shader.glsl");
        int fragShader = Util.createFragmentShader("src/example/res/frag_shader.glsl");
        int program = Util.createProgram(vertexShader,fragShader);

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER,Util.createBuffer(vertices),GL_STATIC_DRAW);
        glVertexAttribPointer(0,2,GL_FLOAT,false,2*4,0);
        glEnableVertexAttribArray(0);

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,Util.createBuffer(indices), GL_STATIC_DRAW);

        long start = System.currentTimeMillis() - 1000;
        while (!Display.isCloseRequested()) {
            if (System.currentTimeMillis() - start > 1_000) {

                start = System.currentTimeMillis();
                System.out.println(start);

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glUseProgram(program);

                glBindVertexArray(vao);
                glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
                Display.update();
            }
        }
    }
}
