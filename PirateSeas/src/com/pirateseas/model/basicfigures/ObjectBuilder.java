/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.pirateseas.model.basicfigures;

import static android.opengl.GLES20.glDrawArrays;

import java.util.ArrayList;
import java.util.List;

class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;

    static interface DrawCommand {
        void draw();
    }

    static class GeneratedData {
        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
    private int offset = 0;

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    private GeneratedData build() {
        return new GeneratedData(vertexData, drawList);
    }
}
