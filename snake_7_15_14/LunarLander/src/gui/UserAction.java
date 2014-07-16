package gui;

/**
* this class defines and handles all the actions
*/
public class UserAction {
 
	public static final int NO_ACTION = 0;
	// list page action
	public static final int MOVE_RIGHT = 1;
	public static final int MOVE_LEFT = 2;
 
	public static boolean handleAction(int action, Object object) {
    	switch (action) {
    	case NO_ACTION:
    	default:
        	break;
    	case MOVE_RIGHT:
       	// code to handle the move to right action
        	break;
    	case MOVE_LEFT:
       	// code to handle the move to left action
        	break;
    	}
    	return true;
	}
 
}
