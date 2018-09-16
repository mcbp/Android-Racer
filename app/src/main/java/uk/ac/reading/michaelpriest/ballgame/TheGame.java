package uk.ac.reading.michaelpriest.ballgame;

//Other parts of the android libraries that we use
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TheGame extends GameThread{

    //Player
    private Player player;
    private Road road;
    private ArrayList<Traffic> trafficlist = new ArrayList<Traffic>();
    private int lanes[] = new int[4];
    private Bitmap pause;
    private int highscore;

    //This is run before anything else, so we can prepare things here
    public TheGame(GameView gameView) {
        //House keeping
        super(gameView);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        mCanvasHeight = metrics.heightPixels;
        mCanvasWidth = metrics.widthPixels;

        lanes[0] = (mCanvasWidth - 200) / 4 - 85;
        lanes[1] = (mCanvasWidth - 200) / 2 - 85;
        lanes[2] = (mCanvasWidth - 200) / 4 * 3 - 85;
        lanes[3] = (mCanvasWidth - 200) - 85;

        road = new Road(BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.road), metrics.widthPixels, metrics.heightPixels);
        road.scale(metrics.widthPixels, metrics.heightPixels);
        road.setVector(moveSpeed);

        player = new Player(BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.car_red), metrics.widthPixels, metrics.heightPixels);
        player.scale();

        pause = BitmapFactory.decodeResource(
                gameView.getContext().getResources(),
                    R.drawable.pause);
        pause = Bitmap.createScaledBitmap(pause, (int) (metrics.widthPixels / 6), (int) (metrics.widthPixels / 6), true);

    }

    //This is run before a new game (also after an old game)
    @Override
    public void setupBeginning() {

        moveSpeed = 5;
        highscore = 0;
        if (player != null) {
            player.setX(getScreenWidth()/2 - player.getWidth()/4);
            player.setImage(BitmapFactory.decodeResource
                    (mGameView.getContext().getResources(),
                            R.drawable.car_red));
            player.scale();
        }

        if (trafficlist != null) {
            ArrayList<Traffic> removal = new ArrayList<Traffic>();
            for (Traffic t : trafficlist) {
                removal.add(t);
            }
            trafficlist.removeAll(removal);
        }

        setState(STATE_READY, "Ready");
    }

    @Override
    protected void doDraw(Canvas canvas) {
        //If there isn't a canvas to draw on do nothing
        //It is ok not understanding what is happening here
        if(canvas == null) return;

        super.doDraw(canvas);
        road.draw(canvas);
        player.draw(canvas);
        for(Traffic t : trafficlist) {
            t.draw(canvas);
        }
        canvas.drawBitmap(pause, 30, 30, null);
    }


    //This is run whenever the phone is touched by the user
	@Override
	protected void actionOnTouch(float x, float y, MotionEvent e) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        //pause button
        if(x < pause.getWidth() + 30 && y < pause.getWidth() + 30) {
            player.setLeftActive(false);
            player.setRightActive(false);
            setState(STATE_PAUSE);
            return;
        }

        //right side
        if(x > metrics.widthPixels/2) {
            //press down
            if(e.getAction()==MotionEvent.ACTION_DOWN) {
                    player.setRightActive(false);
                    player.setLeftActive(true);
            }
        }
        //left side
        else {
            //press down
            if(e.getAction()==MotionEvent.ACTION_DOWN) {
                player.setLeftActive(false);
                player.setRightActive(true);
            }
        }

        //Bound check for player
        if(player.getX() <= 0) {
            player.setRightActive(false);
        }
        if(player.getX() >= metrics.widthPixels - player.getWidth()) {
            player.setLeftActive(false);
        }

        //let go of screen
        if(e.getAction()==MotionEvent.ACTION_UP) {
            player.setRightActive(false);
            player.setLeftActive(false);
        }

	}

	//This is run whenever the phone moves around its axises 
	@Override
	protected void actionWhenPhoneMoved(float yDirection, float xDirection, float zDirection) {

	}


    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed, Background bg) {
        //update game objects
        bg.update();
        road.update();
        player.update();

        //intial traffic
        if(trafficlist.size() == 0) {
            Random randZero = new Random();
            trafficlist.add(createTraffic(randZero.nextInt(3),0));
            trafficlist.add(createTraffic(randZero.nextInt(3),2));
        }

        //traffic waves 2 and onwards
        if(trafficlist.get(trafficlist.size()-1).getY() > mCanvasHeight/2) {
            Random rand = new Random();
            int amount = rand.nextInt(3) + 1;
            int lane;

            //Amount of cars
            switch (amount) {
                case 1:
                    trafficlist.add(createTraffic(rand.nextInt(3), rand.nextInt(3)));
                    break;
                case 2:
                    ArrayList<Integer> twoLanes = new ArrayList<Integer>();
                    twoLanes.add(0); twoLanes.add(1); twoLanes.add(3); twoLanes.add(3);
                    Collections.shuffle(twoLanes);
                    trafficlist.add(createTraffic(rand.nextInt(3), twoLanes.get(0)));
                    trafficlist.add(createTraffic(rand.nextInt(3), twoLanes.get(1)));
                    break;
                case 3:
                    lane = rand.nextInt(3);
                    if(lane != 0) {trafficlist.add(createTraffic(rand.nextInt(3), 0));}
                    if(lane != 1) {trafficlist.add(createTraffic(rand.nextInt(3), 1));}
                    if(lane != 2) {trafficlist.add(createTraffic(rand.nextInt(3), 2));}
                    if(lane != 3) {trafficlist.add(createTraffic(rand.nextInt(3), 3));}
                    break;
            }

        }

        for(Traffic t: trafficlist) {
            t.update();
        }

        //collision detection
        if(dirtCollision()) {
            if(moveSpeed > 5) {
                moveSpeed -= moveSpeed * 0.02;
            }
        } else if(carCollision(trafficlist)) {
            setState(STATE_LOSE, "Highest speed\nachieved: " + highscore);
        } else {
            //increase speed
            moveSpeed+=0.02;
        }

        for(Traffic t: trafficlist) {
            t.setVector(moveSpeed - 2);
        }

        road.setVector(moveSpeed);
        bg.setVector(moveSpeed);

        for(Traffic t: trafficlist) {
            if(t.getY() > mCanvasHeight + 500) {
                trafficlist.remove(t);
            }
        }

        ArrayList<Traffic> removal = new ArrayList<Traffic>();
        for(Traffic t : trafficlist) {
            if(t.getY() > mCanvasHeight + 400) {
                removal.add(t);
            }
        }
        trafficlist.removeAll(removal);

        setScore((long) moveSpeed);
        if (moveSpeed > highscore) {
            highscore = (int)moveSpeed;
        }

    }

    public boolean dirtCollision() {
        if(player.getX() < 90 || player.getX() > getScreenWidth() - 90 - player.getWidth()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean carCollision(ArrayList<Traffic> list) {
        Rect rectPlayer = new Rect(player.getX(), player.getY(), player.getX() + player.getWidth(), player.getY() + player.getHeight());
        for(Traffic t: list) {
            if (rectPlayer.intersect(t.getX()+10, t.getY()+5, t.getX() + t.getWidth()-10, t.getY() + t.getHeight()-5)) {
                player.setImage(BitmapFactory.decodeResource
                        (mGameView.getContext().getResources(),
                                R.drawable.explosion));
                player.scale();
                return true;
            }
        }
        return false;
    }

    public Traffic createTraffic(int car, int lane) {
        if(car == 0) {
            return new Traffic((BitmapFactory.decodeResource(
                    mGameView.getContext().getResources(), R.drawable.car_yellow)),
                    mCanvasWidth, mCanvasHeight, lanes[lane]);
        } else if (car == 1) {
            return new Traffic((BitmapFactory.decodeResource(
                    mGameView.getContext().getResources(), R.drawable.car_blue)),
                    mCanvasWidth, mCanvasHeight, lanes[lane]);
        } else {
            return new Traffic((BitmapFactory.decodeResource(
                    mGameView.getContext().getResources(), R.drawable.car_teal)),
                    mCanvasWidth, mCanvasHeight, lanes[lane]);
        }
    }

    public void increaseGameSpeed() {

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
