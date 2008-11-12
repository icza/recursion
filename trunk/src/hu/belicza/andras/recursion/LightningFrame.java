package hu.belicza.andras.recursion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Andras Belicza
 */
@SuppressWarnings("serial")
public class LightningFrame extends JFrame {
	
	public static void main( final String[] arguments ) {
		final LightningFrame mainFrame = new LightningFrame();
		mainFrame.setTitle( "The Power of Recursion" );
		mainFrame.setLocation( 100, 100 );
		mainFrame.setSize( 800, 800 );
		
		final JComponent painter = new JComponent() {
			public void paintComponent( final Graphics graphics ) {
				graphics.setColor( Color.BLACK );
				graphics.fillRect( 0, 0, getWidth(), getHeight() );
				graphics.setColor( new Color( 255, 255, 0 ) );
				graphics.setColor( new Color( 255, 255, 0 ) );
				
				LightningFrame.graphics = graphics;
				paintLightning( 0, 0, getWidth() - 1, getHeight() - 1 );
				LightningFrame.graphics = null;
			}
		};
		
		mainFrame.addKeyListener( new KeyAdapter() {
			public void keyTyped( final KeyEvent ke ) {
				painter.repaint();
			}
		} );
		
		mainFrame.add( painter, BorderLayout.CENTER );
		mainFrame.add( new JLabel( "Press any key to refresh", JLabel.CENTER ), BorderLayout.SOUTH );
		
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		mainFrame.setVisible( true );
	}
	
	private static Graphics    graphics;
	private static Random      random      = new Random();
	private static final int   GRANULARITY = 20;
	private static final float DISPERSION  = 0.3f;
	
	public static void paintLightning( final int x1, final int y1, final int x2, final int y2 ) {
		final int internalX = x1 + random.nextInt( (int) ( ( x2 - x1 ) * DISPERSION ) + 1 );
		final int internalY = y1 + random.nextInt( (int) ( ( y2 - y1 ) * DISPERSION ) + 1 );
		
		if ( x2 - x1 + y2 - y1 < GRANULARITY ) {
			graphics.drawLine( x1, y1, internalX, internalY );
			graphics.drawLine( internalX, internalY, x2, y2 );
		}
		else {
			paintLightning( x1, y1, internalX, internalY );
			paintLightning( internalX, internalY, x2, y2 );
		}
	}
	
}
