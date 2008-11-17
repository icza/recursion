package hu.belicza.andras.recursion.examples;

import hu.belicza.andras.recursion.model.RandomBaseAlgorithm;

import java.awt.Graphics;

/**
 * A recursive algorithm to generate terrain and its relief map.
 * 
 * @author Belicza Andras
 */
public class Terrain extends RandomBaseAlgorithm {
	
	private static final String PROPERTY_MIN_HEIGHT  = "minHeight";
	private static final String PROPERTY_MAX_HEIGHT  = "minHeight";
	private static final String PROPERTY_SECTOR_SIZE = "sectorSize";
	
	private static final float FLOATING_ARITHMETIC_PRECISION = 100.0f;
	
	private float minHeight;
	private float maxHeight;
	private int   sectorSize;
	
	public Terrain() {
		super( "Terrain and relief map", "András Belicza", "1.0",
			   "A recursive algorithm to generate terrain and its relief map.\n"
			 + "You can see the non-recursive version of this algorithm in action in the LandFight project:\n"
			 + "http://code.google.com/p/landfight" );
		
		properties.setProperty( PROPERTY_MIN_HEIGHT , "-800.0" );
		properties.setProperty( PROPERTY_MAX_HEIGHT , "2000.0" );
		properties.setProperty( PROPERTY_SECTOR_SIZE, "250"    );
	}
	
	@Override
	protected void validateProperties() throws IllegalArgumentException {
		super.validateProperties();
		
		try {
			minHeight  = Float  .parseFloat( properties.getProperty( PROPERTY_MIN_HEIGHT  ) );
			maxHeight  = Float  .parseFloat( properties.getProperty( PROPERTY_MAX_HEIGHT  ) );
			sectorSize = Integer.parseInt  ( properties.getProperty( PROPERTY_SECTOR_SIZE ) );
		}
		catch ( final Exception e ) {
			throw new IllegalArgumentException( e.getMessage(), e );
		}
	}
	
	@Override
	public void paint( final Graphics graphics, int width, int height ) throws IllegalArgumentException {
		super.paint( graphics, width, height );
		
		width  = width  - width  % ( sectorSize ) + 1;
		height = height - height % ( sectorSize ) + 1;
		
		paint( 0, 0, width - 1, height - 1, randomHeight(), randomHeight(), randomHeight(), randomHeight() );
	}
	
	public void paint( final int x1, final int y1, final int x2, final int y2, final float height1, final float height2, final float height3, final float height4 ) {
		if ( x2 - x1 > sectorSize ) {
			final float height12   = randomHeight();
			final float height23   = randomHeight();
			final float height34   = randomHeight();
			final float height41   = randomHeight();
			final float height1234 = randomHeight();
			paint( x1, y1, x1 + sectorSize, y1 + sectorSize, height1, height12, height1234, height41 );
			paint( x1 + sectorSize, y1, x2, y1 + sectorSize, height12, height2, height23, height1234 );
			paint( x1 + sectorSize, y1 + sectorSize, x2, y2, height1234, height23, height3, height34 );
			paint( x1, y1 + sectorSize, x1 + sectorSize, y2, height41, height1234, height34, height4 );
		}
	}
	
	private float randomHeight() {
		return minHeight + random.nextInt( (int) ( ( maxHeight - minHeight ) * FLOATING_ARITHMETIC_PRECISION ) ) / FLOATING_ARITHMETIC_PRECISION;
	}
	
}
