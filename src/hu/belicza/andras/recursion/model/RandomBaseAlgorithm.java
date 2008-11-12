package hu.belicza.andras.recursion.model;

import java.util.Random;

/**
 * 
 * @author Belicza Andras
 */
public abstract class RandomBaseAlgorithm extends BaseAlgorithm {
	
	protected static final String PROPERTY_RANDOM_SEED = "random";
	
	protected final Random random = new Random();
	
	public RandomBaseAlgorithm( final String name, final String author, final String version, final String description ) {
		super( name, author, version, description );
		
		defaultProperties.setProperty( PROPERTY_RANDOM_SEED, Long.toString( new Random().nextLong() ) );
	}
	
	@Override
	protected void validateProperties() throws IllegalArgumentException {
		try {
			random.setSeed( Long.parseLong( properties.getProperty( PROPERTY_RANDOM_SEED ) ) );
		}
		catch ( final Exception e ) {
			throw new IllegalArgumentException( e.getMessage(), e );
		}
	}
	
}
