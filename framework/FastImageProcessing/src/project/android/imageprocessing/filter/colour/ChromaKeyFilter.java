package project.android.imageprocessing.filter.colour;

import android.opengl.GLES20;
import project.android.imageprocessing.filter.BasicFilter;

public class ChromaKeyFilter extends BasicFilter {
	private static final String UNIFORM_COLOUR = "u_Colour";
	private static final String UNIFORM_THRESHOLD = "u_Threshold";
	private static final String UNIFORM_SMOOTHING = "u_Smoothing";
	
	private int colourHandle;
	private int thresholdHandle;
	private int smoothingHandle;
	private float[] colour;
	private float threshold;
	private float smoothing;
	
	public ChromaKeyFilter(float[] colour, float threshold, float smoothing) {
		this.colour = colour;
		this.threshold = threshold;
		this.smoothing = smoothing;
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		colourHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_COLOUR);
		thresholdHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_THRESHOLD);
		smoothingHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_SMOOTHING);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform3f(colourHandle, colour[0], colour[1], colour[2]);
		GLES20.glUniform1f(thresholdHandle, threshold);
		GLES20.glUniform1f(smoothingHandle, smoothing);
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform vec3 "+UNIFORM_COLOUR+";\n"
				+"uniform float "+UNIFORM_THRESHOLD+";\n"
				+"uniform float "+UNIFORM_SMOOTHING+";\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"
		  		+"   float maskY = 0.2989 * "+UNIFORM_COLOUR+".r + 0.5866 * "+UNIFORM_COLOUR+".g + 0.1145 * "+UNIFORM_COLOUR+".b;"
		  		+"   float maskCr = 0.7132 * ("+UNIFORM_COLOUR+".r - maskY);"
		  		+"   float maskCb = 0.5647 * ("+UNIFORM_COLOUR+".b - maskY);"
		  		+"   float Y = 0.2989 * color.r + 0.5866 * color.g + 0.1145 * color.b;"
		  		+"   float Cr = 0.7132 * (color.r - Y);"
		  		+"   float Cb = 0.5647 * (color.b - Y);"
		  		+"   float blendValue = smoothstep("+UNIFORM_THRESHOLD+", "+UNIFORM_THRESHOLD+" + "+UNIFORM_SMOOTHING+", distance(vec2(Cr, Cb), vec2(maskCr, maskCb)));"
		  		+"   gl_FragColor = vec4(color.rgb, color.a * blendValue);\n"
		  		+"}\n";	
	}
}
