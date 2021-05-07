package educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        String pozition = "-0.19999999;-1.0;0.4\n" +
                    "0.20000005;-1.0;0.4\n" +
                    "0.6;-1.0;0.4\n" +
                    "0.6;-0.6;0.4\n" +
                    "-1.0;-0.19999999;0.4\n" +
                    "-0.6;-0.19999999;0.4\n" +
                    "-0.19999999;-0.19999999;0.4\n" +
                    "0.6;-0.19999999;0.4\n" +
                    "-1.0;0.20000005;0.4\n" +
                    "-1.0;0.6;0.4\n" +
                    "-0.6;0.6;0.4\n" +
                    "-0.19999999;0.6;0.4\n";

        String[] split = pozition.split("\n");

        GLFW.glfwInit();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        long window = GLFW.glfwCreateWindow(800, 600, "Kravina:(", 0, 0);

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL33.glViewport(0, 0, 800, 600);
        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            GL33.glViewport(0, 0, w, h);
        });
        Shaders.initShaders();

        ArrayList<Maze> squares = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            String[] NextSplit = split[i].split(";");
            Maze square = new Maze(
                    Float.parseFloat(NextSplit[0]),
                    Float.parseFloat(NextSplit[1]),
                    Float.parseFloat(NextSplit[2]));
            squares.add(square);
        }

        Maze MovingSquare = new Maze(0f, 0f, 0.25f);
        while (!GLFW.glfwWindowShouldClose(window)) {

            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true);
            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            for (int i = 0; i < squares.size(); i++) {
                squares.get(i).render();
            }
            boolean Kolize = false;
            for (int i = 0; i < squares.size(); i++) {
                if (dotykaSe(MovingSquare, squares.get(i))) {
                    Kolize = true;
                }
            }

            MovingSquare.update(window);
            MovingSquare.render();
            if (Kolize) {
                MovingSquare.changeColorToRed();
            } else {
                MovingSquare.changeColorToGreen();
            }

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
        GLFW.glfwTerminate();
    }

    public static boolean dotykaSe(Maze a, Maze b) {
        return (a.getX() + a.getZ() > b.getX() && a.getX() < b.getX() + b.getZ() && a.getY() + a.getZ() / 2 + a.getZ() > b.getY() && a.getY() + a.getZ() / 2 < b.getY() + b.getZ());
    }
}
