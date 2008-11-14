package hu.belicza.andras.recursion.examples;

import java.awt.Color;
import java.awt.Graphics;

import hu.belicza.andras.recursion.model.RandomBaseAlgorithm;

/**
 * A simple recursive algorithm to generate labyrinths.
 * 
 * @author András Belicza
 */
public class Labyrinth extends RandomBaseAlgorithm {
	
	private static final String PROPERTY_PATH_WIDTH = "pathWidth";
	
	private int pathWidth;
	
	public Labyrinth() {
		super( "Labyrinth", "András Belicza", "1.0", "A simple recursive algorithm to generate labyrinths." );
		
		defaultProperties.setProperty( PROPERTY_PATH_WIDTH, "20" );
	}
	
	@Override
	protected void validateProperties() throws IllegalArgumentException {
		super.validateProperties();
		
		try {
			pathWidth = Integer.parseInt  ( properties.getProperty( PROPERTY_PATH_WIDTH ) );
		}
		catch ( final Exception e ) {
			throw new IllegalArgumentException( e.getMessage(), e );
		}
	}
	
	@Override
	public void paint( final Graphics graphics, final int width, final int height ) throws IllegalArgumentException {
		super.paint( graphics, width, height );
		
		paint( 0, 0, width - 1, height - 1 );
		
		// A frame for the labyrinth
		graphics.setColor( Color.GREEN );
		graphics.drawRect( 0, 0, width - 1, height - 1 );
	}
	
	public void paint( final int x1, final int y1, final int x2, final int y2 ) {
		final int dx = x2 - x1;
		final int dy = y2 - y1;
		
		if ( dx >= 2*pathWidth && dy >= 2*pathWidth )
			if ( dx > dy ) {
				final int dividerX = x1 + random.nextInt( dx / pathWidth - 1 ) * pathWidth + pathWidth;
				final int gateWayY = y1 + random.nextInt( dy / pathWidth     ) * pathWidth;
				
				graphics.drawLine( dividerX, y1                  , dividerX, gateWayY );
				graphics.drawLine( dividerX, gateWayY + pathWidth, dividerX, y2       );
				
				paint( x1      , y1, dividerX, y2 );
				paint( dividerX, y1, x2      , y2 );
			}
			else {
				final int dividerY = y1 + random.nextInt( dy / pathWidth - 1 ) * pathWidth + pathWidth;
				final int gateWayX = x1 + random.nextInt( dx / pathWidth     ) * pathWidth;
				
				graphics.drawLine( x1                  , dividerY, gateWayX, dividerY );
				graphics.drawLine( gateWayX + pathWidth, dividerY, x2      , dividerY );
				
				paint( x1, y1      , x2, dividerY );
				paint( x1, dividerY, x2, y2       );
			}
	}
	
}
