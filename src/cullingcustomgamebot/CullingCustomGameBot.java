/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cullingcustomgamebot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//import net.sourceforge.tess4j.*;

/**
 *
 * @author Ross
 */
public class CullingCustomGameBot {
    private Robot mRobot;
    private String mLobbyCode;
    private File mLobbyImage;
    private TextChannel mChannel;
    private long channelID;
    private JDA jda;
    
    public CullingCustomGameBot(){
        try {
            this.channelID = 445818343115587585L;
            mRobot = new Robot();
            jda = new JDABuilder(AccountType.BOT).setToken("NDQ1ODE0Mjg5MjkxNDExNDU2.Ddv8hQ.3bf-z0gsSSIMj7K2UHSqEErQ18M").buildBlocking();
            jda.addEventListener(new MessageListener());
            mChannel = jda.getTextChannelById(channelID);
                 
            while(true){
                creatLobby();
               //   convertImageToText();]
                postLobbyCodeInDiscord();
              
                while(!verifyLobby()){
                     TimeUnit.SECONDS.sleep(61);
                }
                startGame();
                leaveGame();
            }
        } catch (AWTException ex) {
            Logger.getLogger(CullingCustomGameBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CullingCustomGameBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LoginException ex) {
            Logger.getLogger(CullingCustomGameBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void creatLobby(){
         try {
            mLobbyCode = "";
            //press play
            mRobot.mouseMove(120,350);    
            mRobot.mousePress(InputEvent.BUTTON1_MASK);
            mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
            
            //delay 1 second
            TimeUnit.SECONDS.sleep(1);
            
            //press custom
            mRobot.mouseMove(660,530);    
            mRobot.mousePress(InputEvent.BUTTON1_MASK);
            mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
            
            //delay 1 second
            TimeUnit.SECONDS.sleep(1);
            
            //press create
            mRobot.mouseMove(660,530);    
            mRobot.mousePress(InputEvent.BUTTON1_MASK);
            mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
            
             //delay 1 second
            TimeUnit.SECONDS.sleep(1);
            
            //press create
            mRobot.mouseMove(1300,820);
            
            //delay 1 second
            TimeUnit.SECONDS.sleep(1);
            
            //Take screenshot of code
            BufferedImage img;
            img = mRobot.createScreenCapture( new Rectangle(1426, 809, 105, 42) );
            String format = "jpg";
            String fileName = "lobbycode." + format;
            mLobbyImage = new File(fileName);
            ImageIO.write(img, format, mLobbyImage);
            
            //delay 1 second
            TimeUnit.SECONDS.sleep(1);
            
        }  catch (InterruptedException ex) {
            Logger.getLogger(CullingCustomGameBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CullingCustomGameBot.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    public void convertImageToText(){
        /*
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
       // instance.setDatapath("C:\\Users\\Ross\\Downloads\\Tess4J\\tessdata\\");
         //ITesseract instance = new Tesseract1(); // JNA Direct Mapping
         instance.setTessVariable("TESSDATA_PREFIX", "C:\\Users\\Ross\\Downloads\\Tess4J\\tessdata\\");
        try {
            String result = instance.doOCR(mLobbyImage);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        */
    }
    private void postLobbyCodeInDiscord() {
    
        Message message = new MessageBuilder().append("Lobby code:").build();
        mChannel.sendFile(mLobbyImage, message).queue();

       // System.out.println(jda.getTextChannels());   
    }
    public boolean verifyLobby(){
        //if 6 people start
            long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");    
        Date resultdate = new Date(yourmilliseconds);
        int mins = resultdate.getMinutes();
        System.out.println(resultdate.getMinutes());
     
        if(mins==30||mins==0){ 
            Message message = new MessageBuilder().append("Starting match!").build();
            mChannel.sendMessage(message).queue();
            return true;
        } else if(mins == 25|| mins ==55){
            Message message = new MessageBuilder().append("Starting match in 5 minutes.").build();
            mChannel.sendMessage(message).queue();
            return false;
        } else {
            return false;
        }

        //else wait for votestart
    }
    public void startGame(){
        try {
            //delay 1 second
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(CullingCustomGameBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        //start lobby
        mRobot.mouseMove(1500,900);    
        mRobot.mousePress(InputEvent.BUTTON1_MASK);
        mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    
    public void leaveGame(){
           try {
            //delay 1 second
            TimeUnit.SECONDS.sleep(20);
            
            int CloseBracketKeyCode = KeyEvent.VK_CLOSE_BRACKET; // the A key
            mRobot.keyPress(CloseBracketKeyCode);
            mRobot.keyRelease(CloseBracketKeyCode);
            
            
            TimeUnit.SECONDS.sleep(1);
            
            int ESCkeyCode = KeyEvent.VK_ESCAPE; // the ESC key
            mRobot.keyPress(ESCkeyCode);
            mRobot.keyRelease(ESCkeyCode);

            TimeUnit.SECONDS.sleep(1);
           //quit game
            mRobot.mouseMove(200,480);    
            mRobot.mousePress(InputEvent.BUTTON1_MASK);
            mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
            
            TimeUnit.SECONDS.sleep(1);
            //return to main menu
            mRobot.mouseMove(760,540);    
            mRobot.mousePress(InputEvent.BUTTON1_MASK);
            mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
            
            TimeUnit.SECONDS.sleep(20);
            
            } catch (InterruptedException ex) {
            Logger.getLogger(CullingCustomGameBot.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    public void leaveLobby(){
         //click leave lobby
        mRobot.mouseMove(760,100);    
        mRobot.mousePress(InputEvent.BUTTON1_MASK);
        mRobot.mouseRelease(InputEvent.BUTTON1_MASK);

        //press esc to main menu
        int ESCkeyCode = KeyEvent.VK_ESCAPE; // the ESC key
        mRobot.keyPress(ESCkeyCode);
        mRobot.keyRelease(ESCkeyCode);
    }
    public void changeMap(){
        //click map change
        mRobot.mouseMove(760,100);    
        mRobot.mousePress(InputEvent.BUTTON1_MASK);
        mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CullingCustomGameBot mBot = new CullingCustomGameBot();
    }

    public class MessageListener extends ListenerAdapter
    {
        @Override
        public void onMessageReceived(MessageReceivedEvent event)
        {
            if (event.isFromType(ChannelType.PRIVATE))
            {
                System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                                        event.getMessage().getContentDisplay());
            }
            else
            {
                System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                            event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                            event.getMessage().getContentDisplay());
            }
        }
    }
}
