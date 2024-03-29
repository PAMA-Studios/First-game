import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Player extends Physics
{
    public static int health = 6;
    public boolean started = false;
    private int animationFrame = 0;
    public int introFrame = 0;
    public boolean endingAnimation = false;
    public int endingFrame = 0;
    private int invulnerabilityFrames = 0;
    public boolean dead = false;
    public int deathAnimationFrame = 0;
    private int leftTime = 0;
    private int rightTime = 0;
    private int downTime = 0;
    private int jumpTime = 0;
    
    public GreenfootImage imageFront;
    public GreenfootImage imageDuck;
    public GreenfootImage imageStand;
    public GreenfootImage imageJump;
    public GreenfootImage imageWalk1;
    public GreenfootImage imageWalk2;
    public GreenfootImage imageSwim1;
    public GreenfootImage imageSwim2;
    public GreenfootImage imageClimb1;
    public GreenfootImage imageClimb2;
    public GreenfootImage imageHit;
    public GreenfootImage imageTransparent;
    public GreenfootImage cropImage(String image) {
        GreenfootImage cutImage = new GreenfootImage (image);
        cutImage.scale(Options.blockSize,Options.blockSize);
        return cutImage;
    }
    Player(String color, int player) {
        setGravity(1);
        health = 6;
        
        imageFront = cropImage(color + "Front.png");
        imageDuck = cropImage(color + "Duck.png");
        imageStand = cropImage(color + "Stand.png");
        imageJump = cropImage(color + "Jump.png");
        imageWalk1 = cropImage(color + "Walk1.png");
        imageWalk2 = cropImage(color + "Walk2.png");
        imageSwim1 = cropImage(color + "Swim1.png");
        imageSwim2 = cropImage(color + "Swim2.png");
        imageClimb1 = cropImage(color + "Climb1.png");
        imageClimb2 = cropImage(color + "Climb2.png");
        imageTransparent = cropImage("transparent.png");
        setBarrierClasses(new Class[]{Solid.class});
        setPlatformClasses(new Class[]{Platform.class});
        setSlopeLeftClasses(new Class[]{SlopeLeft.class});
        setSlopeRightClasses(new Class[]{SlopeRight.class});
        setDamagingClasses(new Class[]{HalfSaw.class, Slime.class, Spike.class});
        setJumpOnClasses(new Class[]{Slime.class});
        setSuperDamagingClasses(new Class[]{Lava.class});
    }
    public void act() 
    {
        String key = Greenfoot.getKey();
        if("escape".equals(key) && !endingAnimation && !dead) {Greenfoot.setWorld(new LevelSelector());}
        if (!dead) setRelativeLocation(Globals.entityOffsetX,Globals.entityOffsetY);
        if (!started) {
            if (introFrame == 0) { 
                setNewLocation(-Options.blockSize/2, (Options.screenHeight - Options.screenHeight/8*2));
                setDoubleY(getY());
                setDoubleX(getX());
            }
            if (introFrame % 7 == 0) {
                if (getImage() == imageWalk1) {
                    setImage(imageWalk2);
                } else {
                    setImage(imageWalk1);
                }
            }
            if (introFrame >= 100) {
                setImage(imageStand);
                started = true;
            } else {
                moveRight(2.0);
            }
            introFrame += 1;
            doGravity();
        } else if (endingAnimation) {
            if (endingFrame % 7 == 0) {
                if (getImage() == imageWalk1) {
                    setImage(imageWalk2);
                } else {
                    setImage(imageWalk1);
                }
            }
            if (endingFrame >= 100) {
                setImage(imageStand);
                Globals.lastLevel = Globals.selectedLevel;
                if ((Globals.lastLevel + 1) > Globals.level) Globals.level = Globals.lastLevel + 1;
                Greenfoot.setWorld(new LevelSelector());
            } else {
                setRelativeLocation(2.5, 0);
            }
            endingFrame += 1;
            doGravity();
        } else if (!dead) {
            doGravity();
            setImage(imageFront);
            if(Options.leftButtonPlayer1.equals(key)) {leftTime = 0;}
            if(Options.rightButtonPlayer1.equals(key)) {rightTime = 0;}
            if(Options.jumpButtonPlayer1.equals(key)) {jumpTime = 0;}
            if(Options.downButtonPlayer1.equals(key)) {downTime = 0;}
            animationFrame ++;
            if (animationFrame > 60) animationFrame = 0;
            if(Greenfoot.isKeyDown(Options.leftButtonPlayer1))
            {
                if (canMoveLeft(leftTime / 10 + 4.0)) {
                    leftTime += 1;
                    if (leftTime > 60) leftTime = 60;
                    moveLeft(leftTime / 10 + 4.0);
                } else {
                    leftTime = 0;
                }
            } 
            if(Greenfoot.isKeyDown(Options.rightButtonPlayer1))
            {
                if (canMoveRight(rightTime / 10 + 4.0)) {
                    rightTime += 1;
                    if (rightTime > 60) rightTime = 60;
                    moveRight(rightTime / 10 + 4.0);
                    if (animationFrame % (int) (22 - 2*(rightTime / 10)) == 0) {
                        if (getImage() == imageWalk1) {
                            setImage(imageWalk2);
                        } else {
                            setImage(imageWalk1);
                        }
                    }
                } else {
                    rightTime = 0;
                }
            }
            if(Greenfoot.isKeyDown(Options.jumpButtonPlayer1)) // Only jump if on ground
            {
                if (onGround() || onSlope()) {
                    setNewLocation(getDoubleX(), getDoubleY() - 1.0);
                    jump(20); // jump()'s parameter should be rather large
                } 
                jumpTime += 1;
            }
            Coin coin = (Coin)getOneIntersectingObject(Coin.class);
            if (coin != null) {
                Globals.coinsThisLevel = Globals.coinsThisLevel + coin.value;
                if (Globals.coinsThisLevel > 999) Globals.coinsThisLevel = 999;
                Globals.scoreThisLevel = Globals.scoreThisLevel + coin.value * 10;
                if (Globals.scoreThisLevel > 99999) Globals.scoreThisLevel = 99999;
                getWorld().removeObject(coin);
            }
            if (onKillableEnemy()) {
                jump(20);
                Globals.scoreThisLevel = Globals.scoreThisLevel + 100;
                if (Globals.scoreThisLevel > 99999) Globals.scoreThisLevel = 99999;
                Slime object = (Slime)getOneObjectAtOffset(getImage().getWidth()/2, getImage().getHeight()/2 - 1, Slime.class);
                if (object != null) {
                    object.dead = true;
                }
                Slime object2 = (Slime)getOneObjectAtOffset(getImage().getWidth()/-2, getImage().getHeight()/2 - 1, Slime.class);
                if (object2 != null) {
                    object2.dead = true;
                }
            }
            if (invulnerabilityFrames > 0) {
                invulnerabilityFrames -= 1;
                if (invulnerabilityFrames % 4 >= 2) {
                    setImage(imageTransparent);
                } else {
                    setImage(imageFront);
                }
            } else {
                if (tookDamage()) {
                    health -= 1;
                    invulnerabilityFrames = 60;
                }
            }
            if (tookSuperDamage()) {
                health = 0;
            }
            if (finishedLevel()) {
                endingAnimation = true;
                Globals.coins = Globals.coins + Globals.coinsThisLevel;
                if (Globals.coins > 9999) Globals.coins = 9999;
                Globals.scoreThisLevel = Globals.scoreThisLevel + 1000;
                if (Globals.scoreThisLevel > 99999) Globals.scoreThisLevel = 99999;
                Globals.score = Globals.score + Globals.scoreThisLevel;
                if (Globals.score > 9999999) Globals.score = 9999999;
            }
            if (atBottom()) {
                dead = true;
                health = 0;
            } else {
                if (health < 1) {
                    dead = true;
                }
            }
        } else if (dead) {
            Globals.alive = false;
            if (deathAnimationFrame < 30) {
                setRelativeLocation(0,-(deathAnimationFrame / 3));
            } else if (deathAnimationFrame > 30 && deathAnimationFrame < 40) {
                setRelativeLocation(0,(deathAnimationFrame - 40));
            } else if (deathAnimationFrame > 40 && deathAnimationFrame < 50) {
                setRelativeLocation(0,(deathAnimationFrame - 40));
            } else {
                setRelativeLocation(0,(deathAnimationFrame / 5));
            }
            deathAnimationFrame += 1;
            if (getY() > Options.screenHeight + getImage().getHeight()) {
                dead = false;
                health = 6;
                Globals.alive = true;
                Greenfoot.setWorld(new Levels(Globals.selectedLevel));
            }
        }
    }     
}
