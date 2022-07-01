package example;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Main {

    public static float[] vertices = {-0.1f, -0.1f, 1,
            0.0f, 0.1f, 1,
            0.1f, -0.1f, 1};

    public static float[] verticesAndColors = {
            -0.1f, -0.1f, 1f, 0.0f, 0.0f,
            0.0f, 0.1f, 0.0f, 1f, 0.0f,
            0.1f, -0.1f, 0.0f, 0.0f, 1f};

    public static void main(String[] args) throws LWJGLException, NoSuchFieldException, NoSuchMethodException {
        Display.setTitle("two triangles");
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();

        //create shaders

        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        String vertShaderSource = getSource("res/vert_shader.glsl");
        glShaderSource(vertShader, vertShaderSource);
        glCompileShader(vertShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        String fragShaderSource = getSource("res/frag_shader.glsl");
        glShaderSource(fragmentShader, fragShaderSource);
        glCompileShader(fragmentShader);

        int program = glCreateProgram();
        glAttachShader(program, vertShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(15);
        floatBuffer.put(verticesAndColors).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 5 * 4, 2*4);
        glEnableVertexAttribArray(1);

        long startSeconds = System.currentTimeMillis() / 60;

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glUseProgram(program);
            long currentSeconds = System.currentTimeMillis() / 60;
            int uniformLocation = glGetUniformLocation(program, "ourColor");
            System.out.println("uniformLocation: " + uniformLocation);
            glUniform4f(uniformLocation, 0.0f, (float) Math.sin((double) (currentSeconds - startSeconds) / 10), 0.0f, 1.0f);
            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            Display.update();
        }
    }

    private static String getSource(String _path) {
        Path path = Paths.get(_path);
        StringBuilder res = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null)
                res.append(currentLine).append("\n");
        } catch (IOException e) {
        }
        System.out.println(res.toString());
        return res.toString();
    }
}
