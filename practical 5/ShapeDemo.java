abstract class Shape
{
    abstract double area();
}

class Circle extends Shape
{
    private double radius;

    Circle(double radius)
    {
        this.radius = radius;
    }

    double area()
    {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape
{
    private double length;
    private double width;

    Rectangle(double length, double width)
    {
        this.length = length;
        this.width = width;
    }

    double area()
    {
        return length * width;
    }
}

class Triangle extends Shape
{
    private double base;
    private double height;

    Triangle(double base, double height)
    {
        this.base = base;
        this.height = height;
    }

    double area()
    {
        return 0.5 * base * height;
    }
}

public class ShapeDemo
{
    public static void main(String[] args)
    {
        Shape[] shapes =
        {
            new Circle(5),
            new Rectangle(10, 4),
            new Triangle(6, 8),
            new Circle(3)
        };

        double total = 0;
        double largest = 0;

        for (Shape s : shapes)
        {
            double a = s.area();

            System.out.println("Area = " + a);

            total += a;

            if (a > largest)
            {
                largest = a;
            }
        }

        System.out.println("Total Area = " + total);
        System.out.println("Largest Area = " + largest);
    }
}