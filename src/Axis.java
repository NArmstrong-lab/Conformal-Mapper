//Nathan Armstrong 2026

import java.awt.*;

/*
    This class is used to create axis objects
    responible for rendering points on a grid
*/

public class Axis
{
    //colour used as the background for axis
    Color bgr = new Color(0x040404);

    //axis variables
    private int x; //axis x position (in the parent component)
    private int y; //axis y position (in the parent component)
    private int width; //axis width
    private int height; //axis height
    private int dx = 0; //horizontal shift
    private int dy = 0; //vertical shift
    private double zoom = 0.2; //zoom

    //colour varaibles
    private double r = 255; //red
    private double g = 0; //green
    private double b = 0; //blue
    private double dc = 0; //the amount that colours change by when updated 

    //hue shift variables
    private double hueR = 255;
    private double hueG = 0;
    private double hueB = 0;

    //image rendering variables
    private boolean imageMode = false;
    static Color[] colorImage;
    static int pixelSize = 1;

    private double colourRange = 6;

    private Complex[] points;

    public Axis(int x, int y, int width, int height)
    {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    } //axis constructor

    public void setPoints(Complex[] points)
    {
        this.points=points;
    } //set the points to be plotted

    public void setAxisDisplacement(int x, int y)
    {
        dx=x;
        dy=y;
    } //set axis displacement

    public void setAxisZoom(double zoom)
    {
        this.zoom = zoom;
    } //set zoom

    public void setColourRange(double range)
    {
        colourRange = range;
    } //set colour range

    public void setHue(int hue)
    {
        r=255;
        g=0;
        b=0;
        for (int i = 0; i<hue; i++)
        {
            updatePointColour(1);
        }
        hueR = r;
        hueG = g;
        hueB = b;
    } //set hue

    public int getX()
    {
        return this.x;
    } //get axis x position (in parent component)

    public int getY()
    {
        return this.y;
    } //get axis y position (in parent component)


    public int getWidth()
    {
        return this.width;
    } //get axis width

    public int getHeight()
    {
        return this.height;
    } //get axis height

    private double getMantissa(double x)
    {
        double exponent = Math.floor(Math.log10(Math.abs(x)));
            return x / Math.pow(10, exponent);
    } //gets the mantissa of a double x, used for zoom functionality

    private double getExponent(double x)
    {
        return Math.floor(Math.log10(Math.abs(x)));
    } //gets the exponent of a double x, used for zoom functionality

    private void updatePointColour(double delta)
    {
        //update colours
        if (r==255 && g<255 && b==0)
        {
            g+=delta;
        }
        else if (r>0 && g==255 && b==0)
        {
            r-=delta;
        }
        else if (g==255 && b<255 && r==0)
        {
            b+=delta;
        }
        else if (g>0 && b==255 && r==0)
        {
            g-=delta;
        }
        else if (b==255 && r<255 && g==0)
        {
            r+=delta;
        }
        else if (b>0 && r==255 && g==0)
        {
            b-=delta;
        }

        //ensure colour values dont go outside [0,255]
        r = r>255 ? 255:r;
        g = g>255 ? 255:g;
        b = b>255 ? 255:b;

        r = r<0 ? 0:r;
        g = g<0 ? 0:g;
        b = b<0 ? 0:b;
    } //updates the rgb colour state depending on the current rgb colour state

    public void setImageMode(boolean imageMode)
    {
        this.imageMode=imageMode;
    } //sets the image mode boolean

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //render background
        g2d.setColor(bgr);
        g2d.fillRect(x, y, width, height);

        //draw grid lines on the grid depending on the value of the zoom and displacement
        g2d.setColor(Color.gray);
        double truncatedZoom = getMantissa(zoom)/10;
        int j = (int)(3/truncatedZoom);

        int indexOffSetX = (int)(((double)dx/truncatedZoom)/(double)this.width);
        int indexOffSetY = (int)(((double)dy/truncatedZoom)/(double)this.height);

        indexOffSetX = (int)(((double)dx/truncatedZoom)/(double)this.width);
        indexOffSetY = (int)(((double)dy/truncatedZoom)/(double)this.height);

        for (int i = -j; i <= j; i+=1)
        {
            int ix=i-indexOffSetX;
            int iy=i-indexOffSetY;

            g2d.setColor(Color.gray);
            if (ix == 0)
            {
                g2d.setColor(Color.white);
            }
            if (Math.abs((int)(ix*(double)this.width*truncatedZoom)+dx)<=this.width/2)
            {
                g2d.drawLine((int)(ix*(double)this.width*truncatedZoom)+x+this.width/2+dx,0+y,(int)(ix*(double)this.width*truncatedZoom)+x+this.width/2+dx, this.height+y);
            }

            g2d.setColor(Color.gray);
            if (iy == 0)
            {
                g2d.setColor(Color.white);
            }
            if (Math.abs((int)(iy*(double)this.height*truncatedZoom)+dy)<=this.height/2)
            {
                g2d.drawLine(0+x,(int)(iy*(double)this.height*truncatedZoom)+y+this.height/2+dy,this.width+x, (int)(iy*(double)this.height*truncatedZoom)+y+this.height/2+dy);
            }

        }

