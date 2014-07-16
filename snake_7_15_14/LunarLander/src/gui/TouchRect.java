package gui;

import android.graphics.Rect;

/**
* this class defines an handles touch actions within Rectangle of the screen
*/
	public class TouchRect {
    	protected Rect mRec;
    	protected int mAction;
    	protected boolean mEnabled;
    	private Object mObject; // not used for the time being
    	protected boolean mSelected; // not used for the time being
 
    	public TouchRect(Rect rec, int action, boolean enabled, boolean selected, Object object) {
        	mRec = rec;
        	mAction = action;
        	mEnabled = enabled;
        	mObject = object;
        	mSelected = selected;
    	}
 
    	public int touched(int x, int y) {
        	if (mEnabled == false)	{
            	return UserAction.NO_ACTION;
        	}
        	if ((mRec.left <= x) && (x <= mRec.right) && (mRec.top <= y) && (y <= mRec.bottom)){
            	return mAction;
        	}
        	return UserAction.NO_ACTION;
    	}
    	
    	public int getAction()	{
        	return mAction;
    	}
 
    	public final Object getObject() {
        	return mObject;
    	}
 
    	public final boolean isEnabled() {
        	return mEnabled;
    	}
 
    	public final void setEnabled(boolean mEnabled) {
        	this.mEnabled = mEnabled;
    	}
    	
    	public void setSelected(boolean selected)	{
        	mSelected = selected;
    	}
 
    	public void toggleSelection() {
        	mSelected = !mSelected;
    	}
    	
    	public final boolean isSelected() {
        	return mSelected;
    	}
	}