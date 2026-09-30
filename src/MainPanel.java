//Nathan Armstrong 2026

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.awt.image.BufferedImage;

/*
    This class is where the bulk of the functionality
    of the program happens. It is the panel used for
    the conformal mapper portion of the program. This
    is the defualt panel the MainFrame object uses on
    startup.
*/

public class MainPanel extends JPanel implements Runnable
{
    //These colours are used for drawing GUI elements
    private Color subPanelColor = new Color(0x2b2b2b);
    private Color bgr = new Color(0x333333);

    //Used to register GUI components
    private GUIRegister register = new GUIRegister();

    //Labels
    private JLabel functionLabel;
    private JLabel ALabel;
    private JLabel BLabel;
    private JLabel nLabel;
    private JLabel inputSettingsLabel;
    private JLabel shapeLabel;
    private JLabel shapeSliderMinLabel;
    private JLabel shapeSliderMaxLabel;
    private JLabel numberOfPointsLabel;
    private JLabel shapeCentreLabel;
    private JLabel xLabel;
    private JLabel yLabel;
    private JLabel circleRadiusLabel;
    private JLabel gridScaleLabel;
    private JLabel axisSettingsLabel;
    private JLabel hueSliderLabel;
    private JLabel colourRangeSLiderLabel;

    //Buttons
    private JButton resetFunctionButton;
    private JButton resetShapeButton;
    private JButton resetAxisSettingsButton;
    private JButton sendAxisToOriginButton;

    //Combo boxes
    private String[] functionList = {"Exponetial", "Power", "sin","cos","tan","Logarithmic","Joukowsky","Simple Fraction"};
    private JComboBox<String> functionComboBox;

    private String[] shapeList = {"Circle", "Square Grid", "Image"};
    private JComboBox<String> shapeComboBox;

    //Spinners
    private JSpinner ASpinner;
    private JSpinner BSpinner;
    private JSpinner nSpinner;
    private JSpinner circleRadiusSpinner;
    private JSpinner gridScaleSpinner;
    private JSpinner xSpinner;
    private JSpinner ySpinner;
    private JSpinner pixelSizeSpinner;

    //Sliders
    private JSlider pointSlider;
    private JSlider colourRangeSlider;
    private JSlider hueSlider;

    //Image urls for equations
    private URL expUrl = MainFrame.class.getResource("/resources/exp.png");
    private URL powUrl = MainFrame.class.getResource("/resources/pow.png");
    private URL sinUrl = MainFrame.class.getResource("/resources/sin.png");
    private URL cosUrl = MainFrame.class.getResource("/resources/cos.png");
    private URL tanUrl = MainFrame.class.getResource("/resources/tan.png");
    private URL lnUrl = MainFrame.class.getResource("/resources/ln.png");
    private URL joukowskyUrl = MainFrame.class.getResource("/resources/joukowsky.png");
    private URL simpleFractionUrl = MainFrame.class.getResource("/resources/simpleFraction.png");
    private URL imageUrl = MainFrame.class.getResource("/resources/image.png");

