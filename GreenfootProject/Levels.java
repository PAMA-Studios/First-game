import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Levels extends World
{
    protected static int levelWidth; //tiles wide
    protected static int levelHeight; //tiles high
    protected static String mapString; //world layout string
    protected static int[] world; //world layout string
    
    protected static int width; //level rendering tiles per row
    protected static int height; //level rendering rows
    
    Scroller scroller; // the object that performs the scrolling
    Actor scrollActor; // an actor to stay in view*
    
    public void act() //scroll the map every frame
    {
        scroll();
    }
    private static int getStartingHeight(int level) {
        if (level == 0) return 3;
        else if (level == 1) return 3;
        else if (level == 2) return 3;
        else return 4;
    }
    private static String getBackground(int level) {
        return "background.png";
    }
    protected static void getWorldMap (String level) {
        //reset the level layout to blank
        levelWidth = 0;
        levelHeight = 0;
        mapString = "";
        
        //read the level layout
        File readFile = new File("levelLayouts/" + level + ".tmx"); //set what file to read
        Scanner dataReader = null; //scanner for the file
        
        try
        {
            dataReader = new Scanner(readFile); //try to read the file
        }
        catch(IOException e) //error report
        {
            System.out.println("File Read error" + e); //show error
        }
        while(dataReader.hasNext()) //while there's a next line
        {
           String line = dataReader.next(); //line is next line
           if (line.contains("width=\"") && !line.contains("tile")) { //check for width
               line = line.replaceAll("[^\\d]",""); //replace all non numbers with nothing
               levelWidth = Integer.parseInt(line); //set levelWidth
           }
           if (line.contains("height=\"") && !line.contains("tile")) { //check for height
               line = line.replaceAll("[^\\d]",""); //replace all non numbers with nothing
               levelHeight = Integer.parseInt(line); //set levelHeight
           }
           if (line.contains(",")) { //check if line is part of the map
                mapString = mapString += line; //add new line to total map
           }
        }
        String[] integerStrings = mapString.split(","); //split values by ,
        world = new int[integerStrings.length]; //make an array with space for every slot 
        for (int i = 0; i < world.length; i++){ 
            world[i] = Integer.parseInt(integerStrings[i]); //turn array into int array
        }
        
        Globals.worldHeight = Options.blockSize * levelHeight; //set worldHeight in tiles
        Globals.worldWidth = Options.blockSize * levelWidth; //set worldWidth in tiles
        dataReader.close(); //remove the scanner. we don't need it anymore
    }
    protected void renderWorld() {
        //reset starting point for map creation
        width = -1;
        height = 0;
        //System.out.println("\nLevelWidth: " + levelWidth + ", LevelHeight " + levelHeight + ",\nTotal needed entries " + (levelWidth * levelHeight) + ", Total gotten entries: " + world.length + "\n");
        for (int i = 0; i < world.length; i++) {
            width += 1;
            //after width reaches levelWidth it goes to 0 and adds 1 to height. goes to next line
            if (width >= levelWidth) {
                height += 1;
                width = 0;
            }
            if (height > levelHeight) {
                System.out.println("More rows than the level has rows to put stuff in");
                break;
            }
            /*Below is setting the objects into the world
             * 
             *Gonna only write this once because it's all the same thing
             *
             *if (super.check(Globals.nonSolids, world[i] - 1)) {}
             *checks with method of the super class (Levels)
             *if the ID of the block (world[i] - 1) is part of Globals.nonSolids array
             *This array can be found in the Globals Class. obviously. returns true or false
             *
             *
             *addObject(nextBlock,width * widthsize + 1/2 widthsize,height * heightsize + 1/2 heighsize);
             *adds new block at the next cords.
             *
             */
            placeBlock: {
                Actor nextBlock;
                if (check(Globals.platforms, world[i] - 1)) {
                    nextBlock = new Platform(world[i] - 1);
                } else if (check(Globals.nonSolids, world[i] - 1)) {
                    nextBlock = new NonSolid(world[i] - 1);
                } else if (check(Globals.slopeLefts, world[i] - 1)) {
                    nextBlock = new SlopeLeft(world[i] - 1);
                } else if (check(Globals.slopeRights, world[i] - 1)) {
                    nextBlock = new SlopeRight(world[i] - 1);
                } else if (check(Globals.lavas, world[i] - 1)) {
                    nextBlock = new Lava(world[i] - 1);
                } else if (check(Globals.waters, world[i] - 1)) {
                    nextBlock = new Water(world[i] - 1);
                } else if (check(Globals.spikes, world[i] - 1)) {
                    nextBlock = new Spike(world[i] - 1);
                } else if (check(Globals.finishFlag, world[i] - 1)) {
                    nextBlock = new FinishFlag(world[i] - 1);
                } else if (world[i] - 1 == 173) {
                    nextBlock = new Coin(world[i] - 1, 2);
                } else if (world[i] - 1 == 175) {
                    nextBlock = new Coin(world[i] - 1, 5);
                } else if (world[i] - 1 == 174) {
                    nextBlock = new Coin(world[i] - 1, 10);
                } else if (world[i] - 1 == 38 || world[i] - 1 == 39 || world[i] - 1 == 40) {
                    nextBlock = new HalfSaw();
                } else if (world[i] - 1 >= 44 && world[i] - 1 <= 55) {
                    nextBlock = new Slime(world[i] - 1);
                } else if (world[i] != 0){
                    nextBlock = new Solid(world[i] - 1); 
                } else {
                    break placeBlock;
                }
                //finally add the block if not broken out of
                Add(nextBlock); 
            }
        } 
    }
    public void renderHud() {
        HudCoin hudCoin = new HudCoin();
        addObject (hudCoin, 0, 0);
        HudHeart hudHeart1 = new HudHeart(1);
        HudHeart hudHeart2 = new HudHeart(2);
        HudHeart hudHeart3 = new HudHeart(3);
        addObject (hudHeart1, 0, 0);
        addObject (hudHeart2, 0, 0);
        addObject (hudHeart3, 0, 0);
        HudNumber hudNumber1 = new HudNumber("coins", 1);
        HudNumber hudNumber2 = new HudNumber("coins", 2);
        HudNumber hudNumber3 = new HudNumber("coins", 3);
        addObject (hudNumber1, 0, 0);
        addObject (hudNumber2, 0, 0);
        addObject (hudNumber3, 0, 0);
        HudNumber hudNumber4 = new HudNumber("score", 1);
        HudNumber hudNumber5 = new HudNumber("score", 2);
        HudNumber hudNumber6 = new HudNumber("score", 3);
        HudNumber hudNumber7 = new HudNumber("score", 4);
        HudNumber hudNumber8 = new HudNumber("score", 5);
        addObject (hudNumber4, 0, 0);
        addObject (hudNumber5, 0, 0);
        addObject (hudNumber6, 0, 0);
        addObject (hudNumber7, 0, 0);
        addObject (hudNumber8, 0, 0);
    }
    private void renderMenu() {
        ActorsOptions menu = new ActorsOptions();
        addObject (menu, 0, 0);
        
        Key key1 = new Key("jump", 1);
        addObject(key1,0,0);
        Key key2 = new Key("down", 1);
        addObject(key2,0,0);
        Key key3 = new Key("left", 1);
        addObject(key3,0,0);
        Key key4 = new Key("right", 1);
        addObject(key4,0,0);
        Key key5 = new Key("action", 1);
        addObject(key5,0,0);
        Key key6 = new Key("jump", 2);
        addObject(key6,0,0);
        Key key7 = new Key("down", 2);
        addObject(key7,0,0);
        Key key8 = new Key("left", 2);
        addObject(key8,0,0);
        Key key9 = new Key("right", 2);
        addObject(key9,0,0);
        Key key10 = new Key("action", 2);
        addObject(key10,0,0);
    }
    public void Add(Actor nextBlock) {
        addObject(nextBlock, width*Options.blockSize + Options.blockSize/2,
                height*Options.blockSize + Options.blockSize/2);
    }
    public static boolean check(Integer[] arr, int toCheckValue) {
        boolean test
                = Arrays.asList(arr)
                .contains(toCheckValue);
        return test;
    }
    public Levels(int level) {
        super(Options.screenWidth, Options.screenHeight, 1, false);
        
        Globals.backgroundImage = getBackground(level);
        Globals.startingHeight = getStartingHeight(level);
        
        getWorldMap("Level" + level);
        renderWorld();
        renderHud();
        renderMenu();
        
        initiateScroll(Globals.backgroundImage, -40, Globals.worldHeight - (Globals.startingHeight * Options.blockSize - Options.blockSize / 4));
        
        if (Options.screenHeight < 1080) {
            Options.smallerScreen = 1;
        }
        Globals.coinsThisLevel = 0;
        Globals.scoreThisLevel = 0;
    }
    public void initiateScroll(String backgroundImage, int x, int y) {
        GreenfootImage bg = new GreenfootImage(backgroundImage); // creates an image to scroll (adjust as needed)
        
        scroller = new Scroller(this, bg, Globals.worldWidth, Globals.worldHeight); // creates the Scroller object for this world, with background bg of image width and height in Globals.
        scrollActor = new Player("green", 1); //Creates the object to focus on
        addObject(scrollActor, 0, 0);
        scroll();
        scrollActor.setLocation(x, y); //move scrollactor to the right spot
        scroll();
    }
    public void scroll() {
        if (Globals.alive) {
            int loX = Options.screenWidth/16*7; //Barrier left of center to move
            int hiX = Options.screenWidth-(Options.screenWidth/16*7); //Barrier right of center to move
            int loY = Options.screenHeight/8*2; //Barrier from the ceiling to move
            int hiY = Options.screenHeight-(Options.screenHeight/8*3); //Barrier from the bottom to move
            // determine offsets and scroll
            int dsx = 0, dsy = 0;
            if (scrollActor.getX() < loX) dsx = scrollActor.getX()-loX;
            if (scrollActor.getX() > hiX) dsx = scrollActor.getX()-hiX;
            if (scrollActor.getY() < loY) dsy = scrollActor.getY()-loY;
            if (scrollActor.getY() > hiY) dsy = scrollActor.getY()-hiY;
            scroller.scroll(dsx, dsy); //do the scrolling
            Globals.currentX = scroller.getScrolledX(); //return how much the screen has scrolled for future usage
        }
    }
} 
