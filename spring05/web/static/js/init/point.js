export class point
{
    constructor(x,y)
    {
        this.x = x||0;
        this.y = y||0;
        this.radius = 0;
    }

    add(point)
    {
        this.x += point.x;
        this.y += point.y;

        return this;
    }

    subtract(point)
    {
        this.x -= point.x;
        this.y -= point.y;

        return this;   
    }

    reduce(value)
    {
        this.x *= value;
        this.y *= value;
        return this;
    }

    clone()
    {
        return new point(this.x,this.y,this.radius);
    }
}