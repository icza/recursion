package hu.belicza.andras.recursion.examples;

import hu.belicza.andras.recursion.model.RandomBaseAlgorithm;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A recursive algorithm to generate terrain and draw its relief map.
 * 
 * @author Belicza Andras
 */
public class Terrain extends RandomBaseAlgorithm {
	
	private static final String PROPERTY_MIN_HEIGHT  = "minHeight";
	private static final String PROPERTY_MAX_HEIGHT  = "maxHeight";
	private static final String PROPERTY_SECTOR_SIZE = "sectorSize";
	private static final String PROPERTY_DISPERSION  = "dispersion";
	
	/** Values of heights where the relief map changes colors at.      */
	private static final float[] HEIGHT_STONES   = new float[] { -800.0f                 , 0.0f                    , 0.0f                   , 600.0f                    , 900.0f                    , 1300.0f                  , 1600.0f                   , 2000.0f                , Float.MAX_VALUE              };
	/** Values of colors at the height stones to be used for landscape */
	private static final Color[] STONE_COLORS    = new Color[] { new Color( 39, 23, 112 ), new Color( 0, 146, 221 ), new Color( 0, 146, 63 ), new Color( 126, 195, 128 ), new Color( 184, 218, 141 ), new Color( 255, 252, 212 ), new Color( 187, 129, 92 ), new Color( 90, 58, 37 ), Color.BLACK                  };
	
	private float minHeight;
	private float maxHeight;
	private int   sectorSize;
	private float dispersion;
	
	public Terrain() {
		super( "Terrain and relief map", "András Belicza", "1.0",
			   "A recursive algorithm to generate terrain and draw its relief map.\n"
			 + "You can see the non-recursive version of this algorithm in action in the LandFight project:\n"
			 + "http://code.google.com/p/landfight" );
		
		properties.setProperty( PROPERTY_MIN_HEIGHT , "-800.0" );
		properties.setProperty( PROPERTY_MAX_HEIGHT , "2000.0" );
		properties.setProperty( PROPERTY_SECTOR_SIZE, "250"    );
		properties.setProperty( PROPERTY_DISPERSION , "30.0"   );
	}
	
	@Override
	protected void validateProperties() throws IllegalArgumentException {
		super.validateProperties();
		
		try {
			minHeight  = Float  .parseFloat( properties.getProperty( PROPERTY_MIN_HEIGHT  ) );
			maxHeight  = Float  .parseFloat( properties.getProperty( PROPERTY_MAX_HEIGHT  ) );
			sectorSize = Integer.parseInt  ( properties.getProperty( PROPERTY_SECTOR_SIZE ) );
			dispersion = Float  .parseFloat( properties.getProperty( PROPERTY_DISPERSION  ) );
		}
		catch ( final Exception e ) {
			throw new IllegalArgumentException( e.getMessage(), e );
		}
	}
	
	@Override
	public void paint( final Graphics graphics, int width, int height ) throws IllegalArgumentException {
		super.paint( graphics, width, height );
		
		width  = width  - width  % sectorSize + 1;
		height = height - height % sectorSize + 1;
		
		paint( 0, 0, width - 1, height - 1, randomHeight(), randomHeight(), randomHeight(), randomHeight() );
	}
	
	public void paint( final int x1, final int y1, final int x2, final int y2, final float height1, final float height2, final float height3, final float height4 ) {
		final int dx = x2 - x1;
		final int dy = y2 - y1;
		
		if ( dx > sectorSize || dy > sectorSize ) {
			if ( dx > sectorSize && dy > sectorSize ) {
				final float[] baseHeights = randomHeights( 5 );
				paint( x1, y1, x1 + sectorSize, y1 + sectorSize, height1, baseHeights[ 0 ], baseHeights[ 4 ], baseHeights[ 3 ] );
				paint( x1 + sectorSize, y1, x2, y1 + sectorSize, baseHeights[ 0 ], height2, baseHeights[ 1 ], baseHeights[ 4 ] );
				paint( x1, y1 + sectorSize, x1 + sectorSize, y2, baseHeights[ 3 ], baseHeights[ 4 ], baseHeights[ 2 ], height4 );
				paint( x1 + sectorSize, y1 + sectorSize, x2, y2, baseHeights[ 4 ], baseHeights[ 1 ], height3, baseHeights[ 2 ] );
			}
			else if ( dx > sectorSize ) {
				final float[] baseHeights = randomHeights( 2 );
				paint( x1, y1, x1 + sectorSize, y2, height1, baseHeights[ 0 ], baseHeights[ 1 ], height4 );
				paint( x1 + sectorSize, y1, x2, y2, baseHeights[ 0 ], height2, height3, baseHeights[ 1 ] );
			}
			else { // dy > sectorSize
				final float[] baseHeights = randomHeights( 2 );
				paint( x1, y1, x2, y1 + sectorSize, height1, height2, baseHeights[ 0 ], baseHeights[ 1 ] );
				paint( x1, y1 + sectorSize, x2, y2, baseHeights[ 1 ], baseHeights[ 0 ], height3, height4 );
			}
		}
		else {
			// Generate and draw the sector 
			for ( int y = y1; y < y2; y++ ) {
				final float baseLineStartHeight = interpolate( height1, height4, (float) ( y - y1 ) / dy );
				final float baseLineEndHeight   = interpolate( height2, height3, (float) ( y - y1 ) / dy );
				for ( int x = x1; x < x2; x++ ) {
					// Finally generate the height of a point
					float height = interpolate( baseLineStartHeight, baseLineEndHeight, (float) ( x - x1 ) / dx );
					height += ( 0.5f - random.nextFloat() ) * dispersion; // Add random dispersion to it
					// Leave it between limits
					height = height < minHeight ? minHeight : ( height > maxHeight ? maxHeight : height );
					graphics.setColor( new Color( getRGBOfHeight( height ) ) );
					graphics.drawLine( x, y, x, y );
				}
			}
		}
	}
	
