//Nathan Armstrong 2026

import javax.swing.*;
import java.awt.*;
import java.net.*; //used for image handeling

/*
    This class is used to build the application
    window that the panels will be rendered in.
*/

public class MainFrame extends JFrame
{
    private MainPanel mainPanel;
    private AirfoilPanel airfoilPanel;

    private JMenuBar menuBar;
	private JMenu viewMenu;

    private JMenuItem conformalMapper;
	private JMenuItem airfoilSimulation;

    public MainFrame()
    {
        SplashWindow splashWindow = new SplashWindow();
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(1200, 537));
		this.setLocationRelativeTo(null);

        URL iconUrl = MainFrame.class.getResource("/resources/icon.png");
        this.setIconImage(new ImageIcon(iconUrl).getImage());
		this.setTitle("Conformal Mapper "+ConformalMapper.version);
        this.setResizable(false);

        menuBar = new JMenuBar();
		viewMenu = new JMenu("View");

        conformalMapper = new JMenuItem("Conformal Mapper");
        conformalMapper.addActionListener(e->viewConformalMapper());

        airfoilSimulation = new JMenuItem("Airfoil Simulation");
        airfoilSimulation.addActionListener(e->viewAirfoilSimulation());

        viewMenu.add(conformalMapper);
        viewMenu.add(airfoilSimulation);

        menuBar.add(viewMenu);
        this.setJMenuBar(menuBar);

        mainPanel = new MainPanel();
        this.add(mainPanel);

        airfoilPanel = new AirfoilPanel();

        splashWindow.dispose();
        this.setVisible(true);

        this.getContentPane().requestFocusInWindow();
    } //create new panel

    private void viewConformalMapper()
    {
        this.remove(airfoilPanel);
        this.add(mainPanel);
    } //set the current panel to be a MainPanel object

    private void viewAirfoilSimulation()
    {
        this.remove(mainPanel);
        this.add(airfoilPanel);
    } //set the current panel to be a AirfoilPanel object
}
