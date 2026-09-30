//Nathan Armstrong 2026

/*
    This class is responsible for allowing the
    MainPanel to map points in an array through
    the functions defined in the Complex class.
*/

public class MapLibrary
{
    public MapLibrary()
    {

    } //empty constructor

    public Complex[] exp(Complex[] points, double a, double b)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.multiply(new Complex(a, 0),Complex.pow(Complex.exp(points[i]),b));
        }

        return pointsM;
    }//w=a*e^(b*z)

    public Complex[] pow(Complex[] points, double a, double b)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.multiply(new Complex(a, 0), Complex.pow(points[i],b));
        }

        return pointsM;
    } //w=a*z^b

    public Complex[] sin(Complex[] points, double a, double b, double c)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.multiply(new Complex(a,0),Complex.pow(Complex.sin(Complex.multiply(new Complex(b,0), points[i])),c));
        }

        return pointsM;
    } //w=c*sin^a(b*z)

    public Complex[] cos(Complex[] points, double a, double b, double c)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.multiply(new Complex(a,0),Complex.pow(Complex.cos(Complex.multiply(new Complex(b,0), points[i])),c));
        }

        return pointsM;
    }//w=c*cos^a(b*z)

    public Complex[] tan(Complex[] points, double a, double b, double c)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.multiply(new Complex(a,0),Complex.pow(Complex.tan(Complex.multiply(new Complex(b,0), points[i])),c));
        }

        return pointsM;
    } //w=c*tan^a(b*z)

    public Complex[] ln(Complex[] points, double a, double b)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.multiply(Complex.ln(Complex.multiply(points[i], new Complex(b, 0))), new Complex(a, 0));
        }

        return pointsM;
    }//w=a*ln(b*z)

    public Complex[] Joukowsky(Complex[] points)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.sum(points[i], Complex.divide(new Complex(1, 0), points[i]));
        }

        return pointsM;
    } //w=z+1/z

    public Complex[] simpleFraction(Complex[] points, double a, double b, double c)
    {
        Complex[] pointsM = new Complex[points.length];

        for (int i = 0; i < MainPanel.n; i++)
        {
            pointsM[i] = Complex.multiply(new Complex(a, 0),Complex.divide(new Complex(Complex.re(points[i])-b,Complex.im(points[i])), new Complex(Complex.re(points[i])-c,Complex.im(points[i]))));
        }

        return pointsM;
    }//w=(z-a)/(z-b)
}
