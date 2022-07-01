package util;

public class Matrix4f {

    private float[][] matrix;

    public Matrix4f() {
        matrix = new float[4][4];
    }

    public Matrix4f initIdentity(){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i == j)
                    matrix[i][j] = 1;
                else
                    matrix[i][j] = 0;
            }
        }

        return this;
    }

    public Matrix4f initTranslation(float x, float y, float z){
        matrix[0][0] = 1; matrix[0][1] = 0; matrix[0][2] = 0; matrix[0][3] = x;
        matrix[1][0] = 0; matrix[1][1] = 1; matrix[1][2] = 0; matrix[1][3] = y;
        matrix[2][0] = 0; matrix[2][1] = 0; matrix[2][2] = 1; matrix[2][3] = z;
        matrix[3][0] = 0; matrix[3][1] = 0; matrix[3][2] = 0; matrix[3][3] = 1;
        return this;
    }

    public Matrix4f initRotation(float x, float y, float z){
        Matrix4f rx = new Matrix4f();
        Matrix4f ry = new Matrix4f();
        Matrix4f rz = new Matrix4f();

        x = (float) Math.toRadians(x);
        y = (float) Math.toRadians(y);
        z = (float) Math.toRadians(z);

        rz.matrix[0][0] = cos(z);   rz.matrix[0][1] = -sin(z);  rz.matrix[0][2] = 0;        rz.matrix[0][3] = 0;
        rz.matrix[1][0] = sin(z);   rz.matrix[1][1] = cos(z);   rz.matrix[1][2] = 0;        rz.matrix[1][3] = 0;
        rz.matrix[2][0] = 0;        rz.matrix[2][1] = 0;        rz.matrix[2][2] = 1;        rz.matrix[2][3] = 0;
        rz.matrix[3][0] = 0;        rz.matrix[3][1] = 0;        rz.matrix[3][2] = 0;        rz.matrix[3][3] = 1;
        
        rx.matrix[0][0] = 1;        rx.matrix[0][1] = 0;        rx.matrix[0][2] = 0;        rx.matrix[0][3] = 0;
        rx.matrix[1][0] = 0;        rx.matrix[1][1] = cos(x);   rx.matrix[1][2] = -sin(x);  rx.matrix[1][3] = 0;
        rx.matrix[2][0] = 0;        rx.matrix[2][1] = sin(x);   rx.matrix[2][2] = cos(x);   rx.matrix[2][3] = 0;
        rx.matrix[3][0] = 0;        rx.matrix[3][1] = 0;        rx.matrix[3][2] = 0;        rx.matrix[3][3] = 1;
        
        ry.matrix[0][0] = cos(y);   ry.matrix[0][1] = 0;        ry.matrix[0][2] = sin(y);   ry.matrix[0][3] = 0;
        ry.matrix[1][0] = 0;        ry.matrix[1][1] = 1;        ry.matrix[1][2] = 0;        ry.matrix[1][3] = 0;
        ry.matrix[2][0] = -sin(y);  ry.matrix[2][1] = 0;        ry.matrix[2][2] = cos(y);   ry.matrix[2][3] = 0;
        ry.matrix[3][0] = 0;        ry.matrix[3][1] = 0;        ry.matrix[3][2] = 0;        ry.matrix[3][3] = 1;

        matrix = rz.mult(ry.mult(rx)).getMatrix();

        return this;
    }

    public Matrix4f initScaling(float x, float y, float z){
        matrix[0][0] = x; matrix[0][1] = 0; matrix[0][2] = 0; matrix[0][3] = 0;
        matrix[1][0] = 0; matrix[1][1] = y; matrix[1][2] = 0; matrix[1][3] = 0;
        matrix[2][0] = 0; matrix[2][1] = 0; matrix[2][2] = z; matrix[2][3] = 0;
        matrix[3][0] = 0; matrix[3][1] = 0; matrix[3][2] = 0; matrix[3][3] = 1;
        return this;
    }

    public Matrix4f(float m00, float m01, float m02, float m03,
                    float m10, float m11, float m12, float m13,
                    float m20, float m21, float m22, float m23,
                    float m30, float m31, float m32, float m33) {
        setValue(0, 0, m00);
        setValue(0, 1, m01);
        setValue(0, 2, m02);
        setValue(0, 3, m03);
        setValue(1, 0, m10);
        setValue(1, 1, m11);
        setValue(1, 2, m12);
        setValue(1, 3, m13);
        setValue(2, 0, m20);
        setValue(2, 1, m21);
        setValue(2, 2, m22);
        setValue(2, 3, m23);
        setValue(3, 0, m30);
        setValue(3, 1, m31);
        setValue(3, 2, m32);
        setValue(3, 3, m33);
    }

//    public static int[][] matrixMultiplication(int[][] a, int[][] b) {
//        int[][] c = new int[a.length][a.length];
//        for (int i = 0; i < a.length; i++) {
//            for (int j = 0; j < b.length; j++) {
//                for (int k = 0; k < a.length; k++) {
//                    c[i][j] += a[i][k]*b[k][j];
//                }
//            }
//        }
//        return c;
//    }



    public Matrix4f mult(Matrix4f m2) {
        Matrix4f res = new Matrix4f();
        res.clear();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    res.setValue(i, j, res.getValue(i, j) + matrix[i][k] * m2.getValue(k, j));
                }
            }
        }
        return res;
    }

    public void clear() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public void setValue(int i, int j, float value) {
        matrix[i][j] = value;
    }

    public float getValue(int i, int j) {
        return matrix[i][j];
    }


    public void print() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public float[][] getMatrix() {
        return matrix;
    }
    
    private static float sin(float axis){
        return (float) Math.sin(axis);
    }
    private static float cos(float axis){
        return (float) Math.cos(axis);
    }
}
