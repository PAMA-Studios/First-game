import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class MenuOptions extends ActorsOptions
{
    MenuOptions() {
        GreenfootImage image = new GreenfootImage("Options.png");
        image.scale((Options.blockSize) * 6,(Options.blockSize) * 2);
        setImage(image);
    } 
    public void act() 
    {
        if (Greenfoot.mouseClicked(this))
        {
            optionsMenu();
        }
    }
}