    //Scaled images from image urls
    private Image expFormula = new ImageIcon(new ImageIcon(expUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();
    private Image powFormula = new ImageIcon(new ImageIcon(powUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();
    private Image sinFormula = new ImageIcon(new ImageIcon(sinUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();
    private Image cosFormula = new ImageIcon(new ImageIcon(cosUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();
    private Image tanFormula = new ImageIcon(new ImageIcon(tanUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();
    private Image lnFormula = new ImageIcon(new ImageIcon(lnUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();
    private Image joukowskyFormula = new ImageIcon(new ImageIcon(joukowskyUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();
    private Image simpleFractionFormula = new ImageIcon(new ImageIcon(simpleFractionUrl).getImage().getScaledInstance(194, 55, Image.SCALE_SMOOTH)).getImage();

    //Image array of all formulas
    private Image[] functionGraphicList = {expFormula, powFormula, sinFormula, cosFormula, tanFormula, lnFormula, joukowskyFormula, simpleFractionFormula};

    //The two sets of axis for the unmapped (baseAxis) and mapped (mapAxis) points
    private Axis baseAxis;
    private Axis mapAxis;

    //Runtime variables
    private Thread mainThread;
	private Image image;
	private Graphics graphics;

    //Plotting point arrays and variables
    public static int n = 2000;
    private Complex[] points = new Complex[100000]; //unmapped points array
    private double r = 1; //circle radius
    private double cx = 0; //shape centre x
    private double cy = 0; //shape centre y
    private double gridScale = 0.5; //grid scaling factor

    private Complex[] pointsM = new Complex[100000]; //mapped points array

    private MapLibrary map = new MapLibrary(); //map library used to access complex valued functions

    private boolean gridDraggable = false; //used to check if the user should be allowed to drag the axis
    private boolean pointDraggable = false; //used to check if the user should be allowed to drag the points
    private int dragx = 0; //grid drag x
    private int dragy = 0; //grid drag y
    private int pmx = 0; //previous mouse x
    private int cmx = 0; //current mouse x
    private int pmy = 0; //previous mouse y
    private int cmy = 0; //current mouse y
    private double zoom = 0.2; //axis zoom

    //Variables used to map an image
    public static int imageN = 0; //number of pixels in image
    private Complex[] pointsImage; //point array for image pixel co-ordinates
    private static Color[] colorImage; //colour array for image pixel colours

    public MainPanel()
    {
        //Panel settings
        this.setPreferredSize(new Dimension(1200, 800));
        this.setBounds(0, 0, 1200, 800);
        this.setBackground(new Color(0x333333));
        this.setOpaque(false);
        this.setLayout(null);

        //Panel Listeners
        this.addMouseListener(new MouseController());
		this.addMouseMotionListener(new MouseController());
        this.addMouseWheelListener(new MouseWheelController());

        //Try to load in image for image mapping
        try
        {
            BufferedImage image = ImageIO.read(imageUrl);

            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            imageN = imageWidth*imageHeight;
            pointsImage = new Complex[imageN];
            colorImage = new Color[imageN];

            int index=0;
            for (int py=0; py<imageHeight; py++)
            {
                for (int px=0; px<imageWidth; px++)
                {
                    //bitwise operators to extract rgb values from getRGB function
                    int rgb=image.getRGB(px,py);
                    int r=(rgb>>16)&0xFF;
                    int g=(rgb>>8)&0xFF;
                    int b=rgb&0xFF;

                    pointsImage[index] = new Complex(((px-imageWidth/(double)2)/156), ((-py+imageHeight/(double)2))/156);
                    colorImage[index] = new Color(r, g, b);
                    index++;
                }
            }

        } catch (Exception e)
        {
            System.out.println(e);
        }

        //set the image pixel colour array for the axis to render later
        Axis.colorImage = colorImage;

        //Register and add GUI components
        register();
        this.add(functionLabel);
        this.add(ALabel);
        this.add(BLabel);
        this.add(nLabel);
        this.add(inputSettingsLabel);
        this.add(shapeLabel);
        this.add(shapeSliderMinLabel);
        this.add(shapeSliderMaxLabel);
        this.add(numberOfPointsLabel);
        this.add(shapeCentreLabel);
        this.add(xLabel);
        this.add(yLabel);
        this.add(circleRadiusLabel);
        this.add(gridScaleLabel);
        this.add(axisSettingsLabel);
        this.add(colourRangeSLiderLabel);
        this.add(hueSliderLabel);

        this.add(functionComboBox);
        this.add(shapeComboBox);

        this.add(ASpinner);
        this.add(BSpinner);
        this.add(nSpinner);
        this.add(xSpinner);
        this.add(ySpinner);
        this.add(circleRadiusSpinner);
        this.add(gridScaleSpinner);
        this.add(pixelSizeSpinner);

        this.add(resetFunctionButton);
        this.add(resetShapeButton);
        this.add(resetAxisSettingsButton);
        this.add(sendAxisToOriginButton);

        this.add(pointSlider);
        this.add(colourRangeSlider);
        this.add(hueSlider);

        baseAxis = new Axis(10, 10, 400, 400);
        mapAxis = new Axis(420, 10, 400, 400);

        //set the unmapped points to a circle
        for (int i = 0; i < n; i++)
        {
            points[i] = new Complex(r*Math.cos((2*Math.PI*i)/(n))+cx, r*Math.sin((2*Math.PI*i)/(n))+cy);
        }

        baseAxis.setPoints(points);
        mapAxis.setPoints(pointsM);

        colourRangeSlider.setValue(100);

        resetFunction();
        updateShapeGUI();

        resetFunctionButton.requestFocus();
        this.validate();

        mainThread = new Thread(this);
		mainThread.start();
    }

    private void register()
    {
        //labels
        functionLabel = new JLabel();
        functionLabel = register.label(functionLabel, "Function Settings", 950, 15, 130, 20);

        ALabel = new JLabel();
        ALabel = register.label(ALabel, "A", 840, 110, 20, 30);

        BLabel = new JLabel();
        BLabel = register.label(BLabel, "B", 950, 110, 20, 30);

        nLabel = new JLabel();
        nLabel = register.label(nLabel, "n", 1060, 110, 20, 30);

        inputSettingsLabel = new JLabel();
        inputSettingsLabel = register.label(inputSettingsLabel, "Input Settings", 960, 215, 130, 20);

        shapeLabel = new JLabel();
        shapeLabel = register.label(shapeLabel, "Shape", 885, 245, 80, 20);

        shapeSliderMinLabel = new JLabel();
        shapeSliderMinLabel = register.label(shapeSliderMinLabel, "10", 975, 275, 20, 20);

        shapeSliderMaxLabel = new JLabel();
        shapeSliderMaxLabel = register.label(shapeSliderMaxLabel, "10E4", 1135, 275, 40, 20);

        numberOfPointsLabel = new JLabel();
        numberOfPointsLabel = register.label(numberOfPointsLabel, "Number of Points", 1007, 245, 130, 20);

        shapeCentreLabel = new JLabel();
        shapeCentreLabel = register.label(shapeCentreLabel, "Shape Centre", 840, 320, 100, 20);

        xLabel = new JLabel();
        xLabel = register.label(xLabel, "Re", 945, 320, 20, 20);

        yLabel = new JLabel();
        yLabel = register.label(yLabel, "Im", 1060, 320, 20, 20);

        circleRadiusLabel = new JLabel();
        circleRadiusLabel = register.label(circleRadiusLabel, "Radius", 840, 360, 50, 20);

        gridScaleLabel = new JLabel();
        gridScaleLabel = register.label(gridScaleLabel, "Scale", 840, 360, 50, 20);

        axisSettingsLabel = new JLabel();
        axisSettingsLabel = register.label(axisSettingsLabel, "Axis Settings", 600, 425, 200, 20);

        colourRangeSLiderLabel = new JLabel();
        colourRangeSLiderLabel = register.label(colourRangeSLiderLabel, "Colour Range:", 20, 445, 200 , 20);

        hueSliderLabel = new JLabel();
        hueSliderLabel = register.label(hueSliderLabel, "Hue Shift:", 275,445,200,20);

        //combo boxes
        functionComboBox = new JComboBox<String>();
        functionComboBox = register.comboBox(functionComboBox, 840, 60, 130, 30);
        for (int i = 0; i < functionList.length; i++)
        {
            functionComboBox.addItem(functionList[i]);
        }
        functionComboBox.addActionListener(e -> updateFunction());

        shapeComboBox = new JComboBox<String>();
        shapeComboBox = register.comboBox(shapeComboBox, 840, 270, 130, 30);
        for (int i = 0; i < shapeList.length; i++)
        {
            shapeComboBox.addItem(shapeList[i]);
        }
        shapeComboBox.addActionListener(e->updateShapeGUI());

        //spinners
        ASpinner = new JSpinner();
        ASpinner = register.spinner(ASpinner, 855, 110, 80, 30);
        ASpinner.setValue(1);
        ASpinner.addChangeListener(e -> updateFunction());

        BSpinner = new JSpinner();
        BSpinner = register.spinner(BSpinner, 965, 110, 80, 30);
        BSpinner.setValue(1);
        BSpinner.addChangeListener(e -> updateFunction());

        nSpinner = new JSpinner();
        nSpinner = register.spinner(nSpinner, 1075, 110, 80, 30);
        nSpinner.setValue(1);
        nSpinner.addChangeListener(e -> updateFunction());

        xSpinner = new JSpinner();
        xSpinner = register.spinner(xSpinner, 970, 315, 80, 30);
        xSpinner.setValue(0);
        xSpinner.addChangeListener(e->updateShape());
        
        ySpinner = new JSpinner();
        ySpinner = register.spinner(ySpinner, 1080, 315, 80, 30);
        ySpinner.setValue(0);
        ySpinner.addChangeListener(e->updateShape());
        
        circleRadiusSpinner = new JSpinner();
        circleRadiusSpinner = register.spinner(circleRadiusSpinner, 890, 355, 100, 30);
        circleRadiusSpinner.setValue(1);
        circleRadiusSpinner.addChangeListener(e->updateShape());

        gridScaleSpinner = new JSpinner();
        gridScaleSpinner = register.spinner(gridScaleSpinner, 880, 355, 100, 30);
        gridScaleSpinner.setValue(1);
        gridScaleSpinner.addChangeListener(e->updateShape());

        pixelSizeSpinner = new JSpinner();
        pixelSizeSpinner.setBounds(880, 355, 100, 30);
        pixelSizeSpinner.setFocusable(false);
        pixelSizeSpinner.setModel(new SpinnerNumberModel(1, 1, null, 1));
        pixelSizeSpinner.setValue(1);
        pixelSizeSpinner.addChangeListener(e->Axis.pixelSize=((Number) pixelSizeSpinner.getValue()).intValue());

        //buttons
        resetFunctionButton = new JButton();
        resetFunctionButton = register.button(resetFunctionButton, "reset", 1060, 160, 100, 25);
        resetFunctionButton.addActionListener(e -> resetFunction());

        resetShapeButton = new JButton();
        resetShapeButton = register.button(resetShapeButton, "reset", 1060, 370, 100, 25);
        resetShapeButton.addActionListener(e->resetShape());

        resetAxisSettingsButton = new JButton();
        resetAxisSettingsButton = register.button(resetAxisSettingsButton, "reset", 1060, 438,100,25);
        resetAxisSettingsButton.addActionListener(e->resetAxis());

        sendAxisToOriginButton = new JButton();
        sendAxisToOriginButton = register.button(sendAxisToOriginButton, "centre",950,438,100,25);
        sendAxisToOriginButton.addActionListener(e->sendAxisToOrigin());

        //sliders
        pointSlider = new JSlider();
        pointSlider = register.slider(pointSlider,10, 10000, 2000, 990, 272, 145, 30);
        pointSlider.addChangeListener(e->updateShape());

        colourRangeSlider = new JSlider();
        colourRangeSlider = register.slider(colourRangeSlider, 0, 600, 600, 110, 442, 145, 30);
        colourRangeSlider.addChangeListener(e->setColourRange(colourRangeSlider.getValue()/(double)100));

        hueSlider = new JSlider();
        hueSlider = register.slider(hueSlider, 0, 6*255, 0, 335, 442, 145, 30);
        hueSlider.addChangeListener(e->setHue(hueSlider.getValue()));
    } //register gui components

    private void resetAxis()
    {
        hueSlider.setValue(0);
        colourRangeSlider.setValue(100);
    } //reset axis settings

    private void sendAxisToOrigin()
    {
        dragx = 0;
        dragy = 0;
        pmx = 0;
        cmx = 0;
        pmy = 0;
        cmy = 0;
        zoom = 0.2;

        setAxisDisplacement(dragx, dragy);
        setAxisZoom(zoom);
    } //sends the axis to the default state

    private void updateShapeGUI()
    {
        circleRadiusLabel.setVisible(false);
        gridScaleLabel.setVisible(false);

        circleRadiusSpinner.setVisible(false);
        gridScaleSpinner.setVisible(false);
        pixelSizeSpinner.setVisible(false);

        baseAxis.setImageMode(false);
        mapAxis.setImageMode(false);

        switch (shapeComboBox.getSelectedIndex())
        {
            //Circle Shape
            case 0:
                circleRadiusSpinner.setVisible(true);
                circleRadiusLabel.setVisible(true);
                break;
        
            //Grid Shape
            case 1:
                gridScaleSpinner.setVisible(true);
                gridScaleLabel.setVisible(true);
                break;

            //Image
            case 2:
                baseAxis.setImageMode(true);
                mapAxis.setImageMode(true);
                pixelSizeSpinner.setVisible(true);
                gridScaleLabel.setVisible(true);
                break;
            default:
                break;
        }
        updateShape();
    } //updates the input shape GUI

    private void updateShape()
    {
        n = pointSlider.getValue();
        cx = ((Number) xSpinner.getValue()).doubleValue();
        cy = ((Number) ySpinner.getValue()).doubleValue();
        r = ((Number) circleRadiusSpinner.getValue()).doubleValue();
        gridScale = ((Number) gridScaleSpinner.getValue()).doubleValue();


        switch (shapeComboBox.getSelectedIndex())
        {
            //Circle shape
            case 0:
                for (int i = 0; i < n; i++)
                {
                    points[i] = new Complex(r*Math.cos((2*Math.PI*i)/(n))+cx, r*Math.sin((2*Math.PI*i)/(n))+cy);
                }
                break;
            
            //Grid shape
            case 1:
                for (int i = 0; i < n; i++)
                {
                    points[i] = new Complex(cx, cy);
                }

                double gScale = gridScale/2.5;
                for (int i=0; i<n/10; i++)
                {
                    double x =-2.5+i*5.0/(n/10-1);
                    for (int j=0; j<5; j++)
                    {
                        double y = -2.5+j*5.0/4.0;
                        points[i+j*n/10]=new Complex((x+cx/gScale)*gScale,(y+cy/gScale)*gScale);
                        points[n/2+i+j*n/10]=new Complex((y+cx/gScale)*gScale,(x+cy/gScale)*gScale);
                    }
                }
                break;

                //Image
                case 2:
                    n=imageN;
                    for (int i = 0; i<pointsImage.length;i++)
                    {
                        points[i]=new Complex(Complex.re(pointsImage[i])+cx, Complex.im(pointsImage[i])+cy);
                    }
                    break;
            
            default:
                break;
        }
        updateFunction();
    } //updates the input shape type

    private void resetShape()
    {
        shapeComboBox.setSelectedIndex(0);
        xSpinner.setValue(0);
        ySpinner.setValue(0);
        circleRadiusSpinner.setValue(1);
        gridScaleSpinner.setValue(1);
        pixelSizeSpinner.setValue(1);
        pointSlider.setValue(2000);
    } //resets all shape related variables

    private void updateFunction()
    {
        double A = ((Number) ASpinner.getValue()).doubleValue();
        double B = ((Number) BSpinner.getValue()).doubleValue();
        double n = ((Number) nSpinner.getValue()).doubleValue();

        switch (functionComboBox.getSelectedIndex())
        {
            case 0:
                pointsM = map.exp(points, A, B);
                break;
            case 1:
                pointsM = map.pow(points, A, n);
                break;
            case 2:
                pointsM = map.sin(points, A, B, n);
                break;
            case 3:
                pointsM = map.cos(points, A, B, n);
                break;
            case 4:
                pointsM = map.tan(points, A, B, n);
                break;
            case 5:
                pointsM = map.ln(points, A, B);
                break;
            case 6:
                pointsM = map.Joukowsky(points);
                break;
            case 7:
                pointsM = map.simpleFraction(points, 1, A, B);
                break;
            
            default:
                break;
        }

        baseAxis.setPoints(points);
        mapAxis.setPoints(pointsM);
    } //sets the function type that maps the input points

    private void resetFunction()
    {
        functionComboBox.setSelectedIndex(1);
        ASpinner.setValue(1);
        BSpinner.setValue(1);
        nSpinner.setValue(1);
    } //resets all function related variables

    private void setPointDisplacement(double x, double y)
    {
        xSpinner.setValue(((Number) xSpinner.getValue()).doubleValue()+x);
        ySpinner.setValue(((Number) ySpinner.getValue()).doubleValue()+y);
    } //sets the displacement of the input points

    private void setAxisDisplacement(int x, int y)
    {
        baseAxis.setAxisDisplacement(x, y);
        mapAxis.setAxisDisplacement(x, y);
    } //sets the displacement of the axis

    private void setAxisZoom(double zoom)
    {
        baseAxis.setAxisZoom(zoom);
        mapAxis.setAxisZoom(zoom);
    } //sets the axis zoom

    private void setColourRange(double range)
    {
        baseAxis.setColourRange(range);
        mapAxis.setColourRange(range);
    } //sets the colour range of the axis

    private void setHue(int hue)
    {
        baseAxis.setHue(hue);
        mapAxis.setHue(hue);
    } //sets the hue shift of the axis

    private void draw(Graphics g)
	{
        Graphics2D g2d = (Graphics2D)g;

        //draw background
        g2d.setColor(bgr);
        g2d.fillRect(0, 0, 1200, 800);
        
        //draw axis
        baseAxis.draw(g);
        mapAxis.draw(g);

        //clean up area surrounding axis
        g2d.setColor(bgr);
        g2d.fillRect(0,0,getWidth(),10);
        g2d.fillRect(410,0,10,getHeight());
        g2d.fillRect(820,0,getWidth()-820,getHeight());

        //draw sub panels
        g2d.setColor(subPanelColor);
        g2d.fillRect(830, 10, 345, 190);
        g2d.fillRect(830, 210, 345, 200);
        g2d.fillRect(10, 420, 1165, 60);

        //paint the GUI components
        super.paint(g);
	}

	public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g2d.drawImage(image, 0, 0, this);
        g2d.drawImage(functionGraphicList[functionComboBox.getSelectedIndex()], 975, 45, this);
    } //panel paint method

	public void run()
	{
		long lastTime = System.nanoTime();
		double amountOfTicks = 1000000;
		double ns = 1000000000 / amountOfTicks;
		int delta = 0;

		while (true)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1)
			{
				repaint();
				delta--;
			}
		}
	} //run function with delta time loop

    private class MouseController extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{   
            if ((e.getX()>=10 && e.getX()<=410 && e.getY()>=10 && e.getY()<=410)||(e.getX()>=420 && e.getX()<=820 && e.getY()>=10 && e.getY()<=410))
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    gridDraggable = true;
                }

                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    pointDraggable = true;
                }

                pmx = e.getX();
                pmy = e.getY();
            } //check if the mouse is over one of the axis, if yes and the mouse is clicked, the axis may be dragged
		}

		public void mouseReleased(MouseEvent e)
		{
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                gridDraggable = false;
            }

            if (e.getButton() == MouseEvent.BUTTON3)
            {
                pointDraggable = false;
            }
		}

		public void mouseDragged(MouseEvent e)
		{
            cmx = e.getX();
            cmy = e.getY();

            if (gridDraggable)
            {
                dragx += cmx-pmx;
                dragy += cmy-pmy;
                setAxisDisplacement(dragx, dragy);
            }

            if (pointDraggable)
            {
                setPointDisplacement((cmx-pmx)/(zoom*baseAxis.getWidth()), (-cmy+pmy)/(zoom*baseAxis.getHeight()));
            }

            pmx = cmx;
            pmy = cmy;
		} //controls axis/point displacemenet
	} //Class to handle mouse input such as clicking and dragging

    private class MouseWheelController implements MouseWheelListener
    {
        /*
            The purpose of this class is to
            allow the panel to listen for mouse
            scrolling input, and adjust the axis
            zoom accourdingly. We dont just increase/
            decrease the zoom, but also change the
            grid displacement depending the position
            of the cursor when zooming occurs. This
            is to ensure that when the user zooms,
            the "focus" of where they zoom is the
            position of the curson on the axis.
        */

        public void mouseWheelMoved(MouseWheelEvent e)
        {
            if ((e.getX()>=10 && e.getX()<=410 && e.getY()>=10 && e.getY()<=410))
            {
                double oldZoom = zoom;

                zoom -= (double)e.getWheelRotation()*zoom/10;
                if (zoom < 0)
                {
                    zoom = oldZoom;
                }

                double mouseX = e.getX();
                double mouseY = e.getY();

                if (Math.abs((mouseX-baseAxis.getWidth()/2-baseAxis.getX())-((mouseX-baseAxis.getWidth()/2-baseAxis.getX())-dragx)*(zoom/oldZoom))<Integer.MAX_VALUE && Math.abs((mouseY-baseAxis.getHeight()/2-baseAxis.getY())-((mouseY-baseAxis.getHeight()/2-baseAxis.getY())-dragy)*(zoom/oldZoom))<Integer.MAX_VALUE)
                {
                    dragx = (int)((mouseX-baseAxis.getWidth()/2-baseAxis.getX())-((mouseX-baseAxis.getWidth()/2-baseAxis.getX())-dragx)*(zoom/oldZoom));
                    dragy = (int)((mouseY-baseAxis.getHeight()/2-baseAxis.getY())-((mouseY-baseAxis.getHeight()/2-baseAxis.getY())-dragy)*(zoom/oldZoom));
                }
                else
                {
                    zoom=oldZoom;
                }

                setAxisZoom(zoom);
                setAxisDisplacement(dragx, dragy);
            } //if the cursor is hovering over the baseAxis when zooming occurs

            if ((e.getX()>=420 && e.getX()<=820 && e.getY()>=10 && e.getY()<=410))
            {
                //double pz = zoom;
                double oldZoom = zoom;

                zoom -= (double)e.getWheelRotation()*zoom/10;
                if (zoom < 0)
                {
                    zoom = oldZoom;
                }

                double mouseX = e.getX();
                double mouseY = e.getY();

                if (Math.abs((mouseX-mapAxis.getWidth()/2-mapAxis.getX())-((mouseX-mapAxis.getWidth()/2-mapAxis.getX())-dragx)*(zoom/oldZoom))<Integer.MAX_VALUE && Math.abs((mouseY-mapAxis.getHeight()/2-mapAxis.getY())-((mouseY-mapAxis.getHeight()/2-mapAxis.getY())-dragy)*(zoom/oldZoom))<Integer.MAX_VALUE)
                {
                    dragx = (int)((mouseX-mapAxis.getWidth()/2-mapAxis.getX())-((mouseX-mapAxis.getWidth()/2-mapAxis.getX())-dragx)*(zoom/oldZoom));
                    dragy = (int)((mouseY-mapAxis.getHeight()/2-mapAxis.getY())-((mouseY-mapAxis.getHeight()/2-mapAxis.getY())-dragy)*(zoom/oldZoom));
                }
                else
                {
                    zoom=oldZoom;
                }

                setAxisZoom(zoom);
                setAxisDisplacement(dragx, dragy);
            } //if the cursor is hovering over the mapAxis when zooming occurs
        }
    }//Class to handle mouse wheel input
}
