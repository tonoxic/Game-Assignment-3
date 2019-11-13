public class Points
{
    static int score;
    static long born;
    static Points points;
    
    private Points()
    {
        score=3;
        born=System.currentTimeMillis();
    }
    
    public static int getPoints()
    {
        if (points==null)
            points=new Points();
        return points.score;
    }
    
    public static void addPoints(int merit)
    {
        if (points==null)
            points=new Points();
        score+=merit;
        if (score>=60)
            score=60;
    }
    
    public static void deductPoints(int demerit)
    {
        if (points==null)
            points=new Points();
        score-=demerit;
        if (score<=0)
            score=0;
    }
   
}
