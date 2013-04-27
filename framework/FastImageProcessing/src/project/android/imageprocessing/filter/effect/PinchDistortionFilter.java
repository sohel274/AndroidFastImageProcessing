package project.android.imageprocessing.filter.effect;

import android.graphics.PointF;

public class PinchDistortionFilter extends BulgeDistortionFilter {
	public PinchDistortionFilter(PointF center, float radius, float distortionAmount, float aspectRatio) {
		super(center, radius, distortionAmount, aspectRatio);
	}

	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform vec2 "+UNIFORM_CENTER+";\n"
				+"uniform float "+UNIFORM_RADIUS+";\n"
				+"uniform float "+UNIFORM_DISTORTION_AMOUNT+";\n"
				+"uniform float "+UNIFORM_ASPECT_RATIO+";\n"
				
		  		+"void main(){\n"
			    +"   highp vec2 textureCoordinateToUse = vec2("+VARYING_TEXCOORD+".x, ("+VARYING_TEXCOORD+".y * "+UNIFORM_ASPECT_RATIO+" + 0.5 - 0.5 * "+UNIFORM_ASPECT_RATIO+"));\n"
			    +"   highp float dist = distance("+UNIFORM_CENTER+", textureCoordinateToUse);\n" 
			    +"   textureCoordinateToUse = "+VARYING_TEXCOORD+";\n"
			    +"   if (dist < "+UNIFORM_RADIUS+") {\n"
			    +"     textureCoordinateToUse -= "+UNIFORM_CENTER+";\n"
			    +"     highp float percent = 1.0 + ("+UNIFORM_RADIUS+" - dist) / "+UNIFORM_RADIUS+" * "+UNIFORM_DISTORTION_AMOUNT+";\n"
			    +"     textureCoordinateToUse = textureCoordinateToUse * percent;\n"
			    +"     textureCoordinateToUse += "+UNIFORM_CENTER+";\n"
	     		+"   }\n"
			    +"   gl_FragColor = texture2D("+UNIFORM_TEXTURE0+", textureCoordinateToUse);\n"
		  		+"}\n";
	}
}
