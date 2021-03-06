package GUI;

import Game.data.MusicPlayer;
import Logic.Lists.*;
import Logic.Trees.BTree;
import Server.Server;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import Game.Gryphon;
import Game.Attack;
import Game.Dragon;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private int BACKGROUND_WIDTH = 1200;
    private ParallelTransition parallelTransition;
    private int level = 1;
    private boolean inFormation = true;
    private double enemyShoot = 0;
    private int whichFormation = 0;
    private int batchOfEnemies = 100;
    private AnimationTimer timer;
    private boolean pause = false;
    public static int lose = 0;
    private boolean win = false;
    public static MusicPlayer musicPlayer = new MusicPlayer();
    @FXML private Text sideText;
    @FXML private Text sideText2;
    @FXML private AnchorPane paneBoard;
    @FXML private ImageView background1;
    @FXML private ImageView background2;
    @FXML private Label livesLeftTxt;
    @FXML private Label enemiesLeftTxt;
    @FXML private Label levelTxt;
    @FXML private Label currentOrderTxt;
    @FXML private ScrollPane scrollPane;
    @FXML private ScrollPane scrollPane2;
    public static Gryphon player;
    final Logger logger = LoggerFactory.getLogger(Game.class);

    /**
     * Defines player´s first skin (image rute)
     */
    public void choosePlayer1 (){
        logger.info("The player choose Charizard.");
        Holder.playerRute ="file:src/Media/Players/Charizard.gif";
    }

    /**
     * Defines player´s second skin (image rute)
     */
    public void choosePlayer2 (){
        logger.info("The player choose Pony.");
        Holder.playerRute ="file:src/Media/Players/Pony.gif";
    }

    /**
     * Defines player´s third skin (image rute)
     */
    public void choosePlayer3 (){
        logger.info("The player choose Flying Beast.");
        Holder.playerRute = "file:src/Media/Players/FlyingBeast.gif";
    }

    /**
     * Defines player´s fourth skin (image rute)
     */
    public void choosePlayer4 (){
        logger.info("The player choose Pink Pegasus.");
        Holder.playerRute = "file:src/Media/Players/PinkPegasus.gif";
    }

    /**
     * Defines enemies´ first skin (image rute)
     */
    public void chooseEnemy1 (){
        logger.info("The player choose enemy Black Dragon.");
        Holder.enemyRute = "file:src/Media/Enemies/BlackDragon.gif";
    }

    /**
     * Defines enemies´ second skin (image rute)
     */
    public void chooseEnemy2 (){
        logger.info("The player choose enemy Green Dragon.");
        Holder.enemyRute = "file:src/Media/Enemies/GreenDragon.gif";
    }

    /**
     * Defines enemies´ third skin (image rute)
     */
    public void chooseEnemy3 (){
        logger.info("The player choose enemy Nightfury.");
        Holder.enemyRute = "file:src/Media/Enemies/Nightfury.gif";
    }

    /**
     * Defines enemies´ fourth skin (image rute)
     */
    public void chooseEnemy4 (){
        logger.info("The player choose enemy Yellow Dragon.");
        Holder.enemyRute = "file:src/Media/Enemies/YellowDragon.gif";
    }

    /**
     * Changes scene from Menu to Instructions
     */
    public void runInstructions () throws IOException {
        logger.info("Showing instructions screen.");
        Main.setScene("Instructions.fxml");
    }

    /**
     * Changes scene from Instructions to SetUp
     */
    public void runSetUp () throws IOException {
        logger.info("Showing Set Up screen.");
        Main.setScene("SetUp.fxml");
    }

    /**
     * Changes scene from SetUp to Board
     */
    public void runBoard () throws IOException {
        logger.info("Showing the main game screen.");
        Main.setScene("Board.fxml");
    }

    /**
     * startGame is a method called by Main class to start the game
     */
    public void startGame(){

        parallelTransition.play();

        this.setLivesLeftTxt(10);

        player = new Gryphon(9, 50, 100, 130, 80, Holder.playerRute);
        paneBoard.getChildren().add(player);

        Main.scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    if (!this.pause){
                        player.moveLeft();
                    }
                    break;
                case D:
                    if (!this.pause){
                        player.moveRight();
                    }
                    break;
                case W:
                    if (!this.pause){
                        player.moveUp();
                    }
                    break;
                case S:
                    if (!this.pause){
                        player.moveDown();
                    }
                    break;
                case K:
                    if (this.inFormation && !player.isDead() && !this.pause){
                        this.shoot();
                    }
                    break;
                case P:
                    if (!this.pause){
                        timer.stop();
                        this.pause = true;
                        logger.info("The game is paused.");
                    }else{
                        timer.start();
                        this.pause = false;
                        logger.info("The game continues.");
                    }
                    break;
                case ESCAPE:
                    logger.info("Exiting the game.");
                    System.exit(0);
                    break;
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        musicPlayer.setPath("src/Media/Audio/DesertTheme.mp3");
        musicPlayer.run();

        logger.info("Setting up the game.");

        try {
            this.callServerToGenerateList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * shoot() is a method used to create and move player's bullets
     */
    public void shoot(){
        Attack attack = new Attack(0, player.getPosx() + 150, player.getPosy(), 40, 40, "file:src/Media/Bullets/Green Bullet.png", "playerbullet");
        paneBoard.getChildren().add(attack);
        BulletsList.getInstance().addBullet(attack);
    }

    /**
     * shoot(Dragon dragon) is a method used to create and move enemies' bullets
     */
    public void shoot(Dragon dragon){
        if (dragon.getPosx() < 900){
            Attack attack = new Attack(0, dragon.getPosx(), dragon.getPosy() + 20, 40, 40, "file:src/Media/Bullets/Fire.png", "enemybullet");
            paneBoard.getChildren().add(attack);
            BulletsList.getInstance().addBullet(attack);
        }
    }

    /**
     * addEnemies is a method used to add enemies
     */
    public void addEnemies(){
        DragonList list = DragonList.getInstance();
        DragonNode tmp = list.head;
        Image img = new Image(Holder.enemyRute);
        ImagePattern ing = new ImagePattern(img);
        logger.info("Putting enemies on screen.");
        while (tmp != null) {
            tmp.getDragon().setFill(ing);
            paneBoard.getChildren().add(tmp.getDragon());
            tmp = tmp.next;
        }
        this.scrollPane.toFront();
        this.scrollPane2.toFront();
        this.setEnemiesLeftTxt(this.batchOfEnemies);
        this.setLevelTxt(this.level);
        this.setCurrentOrderTxt("Random");
        logger.info("Setting up BTree.");
        this.showBTree();
        logger.info("Start.");
    }

    /**
     * Method to call the server
     * @throws IOException in case something goes wrong.
     */
    public void callServerToGenerateList() throws IOException {
        SendList sl = Server.generate(this.batchOfEnemies);
        logger.info("Getting enemies from the server.");
        SendNode tmp = sl.head;
        while (tmp != null){
            DragonData sub_tmp = tmp.getDragonData();
            Dragon dragon = new Dragon(sub_tmp.getResistence(), sub_tmp.getName(), sub_tmp.getdRSpeed(), sub_tmp.getdAge(), sub_tmp.getdClas(), sub_tmp.getPosx(), sub_tmp.getPosy(), 80, 140, Holder.enemyRute, sub_tmp.getID());
            DragonList.getInstance().addEnemy(dragon);
            dragon.setOnMouseClicked(e -> this.showDragonStats(dragon.getName(), dragon.getRechargeSpeed(), dragon.getAge(), dragon.getResistence() + 1, dragon.getClas()));
            tmp = tmp.next;
        }
        this.addEnemies();
    }

    /**
     * Method to advance to the next level.
     */
    private void nextLevel(){
        this.level++;
        if (this.level == 6){
            logger.info("The player win.");
            player.setDead(true);
            this.win = true;
        }else{
            int lives = player.getResistence();
            lives++;
            this.setLivesLeftTxt(lives);
            player.setResistence(lives);
            this.batchOfEnemies += (20 * this.batchOfEnemies) / 100;
            this.whichFormation = 0;
            this.readLevel();
            Server.count = 1;
            try {
                this.callServerToGenerateList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void end(){
        DragonList tmp2 = DragonList.getInstance();
        paneBoard.getChildren().remove(player);
        this.restartBackground();
        timer.stop();
        this.level = 1;
        try {
            musicPlayer.stopMusic();
            player.setResistence(9);
            Server.count = 1;
            this.whichFormation = 0;
            DragonNode sub_tmp2 = tmp2.head;
            while (sub_tmp2 != null){
                Dragon sub_sub_tmp2 = sub_tmp2.getDragon();
                paneBoard.getChildren().remove(sub_sub_tmp2);
                sub_tmp2 = sub_tmp2.next;
            }
            DragonList.reset();
            this.runSetUp();
        } catch (IOException e) {
            this.errorWindow();
        }
    }

    /**
     * update() is a method used to refresh and update game data
     */
    private void update() throws NullPointerException{
        BulletsList tmp = BulletsList.getInstance();
        DragonList tmp2 = DragonList.getInstance();
        this.enemyShoot += 0.016;
        if (player.isDead() || lose == 3){
            if (!win){
                logger.info("The player lose.");
            }
            this.end();
        }if (tmp.getLarge() != 0){
            BulletsNodes sub_tmp = tmp.head;
            while (sub_tmp != null){
                Attack sub_sub_tmp = sub_tmp.getAttack();
                if (sub_sub_tmp.getWho().equals("playerbullet")){
                    sub_sub_tmp.moveRight();
                    DragonNode sub_tmp2 = tmp2.head;
                    while (sub_tmp2 != null){
                        Dragon sub_sub_tmp2 = sub_tmp2.getDragon();
                        if (sub_sub_tmp.getBoundsInParent().intersects(sub_sub_tmp2.getBoundsInParent())) {
                            sub_sub_tmp.setDead(true);
                            sub_sub_tmp2.hit();
                            break;
                        }
                        sub_tmp2 = sub_tmp2.next;
                    }
                    BulletsNodes sub_tmp3 = tmp.head;
                    while (sub_tmp3 != null){
                        Attack sub_sub_tmp3 = sub_tmp3.getAttack();
                        if (!sub_sub_tmp3.getWho().equals("playerbullet")){
                            if (sub_sub_tmp.getBoundsInParent().intersects(sub_sub_tmp3.getBoundsInParent())){
                                sub_sub_tmp.setDead(true);
                                sub_sub_tmp3.setDead(true);
                                break;
                            }
                        }
                        sub_tmp3 = sub_tmp3.next;
                    }
                    if (sub_sub_tmp.isDead()) {
                        paneBoard.getChildren().remove(sub_sub_tmp);
                        BulletsList.getInstance().deleteBullet(sub_sub_tmp);
                    }
                    sub_tmp = sub_tmp.next;
                }else{
                    sub_sub_tmp.moveLeft();
                    if (sub_sub_tmp.getBoundsInParent().intersects((player.getBoundsInParent()))) {
                        sub_sub_tmp.setDead(true);
                        player.hit();
                        logger.info("The player got hit.");
                        this.setLivesLeftTxt(player.getResistence() + 1);
                    }if (sub_sub_tmp.isDead()) {
                        paneBoard.getChildren().remove(sub_sub_tmp);
                        BulletsList.getInstance().deleteBullet(sub_sub_tmp);
                    }
                    sub_tmp = sub_tmp.next;
                }
            }
        }if (tmp2.getLarge() != 0) {
            DragonNode sub_tmp2 = tmp2.head;
            while (sub_tmp2 != null) {
                Dragon sub_sub_tmp2 = sub_tmp2.getDragon();
                if (inFormation) {
                    sub_sub_tmp2.moveLeft();
                    if (sub_sub_tmp2 == tmp2.head.getDragon()) {
                        tmp2.setLeftest(sub_sub_tmp2.getPosx());
                    }
                    if (this.enemyShoot > 2) {
                        if (Math.random() < 0.5 && sub_sub_tmp2.getRechargeSpeed() >= 70) {
                            this.shoot(sub_sub_tmp2);
                        }
                        if (Math.random() < 0.3 && sub_sub_tmp2.getRechargeSpeed() < 70 && sub_sub_tmp2.getRechargeSpeed() >= 40) {
                            this.shoot(sub_sub_tmp2);
                        }
                        if (Math.random() < 0.2 && sub_sub_tmp2.getRechargeSpeed() < 40) {
                            this.shoot(sub_sub_tmp2);
                        }
                    }
                }
                if (sub_sub_tmp2.isDead()) {
                    paneBoard.getChildren().remove(sub_sub_tmp2);
                    DragonList.getInstance().deleteEnemy(sub_sub_tmp2);
                    this.setEnemiesLeftTxt(DragonList.getInstance().getLarge());
                    logger.info("An enemy died.");
                    if (DragonList.getInstance().getLarge() != 0){
                        this.inFormation = false;
                        this.changeFormation();
                        break;
                    }else{
                        this.nextLevel();
                        break;
                    }
                }
                sub_tmp2 = sub_tmp2.next;
            }
        }if (!this.inFormation){
            DragonNode sub_tmp2 = tmp2.head;
            while (sub_tmp2 != null){
                sub_tmp2.getDragon().moveToX();
                sub_tmp2.getDragon().moveToY();
                sub_tmp2 = sub_tmp2.next;
            }
            if (tmp2.allReady()){
                this.inFormation = true;
            }
        }if (this.enemyShoot > 2) {
            this.enemyShoot = 0;
        }
    }

    /**
     * Method to change the current formation of the game.
     */
    public void changeFormation(){
        if (this.whichFormation == 0){
            logger.info("Changing formation to: Selection Sort by age.");
            this.setCurrentOrderTxt("Selection Sort");
            this.getNewList();
        }else if (this.whichFormation == 1){
            logger.info("Changing formation to: Insertion Sort by recharge speed.");
            this.setCurrentOrderTxt("Insertion Sort");
            this.getNewList();
        }else if (this.whichFormation == 2){
            logger.info("Changing formation to: Quick Sort by age.");
            this.setCurrentOrderTxt("Quick Sort");
            this.getNewList();
        }else if (this.whichFormation == 3){
            logger.info("Changing formation to: Binary Tree by families.");
            this.setCurrentOrderTxt("Binary Tree");
            this.getNewList();
        }else{
            logger.info("Changing formation to: AVL Tree by age.");
            this.setCurrentOrderTxt("AVL Tree");
            this.getNewList();
            this.whichFormation = 0;
        }
    }

    public void getNewList(){
        logger.info("Getting data from server.");
        SendList sl = new SendList();
        DragonList dl = DragonList.getInstance();
        DragonNode tmp = dl.head;
        while (tmp != null){
            Dragon dragon = tmp.getDragon();
            sl.addData(dragon.getAge(),dragon.getRechargeSpeed(), dragon.getClas(), dragon.getPosx(), dragon.getPosy(), dragon.getID(), dragon.getName(), dragon.getResistence());
            tmp = tmp.next;
        }
        try {
            SendList s2 = Server.sort(sl);
            DragonNode tmp3;
            SendNode tmp2 = s2.head;
            while (tmp2 != null){
                DragonData sub_tmp2 = tmp2.getDragonData();
                tmp3 = dl.head;
                while (tmp3 != null){
                    Dragon sub_tmp3 = tmp3.getDragon();
                    if (sub_tmp2.getID() == sub_tmp3.getID()){
                        sub_tmp3.setPosx(sub_tmp2.getPosx());
                        sub_tmp3.setPosy(sub_tmp2.getPosy());
                        break;
                    }else{
                        tmp3 = tmp3.next;
                    }
                }
                tmp2 = tmp2.next;
            }
            this.whichFormation++;
            this.showBTree();
        } catch (IOException e) {
           this.errorWindow();
        }
    }

    public void errorWindow(){
        logger.error("A problem happened while trying to get data from the server.");
        Platform.exit();
    }

    public void showDragonStats(String name, int frs, int age, int resistence, String clas){
        logger.info("Showing data from: " + name);
        String txt = "Dragons Stats:\n" +
                "Name: " +
                name +
                " \n " +
                "Fire Recharge Speed: " +
                frs +
                " \n " +
                "Age: " +
                age +
                " \n " +
                "Resistence: " +
                resistence +
                " \n " +
                "Class: " +
                clas;
        this.sideText.setText(txt);
    }

    public void showBTree(){
        BTree<String> bt = new BTree<>();
        DragonNode tmp = DragonList.getInstance().head;
        while (tmp != null){
            Dragon sub_tmp = tmp.getDragon();
            bt.add(sub_tmp.getName());
            tmp = tmp.next;
        }
        String txt = String.valueOf(bt);
        this.sideText2.setText(txt);
    }

    /**
     * initialize is a method used to add a moving background to the paneBoard
     */
    @FXML
    public void initialize() {

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(10000), background1);
        translateTransition.setFromX(0);
        translateTransition.setToX(-1 * BACKGROUND_WIDTH);
        translateTransition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(10000), background2);
        translateTransition2.setFromX(0);
        translateTransition2.setToX(-1 * BACKGROUND_WIDTH);
        translateTransition2.setInterpolator(Interpolator.LINEAR);

        parallelTransition = new ParallelTransition( translateTransition, translateTransition2 );
        parallelTransition.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * setLivesLeftTxt() is a method used to set livesLeftTxt label text
     */
    public void setLivesLeftTxt (int lives){
        Integer.toString(lives);
        livesLeftTxt.setText("Lives left: " + lives);
    }

    /**
     * setEnemiesLeftTxt() is a method used to set enemiesLeftTxt label text
     */
    public void setEnemiesLeftTxt (int enemies){
        Integer.toString(enemies);
        enemiesLeftTxt.setText("Enemies left: " + enemies);
    }

    /**
     * setLevelTxt() is a method used to set levelTxt label text
     */
    public void setLevelTxt (int level){
        Integer.toString(level);
        levelTxt.setText("Level: " + level);
    }

    /**
     * setCurrentOrderTxt() is a method used to set currentOrderTxt label text
     */
    public void setCurrentOrderTxt (String currentOrder){
        currentOrderTxt.setText("Current order: " + currentOrder);
    }

    /**
     * readLevel() is a method used to set the background image based on the player´s level
     */
    public void readLevel (){
        logger.info("Setting up level:" + this.level);
        if (this.level == 2){
            musicPlayer.stopMusic();
            musicPlayer.setPath("src/Media/Audio/CastleTheme.mp3");
            musicPlayer.run();
            Image image = new Image("file:src/Media/Stages/CastleStage.png");
            background1.setImage(image);
            background2.setImage(image);
        }
        else if (this.level == 3){
            musicPlayer.stopMusic();
            musicPlayer.setPath("src/Media/Audio/ForestTheme.mp3");
            musicPlayer.run();
            Image image2 = new Image("file:src/Media/Stages/ForestStage.png");
            background1.setImage(image2);
            background2.setImage(image2);
        }
        else if (this.level == 4){
            musicPlayer.stopMusic();
            musicPlayer.setPath("src/Media/Audio/UnderwaterTheme.mp3");
            musicPlayer.run();
            Image image3 = new Image("file:src/Media/Stages/UnderwaterStage.png");
            background1.setImage(image3);
            background2.setImage(image3);
        }
        else if (this.level == 5){
            musicPlayer.stopMusic();
            musicPlayer.setPath("src/Media/Audio/SpaceTheme.mp3");
            musicPlayer.run();
            Image image4 = new Image("file:src/Media/Stages/SpaceStage.png");
            background1.setImage(image4);
            background2.setImage(image4);
        }
    }

    public void restartBackground(){
        Image image = new Image("file:src/Media/Stages/DesertStage.png");
        background1.setImage(image);
        background2.setImage(image);
    }
}
