import javax.swing.*;
import java.awt.*;

/*
    This class is responsible for the
    airfoil simulation portion of the
    program.
*/

public class AirfoilPanel extends JPanel implements Runnable
{
    //These colours are used for drawing GUI elements
    private Color subPanelColor = new Color(0x2b2b2b);
    private Color bgr = new Color(0x333333);
    private boolean vColor = false; 

    //Weird button variables
    private int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private boolean weird = false;
    private double weirdTimer = screenWidth;

    //Runtime variables
    private Thread mainThread;
	private Image image;
	private Graphics graphics;

    //particle and rendering arrays
    private Complex[] particles = new Complex[1500]; //particles objects stored as a set of co-ordinates
    private Color[] pColour = new Color[particles.length]; //array that tracks what colour each particle is
    private Complex[] points = new Complex[1000]; //points used to map a circle to the airfoil shape
    private Complex [] airfoil = new Complex[1000]; //points storing the airfoil shape

    private double x0 = -0.25; //centre x of circle
    private double y0 = 0; //centre y of circle
    private Complex z0 = new Complex(x0, y0); //complex valued centre of circle
    private double V0 = 0.006; //Initial fluid velocity/ velocity far away from the airfoil
    private double b = 1; //paramater used to slightly change the circle radius
    private double R = Math.sqrt(Math.pow((x0-b),2)+Math.pow((y0),2)); //circle radius

    private GUIRegister register = new GUIRegister();

    //labels
    private JLabel settingsLabel;
    private JLabel x0Label;
    private JLabel y0Label;
    private JLabel V0Label;
    private JLabel bLabel;

    //sliders
    private JSlider x0Slider;
    private JSlider y0Slider;
    private JSlider V0Slider;
    private JSlider bSlider;

    //buttons
    private JButton resetButton;
    private JButton weirdButton;

    //check boxes
    private JCheckBox vColorCheckBox;

    public AirfoilPanel()
    {
        //panels settings
        this.setPreferredSize(new Dimension(1200, 800));
        this.setBounds(0, 0, 1200, 800);
        this.setBackground(new Color(0x333333));
        this.setOpaque(false);
        this.setLayout(null);

        //assign all particles a random position and colour
        for (int i = 0; i<particles.length; i++)
        {
            particles[i] = new Complex(Math.random()*8.10-8.10/2, Math.random()*4.70-4.70/2);
            pColour[i]= new Color((int)(50+Math.random()*205),(int)(Math.random()*255),(int)(200+Math.random()*55),200);
        }

        //create circle shape to be mapped
        for (int i = 0; i < 1000; i++)
        {
            points[i] = new Complex(R*Math.cos((2*Math.PI*i)/(1000))+x0, R*Math.sin((2*Math.PI*i)/(1000))+y0);
        }

        //map the circle through a joukowsky mapping
        for (int i = 0; i < 1000; i++)
        {
            airfoil[i] = Complex.sum(points[i], Complex.divide(new Complex(1, 0), points[i]));
        }

        //register and add gui components
        register();
        this.add(settingsLabel);
        this.add(x0Label);
        this.add(y0Label);
        this.add(V0Label);
        this.add(bLabel);

        this.add(x0Slider);
        this.add(y0Slider);
        this.add(V0Slider);
        this.add(bSlider);

        this.add(resetButton);
        this.add(weirdButton);

        this.add(vColorCheckBox);

        updateParams();

        mainThread = new Thread(this);
		mainThread.start();
    }

