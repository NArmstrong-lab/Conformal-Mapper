import java.awt.*;

public class Gradient
{
    private Gradient()
    {

    }

    public static Color sunset(double x)
    {
        if (x > 1 || x<0) return null;

        double r = 0;
        double g = 0;
        double b = 0;

        x*=5;

        if (x<=1)
        {
            r = (1-x)*34+x*110;
            g = (1-x)*12+x*39;
            b = (1-x)*46+x*121;
        }
        else if (x<=2)
        {
            x-=1;
            r = (1-x)*110+x*161;
            g = (1-x)*39+x*54;
            b = (1-x)*121+x*92;
        }
        else if (x<=3)
        {
            x-=2;
            r = (1-x)*161+x*227;
            g = (1-x)*54+x*92;
            b = (1-x)*92+x*40;
        }
        else if (x<=4)
        {
            x-=3;
            r = (1-x)*227+x*255;
            g = (1-x)*92+x*165;
            b = (1-x)*40+x*29;
        }
        else if (x<=5)
        {
            x-=4;
            r = (1-x)*255+x*255;
            g = (1-x)*165+x*246;
            b = (1-x)*29+x*216;
        }
        else
        {
            return null;
        }

        return new Color((int)r,(int)g,(int)b);
    }
}
