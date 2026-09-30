//Nathan Armstrong 2026/03/02
public class Complex
{
    double re;
    double im;

    public Complex(double re, double im)
    {
        this.re = re;
        this.im = im;
    }

    public static double re(Complex c)
    {
        return c.re;
    } //returns the real component of c

    public static double im(Complex c)
    {
        return c.im;
    }//returns the imaginary component of c

    public static Complex conjugate(Complex c)
    {
        return new Complex(Complex.re(c), -Complex.im(c));
    }//returns the complex conjugate of c

    public static Complex sum(Complex c1, Complex c2)
    {
        return new Complex(Complex.re(c1)+Complex.re(c2),Complex.im(c1)+Complex.im(c2));
    }//returns the sum c1+c2

    public static Complex sum(double c1, Complex c2)
    {
        return new Complex(c1+Complex.re(c2),Complex.im(c2));
    }//returns the sum c1+c2

    public static Complex sub(Complex c1, Complex c2)
    {
        return new Complex(Complex.re(c1)-Complex.re(c2),Complex.im(c1)-Complex.im(c2));
    }//returns the difference c1-c2

    public static Complex sub(double c1, Complex c2)
    {
        return new Complex(c1-Complex.re(c2),-Complex.im(c2));
    }//returns the difference c1-c2

    public static Complex multiply(Complex c1, Complex c2)
    {
        return new Complex(Complex.re(c1)*Complex.re(c2)-Complex.im(c1)*Complex.im(c2),Complex.re(c1)*Complex.im(c2)+Complex.im(c1)*Complex.re(c2));
    }//returns the product c1*c2

    public static Complex multiply(double c1, Complex c2)
    {
        return new Complex(c1*Complex.re(c2),c1*Complex.im(c2));
    }//returns the product c1*c2

    public static double modulo(Complex c)
    {
        return Math.sqrt(Complex.re(Complex.multiply(c, Complex.conjugate(c))));
    }//returns the complex modulo |c|

    public static Complex divide(Complex c1, Complex c2)
    {
        return Complex.multiply(new Complex(1/Complex.re(Complex.multiply(c2, Complex.conjugate(c2))),0), Complex.multiply(c1, Complex.conjugate(c2)));
    }//returns c1/c2

    public static Complex divide(double c1, Complex c2)
    {
        return Complex.multiply(new Complex(1/Complex.re(Complex.multiply(c2, Complex.conjugate(c2))),0), Complex.multiply(c1, Complex.conjugate(c2)));
    }//returns c1/c2

    public static Complex exp(Complex c)
    {
        return new Complex(Math.exp(c.re)*Math.cos(c.im), Math.exp(c.re)*Math.sin(c.im));
    }//returns e^c in a+bi format

    public static Complex pow(Complex c, double a)
    {
        return new Complex(Math.pow(Complex.modulo(c), a)*Math.cos(a*Math.atan2(c.im, c.re)),Math.pow(Complex.modulo(c), a)*Math.sin(a*Math.atan2(c.im, c.re)));
    }//returns c^a in a+bi format

    public static Complex cos(Complex c)
    {
        Complex c1 = new Complex(-c.im, c.re);
        Complex c2 = new Complex(c.im, -c.re);
        Complex c3 = Complex.sum(Complex.exp(c1),Complex.exp(c2));
        return new Complex(0.5*c3.re,0.5*c3.im);
    }//returns cos(c) in a+bi format

    public static Complex sin(Complex c)
    {
        Complex c1 = new Complex(-c.im, c.re);
        Complex c2 = new Complex(c.im, -c.re);
        Complex c3 = Complex.sub(Complex.exp(c2),Complex.exp(c1));
        return new Complex(-0.5*c3.im,0.5*c3.re);
    }//returns sin(c) in a+bi format

    public static Complex tan(Complex c)
    {
        try
        {
            return Complex.divide(Complex.sin(c), Complex.cos(c));
        }
        catch (Exception e)
        {
            return null;
        }
    }//returns tan(c) in a+bi format

    public static Complex ln(Complex c)
    {
        if (c.re > 0)
        {
            return new Complex (Math.log(Complex.modulo(c)),Math.atan(c.im/c.re));
        }
        
        if (c.re < 0 && c.im >= 0)
        {
            return new Complex (Math.log(Complex.modulo(c)),Math.PI+Math.atan(c.im/c.re));
        }

        if (c.re > 0 && c.im < 0)
        {
            return new Complex (Math.log(Complex.modulo(c)),2*Math.PI-Math.atan(c.im/c.re));
        }

        if (c.re < 0 && c.im < 0)
        {
            return new Complex (Math.log(Complex.modulo(c)),Math.atan(c.im/c.re)-Math.PI);
        }

        if (c.re==0 && c.im<0)
        {
            return new Complex (Math.log(Complex.modulo(c)),-Math.PI/2);
        }

        return new Complex (Math.log(Complex.modulo(c)),Math.PI/2);
    }//returns ln(c) in a+bi format

    public String toString()
    {
        if (re==0&&im==0)
        {
            return "0";
        }
        else if(im==0)
        {
            return ""+this.re;
        }
        else if (re==0)
        {
            return "i"+this.im;
        }
        else if (im<0)
        {
            return this.re+"-i"+-this.im;
        }
        else
        {
            return this.re+"+i"+this.im;
        }
    }//formats object for print(c)
}
