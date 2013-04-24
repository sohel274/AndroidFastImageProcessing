package project.android.imageprocessing.filter.colour;

import android.opengl.GLES20;
import project.android.imageprocessing.filter.BasicFilter;


public class FalseColourFilter extends BasicFilter {
	private static final String UNIFORM_SHADOW_COLOUR = "u_ShadowColour";
	private static final String UNIFORM_HIGHLIGHT_COLOUR = "u_HightlightColour";
	
	private int shadowColourHandle;
	private int highlightColourHandle;
	private float[] shadowColour;
	private float[] highlightColour;

	public FalseColourFilter(float[] shadowColour, float[] highlightColour) {
		this.shadowColour = shadowColour;
		this.highlightColour = highlightColour;
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		shadowColourHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_SHADOW_COLOUR);
		highlightColourHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_HIGHLIGHT_COLOUR); 
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform3f(shadowColourHandle, shadowColour[0], shadowColour[1], shadowColour[2]);
		GLES20.glUniform3f(highlightColourHandle, highlightColour[0], highlightColour[1], highlightColour[2]);
	}
	@Override
	protected String getFragmentShader() {
		return 
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform vec3 "+UNIFORM_SHADOW_COLOUR+";\n"	
				+"uniform vec3 "+UNIFORM_HIGHLIGHT_COLOUR+";\n"	
				+"vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"	
				+"   float luminance =  dot(color.rgb, luminanceWeighting);\n"
		  		+"   gl_FragColor = vec4(mix("+UNIFORM_SHADOW_COLOUR+", "+UNIFORM_HIGHLIGHT_COLOUR+", luminance), color.a);\n"
		  		+"}\n";	
	}
}
