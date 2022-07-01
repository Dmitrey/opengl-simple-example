###1. Shaders

  String vertexShaderSource = "source code";\
  int vertShader = glCreateShader(GL_VERTEX_SHADER);\
  glShaderSource(vertShader, vertexShaderSource);\
  glCompileShader(vertShader);

create-source-compile

  vertex shader and fragment shader supposed to be created by user

###2. Program

int shaderProgram = glCreateProgram();\
        glAttachShader(shaderProgram, vertShader);\
        glAttachShader(shaderProgram, fragShader);\
        glLinkProgram(shaderProgram);\
        glValidateProgram(shaderProgram);

create-attach-link

###3. VBO

int vbo = glGenBuffers();\
FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertices.length);\
floatBuffer.put(vertices).flip();\
glBindBuffer(GL_ARRAY_BUFFER, vbo);\
glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);\
glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);\
glEnableVertexAttribArray(0);
