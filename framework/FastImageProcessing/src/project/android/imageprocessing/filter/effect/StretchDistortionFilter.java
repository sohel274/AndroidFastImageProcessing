package project.android.imageprocessing.filter.effect;

import android.graphics.PointF;
import android.opengl.GLES20;
import project.android.imageprocessing.filter.BasicFilter;

public class StretchDistortionFilter extends BasicFilter {
	protected static final String UNIFORM_CENTER = "u_Center";
	
	private int centerHandle;
	private PointF center;
	
	public StretchDistortionFilter(PointF center) {
		this.center = center;
	}
		
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		centerHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_CENTER);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform2f(centerHandle, center.x, center.y);
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform vec2 "+UNIFORM_CENTER+";\n"
				
				
		  		+"void main(){\n"
		  		+"	highp vec2 normCoord = 2.0 * "+VARYING_TEXCOORD+" - 1.0;\n"
			    +"	highp vec2 normCenter = 2.0 * "+UNIFORM_CENTER+" - 1.0;\n"
			    +"	normCoord -= normCenter;\n"
			    +"	mediump vec2 s = sign(normCoord);\n"
			    +"	normCoord = abs(normCoord);\n"
			    +"	normCoord = 0.5 * normCoord + 0.5 * smoothstep(0.25, 0.5, normCoord) * normCoord;\n"
			    +"	normCoord = s * normCoord;\n"
			    +"	normCoord += normCenter;\n"
			    +"	mediump vec2 textureCoordinateToUse = normCoord / 2.0 + 0.5;\n"
			    +"	gl_FragColor = texture2D("+UNIFORM_TEXTURE0+", textureCoordinateToUse );\n"
		  		+"}\n";
	}
}
