class Fraction {
    private int num;
    private int den;

    public Fraction(int num, int den) 
    {
        int g = gcd(num, den);
        this.num = num / g;
        this.den = den / g;
    }

    private int gcd(int a, int b) 
    {
        while (b != 0) 
        {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    @Override
    public String toString() 
    {
        return num + "/" + den;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (!(obj instanceof Fraction)) 
        {
            return false;
        }
        Fraction f = (Fraction) obj;
        return num == f.num && den == f.den;
    }

    @Override
    public int hashCode() 
    {
        return num * 31 + den;
    }

    public static void main(String[] args) 
    {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(3, 4);

        System.out.println("Fraction 1 : " + f1);
        System.out.println("Fraction 2 : " + f2);
        System.out.println("Fraction 3 : " + f3);
        System.out.println();
        System.out.println("F2 equals f3 : " + f2.equals(f3));
        System.out.println("F1 equals f2 : " + f1.equals(f2));
        System.out.println("F1 equals f3 : " + f1.equals(f3));
    }
}
