package project.android.imageprocessing.filter.effect;

import android.opengl.GLES20;

public class PolkaDotFilter extends PixellateFilter {
	protected static final String UNIFORM_DOT_SCALING = "u_DotScaling";
	
	private int dotScalingHandle;
	private float dotScaling;
	
	public PolkaDotFilter(float dotScaling, float fractionalWidth, float aspectRatio) {
		super(fractionalWidth, aspectRatio);
		this.dotScaling = dotScaling;
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		dotScalingHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_DOT_SCALING);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(dotScalingHandle, dotScaling);
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_FRACTIONAL_WIDTH+";\n"	
				+"uniform float "+UNIFORM_ASPECT_RATIO+";\n"
				+"uniform highp float "+UNIFORM_DOT_SCALING+";\n"
				
		  		+"void main(){\n"
		  		+"   highp vec2 sampleDivisor = vec2("+UNIFORM_FRACTIONAL_WIDTH+", "+UNIFORM_FRACTIONAL_WIDTH+" /  "+UNIFORM_ASPECT_RATIO+");\n"
			    +"   highp vec2 samplePos = "+VARYING_TEXCOORD+" - mod("+VARYING_TEXCOORD+", sampleDivisor) + 0.5 * sampleDivisor;\n"
		  		+"   highp vec2 textureCoordinateToUse = vec2("+VARYING_TEXCOORD+".x, ("+VARYING_TEXCOORD+".y * "+UNIFORM_ASPECT_RATIO+" + 0.5 - 0.5 * "+UNIFORM_ASPECT_RATIO+"));\n"
		  		+"   highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * "+UNIFORM_ASPECT_RATIO+" + 0.5 - 0.5 * "+UNIFORM_ASPECT_RATIO+"));\n"
			    +"   highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);\n"
			    +"   lowp float checkForPresenceWithinDot = step(distanceFromSamplePoint, ("+UNIFORM_FRACTIONAL_WIDTH+" * 0.5) * "+UNIFORM_DOT_SCALING+");\n"
			    +"   gl_FragColor = vec4(texture2D("+UNIFORM_TEXTURE0+", samplePos ).rgb * checkForPresenceWithinDot, 1.0);\n"
		  		+"}\n";	
	}
}
