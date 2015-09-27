package me.kellymckinnon.shirtswap;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import me.kellymckinnon.shirtswap.R;

/**
 * Created by Fisher on 8/3/15.
 */
public class CardStackContainer extends RelativeLayout implements View.OnTouchListener {

    private static final String TAG = "CardStackContainer";

    private float mLastTouchX;
    private float mLastTouchY;
    private float mPositionX;
    private float mPositionY;
    private float mOriginX;
    private float mOriginY;

    private CardAdapter mCardAdapter;
    private GestureDetector mGestureDetector;
    private CardView mFrontCard;
    private CardView mBackCard;
    private int mNextPosition;

    private SwipeCallbacks mSwipeCallbacks;

    public CardStackContainer(Context context) {
        this(context, null, 0);
    }

    public CardStackContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardStackContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(context, new FlingListener());
    }

    public void setCardAdapter(CardAdapter cardAdapter) {
        mCardAdapter = cardAdapter;

        DataSetObserver dataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                addFrontCard();
                addBackCard();
            }
        };
        mCardAdapter.registerDataSetObserver(dataSetObserver);

        addFrontCard();

        addBackCard();
    }

    private void addFrontCard() {
        if(mCardAdapter.getCount() > 0 && mFrontCard == null) {
            CardView cardView = mCardAdapter.getView(0, null, this);
            cardView.setCardElevation(8);
            cardView.setOnTouchListener(this);
            mFrontCard = cardView;
            addView(cardView);
            mNextPosition++;
        }
    }

    private void addBackCard() {
        if(mCardAdapter.getCount() > mNextPosition && mBackCard == null) {
            CardView cardView = mCardAdapter.getView(mNextPosition, null, this);
            cardView.setTranslationY(30);
            mBackCard = cardView;
            addView(cardView);
            mNextPosition++;
        }
        bringChildToFront(mFrontCard);
    }

    public void swipeRight() {
        int position = getSwipedPosition();
        swipeCard(true);
        if (mSwipeCallbacks != null) {
            mSwipeCallbacks.onSwipedRight(mCardAdapter.getItem(position));
        }
    }

    private int getSwipedPosition() {
        int position;
        if(mBackCard != null) {
            position = mNextPosition - 2;
        } else {
            position = mNextPosition - 1;
        }
        return position;
    }

    public void swipeLeft() {
        int position = getSwipedPosition();
        swipeCard(false);
        if (mSwipeCallbacks != null) {
            mSwipeCallbacks.onSwipedLeft(mCardAdapter.getItem(position));
        }
    }

    private void swipeCard(boolean swipeRight) {
        if(swipeRight) {
            mFrontCard.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_right));
        } else {
            mFrontCard.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_left));
        }

        removeView(mFrontCard);
        mFrontCard = null;
        if(mBackCard != null) {
            mBackCard.animate().translationY(0).setDuration(200);
            mBackCard.setOnTouchListener(this);
            mBackCard.setCardElevation(8);
            mFrontCard = mBackCard;
            mBackCard = null;
            addBackCard();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)) {
            if(mPositionX < mOriginX) { // check current position vs original position
                swipeCard(false);
            } else {
                swipeCard(true);
            }
            return true;
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();

                mPositionX = v.getX();
                mPositionY = v.getY();

                mOriginX = v.getX();
                mOriginY = v.getY();

                break;
            case MotionEvent.ACTION_UP:
                reset(v);
                break;
            case MotionEvent.ACTION_MOVE:
                float xMove = event.getX();
                float yMove = event.getY();

                float changeX = xMove - mLastTouchX;
                float changeY = yMove - mLastTouchY;

                mPositionX += changeX;
                mPositionY += changeY;

                v.setX(mPositionX);
                v.setY(mPositionY);

                break;
        }
        return true;
    }

    private void reset(View v) {
        mPositionX = mOriginX;
        mPositionY = mOriginY;

        v.animate()
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .x(mOriginX)
                .y(mOriginY);
    }

    public void setSwipeCallbacks(SwipeCallbacks swipeCallbacks) {
        mSwipeCallbacks = swipeCallbacks;
    }

    private class FlingListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "FLING!!");
            return true;
        }
    }

    public interface SwipeCallbacks {
        void onSwipedRight(User user);
        void onSwipedLeft(User user);
    }
}