        if (points != null && !imageMode)
        {
            this.r=hueR;
            this.g=hueG;
            this.b=hueB;
            this.dc=colourRange*255/(double)MainPanel.n;
            for (int i = 0; i < MainPanel.n; i++)
            {
                g2d.setColor(new Color((int)this.r,(int)this.g,(int)this.b));
                if(Math.abs((int)(((double)this.width*zoom)*Complex.re((points[i])))+dx-1)<this.width/2-1 && Math.abs((int)((-(double)this.height*zoom)*Complex.im((points[i])))+dy-1)<this.height/2-1)
                {
                    g2d.fillOval((int)(((double)this.width*zoom)*Complex.re((points[i]))+this.width/2+x-2)+dx, (int)((-(double)this.width*zoom)*Complex.im(points[i])+this.height/2+y-2)+dy, 4, 4);
                }
                updatePointColour(dc);
            }
        } //if the input points arent an empty array and the renderer is not in image mode, draw the points at their respective co-ordinates, then update the colour for the next point
        else if (points != null && imageMode)
        {
            int u = (int)(zoom*5);
            if (u<1) u=1;
            u=pixelSize;
            int v = (int)((double)u/(double)2);

            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            for (int i = 0; i < MainPanel.imageN; i++)
            {
                //System.out.println(i);
                g2d.setColor(colorImage[i]);
                if(points[i]==null)
                {
                    System.out.println(i);
                }
                if(Math.abs((int)(((double)this.width*zoom)*Complex.re((points[i])))+dx-1)<this.width/2-1 && Math.abs((int)((-(double)this.height*zoom)*Complex.im((points[i])))+dy-1)<this.height/2-1)
                {
                    g2d.fillRect((int)(((double)this.width*zoom)*Complex.re((points[i]))+this.width/2+x-v)+dx, (int)((-(double)this.height*zoom)*Complex.im(points[i])+this.height/2+y-v)+dy, u, u);
                }
            }
        } //if the input points arent an empty array and the renderer is in image mode, draw the points at their respective co-ordinates and use the colours in the colorImage array

        //now we draw all the text components on the grid, i.e. numbers
        int xAxisStringY = this.height/2+y+dy-3;
        if (xAxisStringY >= this.height+y-3)
        {
            xAxisStringY = this.height+y-3;
        }
        else if (xAxisStringY <= y+15)
        {
            xAxisStringY=y+15;
        }

        int yAxisStringX = (int)(this.width/2+x+dx+4);
        if (yAxisStringX >= this.width+x-16)
        {
            yAxisStringX = this.width+x-16;
        }
        else if (yAxisStringX <= x+15)
        {
            yAxisStringX=x+15;
        }

        for (int i = -j; i <= j; i+=1)
        {
            int ix=i-indexOffSetX;
            int iy=i-indexOffSetY;
        
            String exponent = "E"+-((int)getExponent(zoom)+1);
            if (0.1 < zoom && zoom < 1)
            {
                exponent = "";
            }

            g2d.setColor(Color.white);
            if (Math.abs((int)(ix*(double)this.width*truncatedZoom)+dx)<=this.width/2 && ix!=0)
            {
                if (Math.abs((int)(ix*(double)this.width*truncatedZoom)+dx)<=this.width/2)
                {
                    g2d.setColor(new Color(255,255,255, (int) ( 255* Math.pow( 1 - Math.abs( ((ix*(double)this.width*truncatedZoom)+dx) / (this.width/2) ),0.25 ) ) ));
                }
                g2d.drawString(""+ix+exponent,(int)(ix*(double)this.width*truncatedZoom)+x+this.width/2+dx-4,xAxisStringY);
                g2d.setColor(Color.white);
            }

            if (Math.abs((int)(ix*(double)this.width*truncatedZoom)+dx)<=this.width/2 && Math.abs(dy)<=this.height/2 && ix==0)
            {
                if (Math.abs((int)(ix*(double)this.width*truncatedZoom)+dx)<=this.width/2)
                {
                    g2d.setColor(new Color(255,255,255, (int) ( 255* Math.pow( 1 - Math.abs( ((ix*(double)this.width*truncatedZoom)+dx) / (this.width/2) ),0.25 ) ) ));
                }
                g2d.drawString(""+ix,(int)(ix*(double)this.width*truncatedZoom)+x+this.width/2+dx-10,this.height/2+y+dy-3);
            }
            
            if (Math.abs((int)(iy*(double)this.height*truncatedZoom)+dy)<=this.height/2 && iy!=0)
            {
                if (Math.abs((int)(iy*(double)this.height*truncatedZoom)+dy)<=this.height/2)
                {
                    g2d.setColor(new Color(255,255,255, (int) ( 255* Math.pow( 1 - Math.abs( ((iy*(double)this.height*truncatedZoom)+dy) / (this.height/2) ),0.25 ) ) ));
                }
                g2d.drawString(-iy+exponent+"i",yAxisStringX,(int)(iy*(double)this.height*truncatedZoom)+y+this.height/2+dy+4);
            }
        }
    }
}
