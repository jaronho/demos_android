package com.github.florent37.viewanimator;

import android.view.View;

/**
 * Created by florentchampigny on 22/12/2015.
 */
public class AnimationListener {

    private AnimationListener(){}

    public interface Start{
        void onStart();
    }

    public interface Stop{
        void onStop();
    }

    public interface Update{
        void update(View view, float value);
    }
}
