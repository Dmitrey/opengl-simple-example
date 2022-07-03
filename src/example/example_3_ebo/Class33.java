package example.example_3_ebo;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import util.Matrix4f;
import util.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Class33 {

    static float x = (float) Math.sqrt(2);

    public static float[] vertices = {
            0,1,0,
            -1,1-x,-1,
            -1,1-x,1,
            1,1-x,1,
            1,1-x,-1
    };

//    public static int[] indices = new int[]{
//            2,0,3,
//            3,1,2,
//            3,4,1
//    };

    public static int[] indices = new int[]{
            1,2,4,
            2,3,4,
            1,0,4,
            4,0,3,
            3,0,2,
            2,0,1
    };

    public static void main(String[] args) throws LWJGLException {
        Display.setTitle("two triangles");
        Display.setDisplayMode(new DisplayMode(800, 800));
        Display.create();

        glClearColor(0, 0, 0, 0);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        //todo be careful with this
//        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_FRAMEBUFFER_SRGB);

        int vertexShader = Util.createVertexShader("src/example/res/vert_shader.glsl");
        int fragShader = Util.createFragmentShader("src/example/res/frag_shader.glsl");
        int program = Util.createProgram(vertexShader, fragShader);

        int uniformLocation = glGetUniformLocation(program, "tr_matrix");

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Util.createBuffer(vertices), GL_STATIC_DRAW);

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createBuffer(indices), GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
        glEnableVertexAttribArray(0);

        long start = System.currentTimeMillis();
        long counter = 0;
        while (!Display.isCloseRequested()) {
            if (System.currentTimeMillis() - start > 10) {
                start = System.currentTimeMillis();
                System.out.println(start);
                counter++;
            }

            //просто составляем матрицу по специальному алгоритму на основе углов поворота по осям
            Matrix4f rotation = new Matrix4f().initRotation(counter, counter, 0);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glUseProgram(program);

            //передаем полученную матрицу шейдеру, где на неё умножаются векторы положения вершин?
            glUniformMatrix4(uniformLocation, true, Util.createFlippedBuffer(rotation));

            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            Display.update();

        }
    }
}