    private void register()
    {
        settingsLabel = new JLabel();
        settingsLabel = register.label(settingsLabel, "Settings", 980, 20, 100, 20);

        x0Label = new JLabel();
        x0Label = register.label(x0Label, "x0:", 890, 103, 100, 20);

        y0Label = new JLabel();
        y0Label = register.label(y0Label, "y0:", 890, 143, 100, 20);

        V0Label = new JLabel();
        V0Label = register.label(V0Label, "V0:", 890, 183, 100, 20);

        bLabel = new JLabel();
        bLabel = register.label(bLabel, "b:", 890, 223, 100, 20);

        x0Slider = new JSlider();
        x0Slider = register.slider(x0Slider, -100, 0, -50, 910, 100, 200, 30);
        x0Slider.addChangeListener(e->updateParams());

        y0Slider = new JSlider();
        y0Slider = register.slider(y0Slider, 0, 3*Math.abs(x0Slider.getValue()), 0, 910, 140, 200, 30);
        y0Slider.addChangeListener(e->updateParams());

        V0Slider = new JSlider();
        V0Slider = register.slider(V0Slider, 0, 50, 15, 910, 180, 200, 30);
        V0Slider.addChangeListener(e->V0=V0Slider.getValue()/(double)1000);
        V0=V0Slider.getValue()/(double)1000;

        bSlider = new JSlider();
        bSlider = register.slider(bSlider, 100, 200, 100, 910, 220, 200, 30);
        bSlider.addChangeListener(e->updateParams());

        resetButton = new JButton();
        resetButton = register.button(resetButton, "reset", 960, 300, 100, 25);
        resetButton.addActionListener(e->resetParams());

        weirdButton = new JButton();
        weirdButton = register.button(weirdButton, "Weird Button", 910, 445, 200, 25);
        weirdButton.addActionListener(e->weirdButton());

        vColorCheckBox = new JCheckBox();
        vColorCheckBox = register.checkBox(vColorCheckBox, "Velocity Colouring", 935, 255, 200, 30);
        vColorCheckBox.addActionListener(e->vColor=!vColor);
    } //register gui components

    private void weirdButton()
    {
        weird=true;
        V0Slider.setValue(V0<0.1?100:V0Slider.getValue());
    } //weird button press action

    private void resetParams()
    {
        x0Slider = register.slider(x0Slider, -100, 0, -50, 910, 100, 200, 30);
        y0Slider = register.slider(y0Slider, 0, Math.abs(x0Slider.getValue()), 0, 910, 140, 200, 30);
        V0Slider = register.slider(V0Slider, 0, 50, 15, 910, 180, 200, 30);
        bSlider = register.slider(bSlider, 100, 200, 100, 910, 220, 200, 30);
        vColor = false;
        vColorCheckBox.setSelected(false);
        updateParams();
    } //resets all input parameters

    private void updateParams()
    {
        //the values that y0 can take are dependant on the value of x0, thus we check y0 and x0 and adjust the y0 slider accourdingly
        if (y0Slider.getValue()>3*Math.abs(x0Slider.getValue()))
        {
            y0Slider = register.slider(y0Slider, 0, 3*Math.abs(x0Slider.getValue()), 3*Math.abs(x0Slider.getValue()), 910, 140, 200, 30);
        }
        else
        {
            y0Slider = register.slider(y0Slider, 0, 3*Math.abs(x0Slider.getValue()), y0Slider.getValue(), 910, 140, 200, 30);
        }

        x0 = x0Slider.getValue()/(double)100;
        y0 = y0Slider.getValue()/(double)100;
        b = bSlider.getValue()/(double)100;
        R = Math.sqrt(Math.pow((x0-b),2)+Math.pow((y0),2));
        z0 = new Complex(x0, y0);

        for (int i = 0; i < 1000; i++)
        {
            points[i] = new Complex(R*Math.cos((2*Math.PI*i)/(1000))+x0, R*Math.sin((2*Math.PI*i)/(1000))+y0);
        }

        for (int i = 0; i < 1000; i++)
        {
            airfoil[i] = Complex.sum(points[i], Complex.divide(new Complex(1, 0), points[i]));
        }
    } //updates input parameters

