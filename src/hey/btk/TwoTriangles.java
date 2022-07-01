package hey.btk;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TwoTriangles {

    private static int vertShader;
    private static int yellowFragShader;
    private static int orangeFragShader;
    private static int yellowProgram;
    private static int orangeProgram;

    private static int vbo1;
    private static int vbo2;
    private static int vao1;
    private static int vao2;

    private static float[] vertices1 = {
            -0.8f,  -0.4f, 0.0f,
            -0.4f, 0.4f, 0.0f,
            0, -0.4f, 0.0f
    };
    private static float[] vertices2 = {
            0f,  -0.4f, 0.0f,
            0.4f, 0.4f, 0.0f,
            0.8f, -0.4f, 0.0f
    };

    public static void main(String[] args) throws LWJGLException {

        Display.setTitle("two triangles");
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();

        glClearColor(0, 0.5f, 0.5f, 0);
        bindShaders();

        vao1 = glGenVertexArrays();
        glBindVertexArray(vao1);
        vbo1 = createVbo(vertices1);

        vao2 = glGenVertexArrays();
        glBindVertexArray(vao2);
        vbo2 = createVbo(vertices2);

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            Display.update();
        }
    }

    public static void render() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(yellowProgram);
        glBindVertexArray(vao1);
        glDrawArrays(GL_TRIANGLES,0,9);

        glUseProgram(orangeProgram);
        glBindVertexArray(vao2);
        glDrawArrays(GL_TRIANGLES,0,9);

    }

    public static int createYellowFragmentShader(){
        String fragmentShaderSource = "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "void main(){\n" +
                "    FragColor = vec4(0.0,0.5f,0.8f,1);\n" +
                "}";

        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        System.out.println("fragShader " + fragShader);

        glShaderSource(fragShader, fragmentShaderSource);
        glCompileShader(fragShader);
        return fragShader;
    }

    public static int createOrangeFragmentShader(){
        String fragmentShaderSource = "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "void main(){\n" +
                "    FragColor = vec4(1.0,0.5f,0.2f,1);\n" +
                "}";
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        System.out.println("fragShader " + fragShader);
        glShaderSource(fragShader, fragmentShaderSource);
        glCompileShader(fragShader);
        return fragShader;
    }

    public static int createVertexShader(){
        String vertexShaderSource = "#version 330 core\n" +
                "layout (location=0) in vec3 aPos;\n" +
                "void main(){\n" +
                "    gl_Position = vec4(aPos.x,aPos.y,aPos.z,1.0);\n" +
                "}";
        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        System.out.println("vertShader " + vertShader);
        glShaderSource(vertShader, vertexShaderSource);
        glCompileShader(vertShader);
        return vertShader;
    }

    public static int createProgram(int vertShader, int fragShader){
        int shaderProgram = glCreateProgram();

        System.out.println("shaderProgram " + shaderProgram);
        glAttachShader(shaderProgram, vertShader);
        glAttachShader(shaderProgram, fragShader);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
        return shaderProgram;
    }

    public static void bindShaders(){
        vertShader = createVertexShader();
        yellowFragShader = createYellowFragmentShader();
        orangeFragShader = createOrangeFragmentShader();
        yellowProgram = createProgram(vertShader,yellowFragShader);
        orangeProgram = createProgram(vertShader,orangeFragShader);


        glValidateProgram(yellowProgram);
        if (glGetProgram(yellowProgram, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(yellowProgram, 1024));
            System.exit(1);
        }

        glValidateProgram(orangeProgram);
        if (glGetProgram(orangeProgram, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(orangeProgram, 1024));
            System.exit(1);
        }

        glDeleteShader(vertShader);
        glDeleteShader(yellowFragShader);
        glDeleteShader(orangeFragShader);
    }

    public static int createVbo(float[] vertices){

        int vbo = glGenBuffers();
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertices.length);
        floatBuffer.put(vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
        glEnableVertexAttribArray(0);
        return vbo;
    }
}
