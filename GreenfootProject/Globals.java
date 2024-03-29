public class Globals  
{
    public static int currentX = 0;
    public static int worldWidth; //world width
    public static int worldHeight; //world height
    public static int startingHeight;
    public static String backgroundImage = "background.png"; //backgroud image
    public static boolean alive = true;
    public static double entityOffsetX = 0;
    public static double entityOffsetY = 0;
    
    public static int level = 0;
    public static int lastLevel = 0;
    public static int selectedLevel = 0;
    
    public static int coins = 0;
    public static int coinsThisLevel = 0;
    
    public static int score = 0;
    public static int scoreThisLevel = 0;
    
    public static Integer platforms[] = {74,75,76,77,92,93,94,95,110,111,112,113,128,129,130,131,146,147
        ,148,149,164,165,166,167};
    public static Integer slopeLefts[] = {78,96,114,132,150,168};
    public static Integer slopeRights[] = {79,97,115,133,151,169};
    public static Integer lavas[] = {230,231,232};
    public static Integer waters[] = {263,264,265};
    public static Integer spikes[] = {249};
    public static Integer finishFlag[] = {176,177,178,179,180,181,182,183,184,185,186,187};
    public static Integer nonSolids[] = {188
        ,189,190,191,192,193,194,195,196,197,198,218,219,220,221,222,223,224,225,226,227,228,229,233,234
        ,235,240,241,242,243,244,245,246,247,248,250,251};
}