    private Complex V(Complex z)
    {
        if (Complex.modulo( Complex.sub(Complex.sub(Complex.multiply(0.5, z),Complex.pow(Complex.sum(-1,Complex.multiply(0.25,Complex.pow(z,2))),0.5)),z0)) >= R)
        {
            Complex sqrt = Complex.pow(Complex.sum(-4, Complex.pow(z, 2)),0.5);
            Complex sMinusZ = Complex.sub(sqrt, z);
            Complex t1 = Complex.sum(Complex.sum(Complex.sub(Complex.sum(2* Math.pow(R, 2),Complex.multiply(-2, Complex.multiply(sqrt, z0))),Complex.pow(z, 2)),Complex.multiply(z, sqrt)),Complex.sum(Complex.multiply(2, Complex.multiply(z, z0)),Complex.sum(2,Complex.multiply(-2, Complex.pow(z0, 2)))));
            Complex denom = Complex.multiply(sqrt,Complex.pow(Complex.sum(sMinusZ, Complex.multiply(2, z0)),2));

            Complex result = Complex.divide(Complex.multiply(-V0,Complex.multiply(sMinusZ, t1)),denom);
            return Complex.conjugate(result);
        }
        else
        {
            Complex sqrt = Complex.pow(Complex.sum(-4, Complex.pow(z, 2)),0.5);
            Complex sMinusZ = Complex.sum(sqrt, Complex.multiply(1, z));
            Complex t1 = Complex.sum(Complex.sum(Complex.sum(Complex.sum(-2* Math.pow(R, 2),Complex.multiply(-2, Complex.multiply(sqrt, z0))),Complex.multiply(1, Complex.pow(z, 2))),Complex.multiply(z, sqrt)),Complex.sum(Complex.multiply(-2, Complex.multiply(z, z0)),Complex.sum(-2,Complex.multiply(2, Complex.pow(z0, 2)))));
            Complex denom = Complex.multiply(sqrt,Complex.pow(Complex.sum(sMinusZ, Complex.multiply(-2, z0)),2));

            Complex result = Complex.divide(Complex.multiply(V0,Complex.multiply(sMinusZ, t1)),denom);
            return Complex.conjugate(result);
        }
    } //complex valued velocity function

    private void update()
    {
        for (int i = 0; i<particles.length; i++)
        {
            particles[i]=Complex.sum(particles[i], V(particles[i]));

            if (particles[i].re > 8.2/2-0.1 || !(Complex.modulo( Complex.sub(Complex.sub(Complex.multiply(0.5, particles[i]),Complex.pow(Complex.sum(-1,Complex.multiply(0.25,Complex.pow(particles[i],2))),0.5)),z0)) > R || Complex.modulo( Complex.sub(Complex.sum(Complex.multiply(0.5, particles[i]),Complex.pow(Complex.sum(-1,Complex.multiply(0.25,Complex.pow(particles[i],2))),0.5)),z0)) > R))
            {
                particles[i]=new Complex(-8.1/2-8.1/4*Math.random(), Math.random()*4.70-4.70/2);
            }
        }

        if (weird)
        {
            ConformalMapper.frame.setLocation((int)weirdTimer,ConformalMapper.frame.getLocation().y);
            weirdTimer=weirdTimer-V0*100;
            if (weirdTimer<0-this.getWidth())
            {
                weird = false;
                weirdTimer=screenWidth;
                ConformalMapper.frame.setLocationRelativeTo(null);
            }
        }
    } //updates the simulation state

    private void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        //draw background
        g2d.setColor(bgr);
        g2d.fillRect(0, 0, 1200, 800);

        //draw simulation viewport, particles and airfoil
        g2d.setColor(new Color(0x14031C));
        g2d.fillRect(10,10,810,470);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i<particles.length;i++)
        {
            g2d.setColor(pColour[i]);
            if (vColor)
            {
                float c = (float) ((Math.tanh((Complex.modulo(V(particles[i])))/V0-1)+1)/2.0);
                g2d.setColor(Gradient.sunset(c));
            }
            g2d.fillOval((int)(particles[i].re*100-2+10+820/2), (int)(-particles[i].im*100-2+10+470/2), 4, 4);
        }

        g2d.setColor(Color.WHITE);
        for (int i = 0; i<airfoil.length;i++)
        {
            g2d.fillOval((int)(airfoil[i].re*100-1+10+820/2), (int)(-airfoil[i].im*100-1+10+470/2), 2, 2);
        }

        //draw subpanels
        g2d.setColor(subPanelColor);
        g2d.fillRect(830, 10, 345, 470);

        //clean up viewport
        g2d.setColor(bgr);
        g2d.fillRect(0, 0, 820, 10);
        g2d.fillRect(0, 480, 820, 10);
        g2d.fillRect(0, 0, 10, 500);

        //draw GUI components
        super.paint(g);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g2d.drawImage(image, 0, 0, this);
    } //paint component method

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
                update();
				repaint();
				delta--;
			}
		}
	} //run method with delta time loop
}
