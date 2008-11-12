package hu.belicza.andras.recursion.view;

import hu.belicza.andras.recursion.model.Algorithm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Visualiser application for algorithms implementing the {@link hu.belicza.andras.recursion.model.Algorithm} interface.
 * 
 * @author Belicza Andras
 */
@SuppressWarnings("serial")
public class AlgorithmVisualiser extends JFrame {
	
	public static final String APPLICATION_NAME    = "AlgorithmVisualiser";
	public static final String APPLICATION_VERSION = "0.1 2008-11-12";
	public static final String APPLICATION_AUTHOR  = "András Belicza";
	
	public static final String ALGORITHMS_XML_FILE_NAME = "algorithms.xml";
	public static final String ALGORITHM_ELEMENT_NAME   = "algorithm";
	public static final String CLASS_ATTRIBUTE          = "class";
	
	private final Algorithm[] algorithms;
	
	/**
	 * Entry point of the program.<br>
	 * Creates an instance, a frame.
	 * 
	 * @param arguments used to take arguments from the running environment - not used here
	 */
	public static void main( final String[] arguments ) {
		new AlgorithmVisualiser();
	}
	
	public AlgorithmVisualiser() {
		super( APPLICATION_NAME + " version " + APPLICATION_VERSION + " by " + APPLICATION_AUTHOR );
		
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		algorithms = loadAlgorithms();
		
		buildGUI();
		
		setBounds( 70, 30, 900, 650 );
		
		setVisible( true );
	}
	
	/**
	 * Loads the available algorithm list from an XML file, and instantiates them.
	 * @return an array of instances of the found algorithm classes
	 */
	@SuppressWarnings("unchecked")
	private static Algorithm[] loadAlgorithms() {
		List< Algorithm > algorithmList = null;
		
		try {
			final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse( AlgorithmVisualiser.class.getResourceAsStream( ALGORITHMS_XML_FILE_NAME ) );
			
			final NodeList algorithmNodeList = document.getElementsByTagName( ALGORITHM_ELEMENT_NAME );
			
			algorithmList = new ArrayList< Algorithm >( algorithmNodeList.getLength() );
			
			for ( int i = 0; i < algorithmNodeList.getLength(); i++ ) {
				try {
					final String className = algorithmNodeList.item( i ).getAttributes().getNamedItem( CLASS_ATTRIBUTE ).getNodeValue();
					final Class< ? extends Algorithm > algorithmClass = (Class< ? extends Algorithm >) Class.forName( className );
					
					algorithmList.add( algorithmClass.newInstance() );
				}
				catch ( final Exception e ) {
					logMessage( e.getMessage() );
				}
			}
		}
		catch ( final Exception e ) {
			logMessage( e.getMessage() );
		}
		
		return algorithmList == null ? new Algorithm[ 0 ] : algorithmList.toArray( new Algorithm[ algorithmList.size() ] );
	}
	
	private void buildGUI() {
		final Container contentPane = getContentPane();
		
		final JComponent canvasComponent = new JComponent() {
			@Override
			public void paintComponent( final Graphics graphics ) {
				graphics.setColor( Color.PINK );
				graphics.fillRect( 0, 0, getWidth(), getHeight() );
			}
		};
		contentPane.add( canvasComponent, BorderLayout.CENTER );
		
		final Box controlBox = Box.createVerticalBox();
		final JLabel chooseLabel = new JLabel( "Choose an algorithm:" );
		chooseLabel.setAlignmentX( CENTER_ALIGNMENT );
		controlBox.add( chooseLabel );
		final JComboBox algorithmComboBox = new JComboBox( algorithms );
		controlBox.add( algorithmComboBox );
		
		final JPanel wrapperPanel = new JPanel();
		wrapperPanel.add( controlBox );
		contentPane.add( wrapperPanel, BorderLayout.EAST );
	}
	
	/**
	 * Logs a message.
	 * @param message message to be logged
	 */
	private static void logMessage( final String message ) {
		System.out.println( message );
	}
	
}
