package hey.btk;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Main {

    public static void main(String[] args) throws LWJGLException {

        Display.setTitle("example");
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();

        glClearColor(0, 0.5f, 0.5f, 0);

        double[] vertices = {
                0.5f,  0.5f, 0.0f,  // top right
                0.5f, -0.5f, 0.0f,  // bottom right
                -0.5f, -0.5f, 0.0f,  // bottom left
                -0.5f,  0.5f, 0.0f   // top left
        };
        int[] indices = {  // note that we start from 0!
                0, 1, 3,   // first triangle
                1, 2, 3    // second triangle
        };

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        System.out.println(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        DoubleBuffer doubleBuffer = BufferUtils.createDoubleBuffer(12);
        doubleBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, doubleBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_DOUBLE, false, 3 * 8, 0);
        glEnableVertexAttribArray(0);

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ebo);
        IntBuffer intBuffer = BufferUtils.createIntBuffer(6);
        intBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,intBuffer,GL_STATIC_DRAW);

//        glEnableVertexAttribArray(1);
        glBindVertexArray(0);

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

        if (glGetProgram(shaderProgram, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(shaderProgram, 1024));
            System.exit(1);
        }


        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render(vertices, vbo, shaderProgram, vao, ebo);
            Display.update();

        }
    }

    public static void render(double vertices[], int vbo, int shaderProgram, int vao, int ebo) {
        glUseProgram(shaderProgram);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
    }
}
