package hu.belicza.andras.recursion.view;

import hu.belicza.andras.recursion.model.Algorithm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Visualiser application for algorithms implementing the {@link Algorithm} interface.
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
	
	/** Combo box to display and select available algorithms.                */
	private final JComboBox algorithmComboBox;
	/** Text area to view and edit the properties of the selected algorithm. */
	private final JTextArea propertiesTextArea = new JTextArea();
	
	/**
	 * Entry point of the program.<br>
	 * Creates an instance, a frame.
	 * 
	 * @param arguments used to take arguments from the running environment - not used here
	 */
	public static void main( final String[] arguments ) {
		new AlgorithmVisualiser();
	}
	
	/**
	 * Creates a new <code>AlgorithmVisualiser</code>.
	 */
	public AlgorithmVisualiser() {
		super( APPLICATION_NAME + " version " + APPLICATION_VERSION + " by " + APPLICATION_AUTHOR );
		
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		algorithmComboBox = new JComboBox( loadAlgorithms() );	
		
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
	
	/**
	 * Builds the graphical user interface of the algorithm visualiser.
	 */
	private void buildGUI() {
		final Container contentPane = getContentPane();
		
		
		final JComponent canvasComponent = new JComponent() {
			@Override
			public void paintComponent( final Graphics graphics ) {
				graphics.setColor( new Color( 50, 50, 50 ) );
				graphics.fillRect( 0, 0, getWidth(), getHeight() );
				graphics.setColor( Color.WHITE );
				
				final Algorithm selectedAlgorithm = (Algorithm) algorithmComboBox.getSelectedItem();
				try {
					selectedAlgorithm.paint( graphics, getWidth(), getHeight() );
				}
				catch ( final IllegalArgumentException ie ) {
					// TODO: handle this (warn user)
				}
			}
		};
		contentPane.add( canvasComponent, BorderLayout.CENTER );
		
		final Box controlBox = Box.createVerticalBox();
		controlBox.add( createLabelForBox( "Choose an algorithm:" ) );
		controlBox.add( algorithmComboBox );
		
		controlBox.add( createLabelForBox( "Algorithm description:" ) );
		final JTextArea descriptionTextArea = new JTextArea();
		descriptionTextArea.setRows( 7 );
		descriptionTextArea.setWrapStyleWord( true );
		descriptionTextArea.setLineWrap( true );
		controlBox.add( new JScrollPane( descriptionTextArea ) );
		
		controlBox.add( createLabelForBox( "Algorithm properties:" ) );
		propertiesTextArea.setRows( 7 );
		controlBox.add( new JScrollPane( propertiesTextArea ) );
		
		algorithmComboBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent ae ) {
				final Algorithm selectedAlgorithm = (Algorithm) algorithmComboBox.getSelectedItem();
				descriptionTextArea.setText( selectedAlgorithm.getDescription() );
				updatePropertiesTextArea();
			}
		} );
		algorithmComboBox.setSelectedIndex( 0 );
		
		final JPanel buttonsPanel = new JPanel();
		final JButton drawButton = new JButton( "Draw" );
		drawButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent ae ) {
				// First copy the optionally modified properties
				final Algorithm selectedAlgorithm = (Algorithm) algorithmComboBox.getSelectedItem();
				
				final Properties modifiedProperties = new Properties();
				try {
					modifiedProperties.load( new StringReader( propertiesTextArea.getText() ) );
				} catch ( final IOException ie ) {
					// This will never be thrown
				}
				final Properties algorithmProperties = selectedAlgorithm.getProperties();
				for ( final String propertyName : modifiedProperties.stringPropertyNames() )
					algorithmProperties.setProperty( propertyName, modifiedProperties.getProperty( propertyName ) );
				
				// And then initiate a repaint
				canvasComponent.repaint();
			}
		} );
		buttonsPanel.add( drawButton );
		
		final JButton restoreDefaultPropertiesButton = new JButton( "Restore defaults" );
		restoreDefaultPropertiesButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent ae ) {
				( (Algorithm) algorithmComboBox.getSelectedItem() ).restoreDefaultProperties();
				updatePropertiesTextArea();
			}
		} );
		buttonsPanel.add( restoreDefaultPropertiesButton );
		
		controlBox.add( buttonsPanel );
		
		final JPanel wrapperPanel = new JPanel();
		wrapperPanel.add( controlBox );
		contentPane.add( wrapperPanel, BorderLayout.EAST );
	}
	
	/**
	 * Creates and returns a label with the specified text to be used in a {@link javax.swing.Box}.<br>
	 * The returned label is center-aligned.
	 * 
	 * @param text text of the createable label
	 * @return a center-aligned label with the specified text
	 */
	private static JLabel createLabelForBox( final String text ) {
		final JLabel label = new JLabel( text );
		label.setAlignmentX( CENTER_ALIGNMENT );
		return label;
	}
	
	/**
	 * Updates the properties text area with the properties of the selected algorithm. 
	 */
	private void updatePropertiesTextArea() {
		final Algorithm    selectedAlgorithm      = (Algorithm) algorithmComboBox.getSelectedItem();
		final StringWriter propertiesStringWriter = new StringWriter();
		selectedAlgorithm.getProperties().list( new PrintWriter( propertiesStringWriter ) );
		
		propertiesTextArea.setText( propertiesStringWriter.toString() );
	}
	
	/**
	 * Logs a message.
	 * @param message message to be logged
	 */
	private static void logMessage( final String message ) {
		System.out.println( message );
	}
	
}
