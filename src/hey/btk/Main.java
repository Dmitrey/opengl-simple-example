package hey.btk;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.DoubleBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Main {

    public static void main(String[] args) throws LWJGLException {

        Display.setTitle("example");
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();

        glClearColor(0, 0.2f, 0.5f, 0);
        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_FRAMEBUFFER_SRGB);

        double vertices[] = {
                -0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                0.0f, -0.5f, 0.0f
        };

        int vbo = glGenBuffers();
        System.out.println(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        DoubleBuffer doubleBuffer = BufferUtils.createDoubleBuffer(9);
        for (double vertex : vertices) {
            doubleBuffer.put(vertex);
        }
        doubleBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, doubleBuffer, GL_STATIC_DRAW);

        String vertexShaderSource = "#version 330 core\n" +
                "layout (location=0) in vec3 aPos;\n" +
                "void main(){\n" +
                "    gl_Position = vec4(aPos.x,aPos.y,aPos.z,1.0);\n" +
                "}";

        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        System.out.println("vertShader " + vertShader);
        glShaderSource(vertShader, vertexShaderSource);
        glCompileShader(vertShader);

        String fragmentShaderSource = "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "void main(){\n" +
                "    FragColor = vec4(1.0,0.5f,0.2f,1);\n" +
                "}";

        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        System.out.println("fragShader " + fragShader);

        glShaderSource(fragShader, fragmentShaderSource);
        glCompileShader(fragShader);

        int shaderProgram = glCreateProgram();
        System.out.println("shaderProgram " + shaderProgram);

        glAttachShader(shaderProgram, vertShader);
        glAttachShader(shaderProgram, fragShader);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);

        glDeleteShader(vertShader);
        glDeleteShader(fragShader);

        if(glGetProgram(shaderProgram, GL_VALIDATE_STATUS) == 0){
            System.err.println(glGetProgramInfoLog(shaderProgram,1024));
            System.exit(1);
        }


        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render(vertices, vbo, shaderProgram);
            Display.update();

        }
    }

    public static void render(double vertices[], int vbo, int shaderProgram) {
        glUseProgram(shaderProgram);

        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_DOUBLE, false, 3 * 8, 0);
        glDrawArrays(GL_TRIANGLES, 0, 9); // 9?
        glDisableVertexAttribArray(0);

        //todo func to draw
    }
}
