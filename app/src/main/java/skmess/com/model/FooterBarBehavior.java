package skmess.com.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Simple layout behavior that will track the state of the AppBarLayout
 * and match its offset for a corresponding footer.
 */
public class FooterBarBehavior extends CoordinatorLayout.Behavior<FooterBarLayout> {
    //Required to instantiate as a default behavior
    public FooterBarBehavior() {
    }

    //Required to attach behavior via XML
    public FooterBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //This is called to determine which views this behavior depends on
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   FooterBarLayout child,
                                   View dependency) {
        //We are watching changes in the AppBarLayout
        return dependency instanceof AppBarLayout;
    }

    //This is called for each change to a dependent view
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FooterBarLayout child, View dependency) {
        int offset = -dependency.getTop();
        child.setTranslationY(offset);
        return true;
    }
}