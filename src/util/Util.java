package util;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glCompileShader;

public class Util {
    
    public static FloatBuffer createBuffer(float[] arr){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(arr.length);
        return (FloatBuffer) floatBuffer.put(arr).flip();
    }
    
    public static IntBuffer createBuffer(int[] arr){
        IntBuffer IntBuffer = BufferUtils.createIntBuffer(arr.length);
        return (IntBuffer) IntBuffer.put(arr).flip();
    }

    private static String getSource(String _path) {
        Path path = Paths.get(_path);
        StringBuilder res = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null)
                res.append(currentLine).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(res.toString());
        return res.toString();
    }

    public static int createFragmentShader(String pathToShader){
        String fragmentShaderSource = getSource(pathToShader);
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        System.out.println("fragShader " + fragShader);
        glShaderSource(fragShader, fragmentShaderSource);
        glCompileShader(fragShader);
        return fragShader;
    }

    public static int createVertexShader(String pathToShader){
        String vertexShaderSource = getSource(pathToShader);
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

    public static FloatBuffer createFlippedBuffer(Matrix4f matrix4f){
        FloatBuffer floatBuffer  = BufferUtils.createFloatBuffer(4*4);
        for (float[] row: matrix4f.getMatrix()) {
            floatBuffer.put(row);
        }
        floatBuffer.flip();
        return floatBuffer;
    }

}
