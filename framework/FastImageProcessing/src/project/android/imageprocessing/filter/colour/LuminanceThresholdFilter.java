package project.android.imageprocessing.filter.colour;

import android.opengl.GLES20;
import project.android.imageprocessing.filter.BasicFilter;

public class LuminanceThresholdFilter extends BasicFilter {
	private static final String UNIFORM_THRESHOLD = "u_Threshold";
	
	private int thresholdHandle;
	private float threshold;
	
	public LuminanceThresholdFilter(float threshold) {
		this.threshold = threshold;
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		thresholdHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_THRESHOLD);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(thresholdHandle, threshold);
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_THRESHOLD+";\n"
				+"vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"	
				+"   float luminance = dot(color.rgb, luminanceWeighting);\n"
		  		+"   gl_FragColor = vec4(vec3(step("+UNIFORM_THRESHOLD+", luminance)), color.a);\n"
		  		+"}\n";	
	}
}
