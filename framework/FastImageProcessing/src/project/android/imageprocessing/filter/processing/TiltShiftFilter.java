package project.android.imageprocessing.filter.processing;

import android.opengl.GLES20;
import project.android.imageprocessing.filter.CompositeFilter;
import project.android.imageprocessing.input.GLTextureOutputRenderer;

public class TiltShiftFilter extends CompositeFilter {
	protected static final String UNIFORM_FOCUS_FALLOFF = "u_FocusFalloff";
	protected static final String UNIFORM_TOP_FOCUS = "u_TopFocus";
	protected static final String UNIFORM_BOTTOM_FOCUS = "u_BottomFocus";
	
	private float focusFallOffRate;
	private float topFocusLevel;
	private float bottomFocusLevel;
	private int focusFallOffRateHandle;
	private int topFocusLevelHandle;
	private int bottomFocusLevelHandle;
	
	private GaussianBlurFilter blur;

	public TiltShiftFilter(float blurSize, float topFocusLevel, float bottomFocusLevel, float focusFallOffRate) {
		super(2);
		this.topFocusLevel = topFocusLevel;
		this.bottomFocusLevel = bottomFocusLevel;
		this.focusFallOffRate = focusFallOffRate;
		
		blur = new GaussianBlurFilter(blurSize);
		blur.addTarget(this);

		registerFilter(blur);
		registerInitialFilter(blur);
		registerTerminalFilter(blur);
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		focusFallOffRateHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_FOCUS_FALLOFF);
		topFocusLevelHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TOP_FOCUS);
		bottomFocusLevelHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_BOTTOM_FOCUS);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(focusFallOffRateHandle, focusFallOffRate);
		GLES20.glUniform1f(topFocusLevelHandle, topFocusLevel);
		GLES20.glUniform1f(bottomFocusLevelHandle, bottomFocusLevel);
	} 
	
	@Override
	public void newTextureReady(int texture, GLTextureOutputRenderer source) {
		if(filterLocations.size() < 2 || !filterLocations.get(0).equals(source)) {
			clearRegisteredFilters();
			registerFilter(source, 0);
			registerFilter(blur, 1);
			registerInputOutputFilter(source);
		}
		super.newTextureReady(texture, source);
	}
	
	@Override
	protected String getFragmentShader() {
		return
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n" 
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n"
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_FOCUS_FALLOFF+";\n"
				+"uniform float "+UNIFORM_TOP_FOCUS+";\n"
				+"uniform float "+UNIFORM_BOTTOM_FOCUS+";\n"
						
				
		  		+"void main(){\n"
				+"   vec4 sharpImageColor = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+");\n"
				+"   vec4 blurredImageColor = texture2D("+UNIFORM_TEXTUREBASE+1+", "+VARYING_TEXCOORD+");\n"
				+"   float blurIntensity = 1.0 - smoothstep("+UNIFORM_TOP_FOCUS+" - "+UNIFORM_FOCUS_FALLOFF+", "+UNIFORM_TOP_FOCUS+", "+VARYING_TEXCOORD+".y);\n"
				+"   blurIntensity += smoothstep("+UNIFORM_BOTTOM_FOCUS+", "+UNIFORM_BOTTOM_FOCUS+" + "+UNIFORM_FOCUS_FALLOFF+", "+VARYING_TEXCOORD+".y);\n"
				+"   gl_FragColor = mix(sharpImageColor, blurredImageColor, blurIntensity);\n"
				+"}\n";
	}
}