	private float[] randomHeights( int count ) {
		final float[] randomHeights = new float[ count ];
		
		while ( count-- > 0 )
			randomHeights[ count ] = randomHeight();
		
		return randomHeights;
	}
	
	private float randomHeight() {
		return minHeight + random.nextFloat() * ( maxHeight - minHeight );
	}
	
	/**
	 * Interpolates. Calculates a 3rd value at a specified position between 2 boundary value.<br>
	 * The position is specified by a ratio, whose value means:
	 * 0 => value1, 1 => value2, 0 < ratio < 1  => somewhere between value1 and value2.
	 * We would like the result to be continuous and nice, so 'somewhere' is determined by a
	 * function which is non-linear (linear resulted in "breaklines"). It goes from value1
	 * to value2 by the function which starts with 0 derivation and ends with 0 derivation
	 * (it has an infletion point, it has an 'S'). We use for this an 'x^4' function:<br>
	 * f(x) = a*x^4 + b*x^3 + c*x^2 + d*x^1 + e*x^0<br>
	 * We have parameters: f(0)=0, f(1)=1, f'(0)=0, f'(1)=0, and we'd like that f'(0.5)=0.5.
	 * The result is this:<br>
	 * 
	 * f(x) = 2*x^4 - 6*x^3 + 5*x^2   (my magic function!)
	 * 
	 * @param value1 first value of interpolation
	 * @param value2 last value of interpolation
	 * @param ratio  where between value1 and value2 we need the interpolation
	 * @return the interpolation of value1 and value2 at a position between them specified by ratio
	 */
	private float interpolate( final float value1, final float value2, final float ratio ) {
		final float ratio2 = ratio * ratio;
		return value1 + ( value2 - value1 ) * ( 2.0f * ratio2*ratio2 - 6.0f * ratio2 * ratio + 5.0f * ratio2 );
	}
	
	/**
	 * Calculates and returns the RGB components of the color of the specified height on the landscape.<br>
	 * The returned int value contains the rgb values in the right order. Blue is at the least significant bits.
	 * All of the rgb components are 8 bit precision.
	 * @param height height whose color needed to be calculated
	 * @return the RGB values of the specified height on the landscape
	 */
	private int getRGBOfHeight( final float height ) {
		// We locate the height between 2 heights whose interpolatable colors will determine the searched RGB
		int i;
		for ( i = 1; height > HEIGHT_STONES[ i ]; i++ )
			;
		
		return interpolateColors( STONE_COLORS[ i - 1 ], STONE_COLORS[ i ], ( height - HEIGHT_STONES[ i - 1 ] ) / ( HEIGHT_STONES[ i ] - HEIGHT_STONES[ i - 1 ] ) );
	}
	
	/**
	 * Linear-interpolates colors. Calculates a 3rd color at a specified position between 2 boundary colors.<br>
	 * The position is specified by a ratio, whose value means:
	 * 0 => value1, 1 => value2, 0 < ratio < 1  => somewhere between value1 and value2.<br>
	 * In case of colors, interpolation means interpolation of red, green and blue components of colors.
	 * 
	 * @param value1 first value of interpolation
	 * @param value2 last value of interpolation
	 * @param ratio  where between value1 and value2 we need the interpolation
	 * @return the RGB value of the linear interpolation of value1 and value2 at a position between them specified by ratio
	 */
	private int interpolateColors( final Color value1, final Color value2, final double ratio ) {
		final int a = value1.getAlpha() + (int) ( ( value2.getAlpha() - value1.getAlpha() ) * ratio );
		final int r = value1.getRed  () + (int) ( ( value2.getRed  () - value1.getRed  () ) * ratio );
		final int g = value1.getGreen() + (int) ( ( value2.getGreen() - value1.getGreen() ) * ratio );
		final int b = value1.getBlue () + (int) ( ( value2.getBlue () - value1.getBlue () ) * ratio );
		
		return ( a << 24 ) + ( r << 16 ) + ( g << 8 ) + b;
	}
	
}
