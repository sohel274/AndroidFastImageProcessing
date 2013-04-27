package project.android.imageprocessing.helper;

import java.nio.IntBuffer;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class ImageHelper {
	/**
	 * This method takes in a bitmap and binds it to an opengl texture.  This method can only be called if
	 * there is an active opengl context.
	 * @param bitmap
	 * a bitmap to turn into an opengl texture
	 * @return texture
	 * an opengl texture with the given bitmap
	 */
	public static int bitmapToTexture(Bitmap bitmap) {
		int[] tex = new int[1];
		GLES20.glGenTextures(1, tex, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        return tex[0];
	}
}
