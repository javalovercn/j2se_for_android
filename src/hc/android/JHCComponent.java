package hc.android;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;

public abstract class JHCComponent extends JComponent {
	private Action action;
	private PropertyChangeListener actionPropertyChangeListener;
	protected String actionCommand;

	public Action getAction() {
		return action;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void removeActionListener(ActionListener l) {
		if ((l != null) && (getAction() == l)) {
			setAction(null);
		} else {
			list.remove(ActionListener.class, l);
		}
	}

	protected void configurePropertiesFromAction(Action a) {
		setEnabled((a != null) ? a.isEnabled() : true);
		// AbstractAction.setEnabledFromAction(this, a);need friend in same
		// package.
		// AbstractAction.setToolTipTextFromAction(this, a);need friend in same
		// package.
		setActionCommandFromAction(a);
	}

	protected void setActionCommandFromAction(Action a) {
		setActionCommand((a != null) ? (String) a.getValue(Action.ACTION_COMMAND_KEY) : null);
	}

	public void setActionCommand(String aCommand) {
		actionCommand = aCommand;
	}

	public void setAction(Action a) {
		Action oldValue = getAction();
		if (action == null || !action.equals(a)) {
			action = a;
			if (oldValue != null) {
				removeActionListener(oldValue);
				actionPropertyChangeListener = null;
			}
			// configurePropertiesFromAction(action);
			if (action != null) {
				if (!isListener(ActionListener.class, action)) {
					addActionListener(action);
				}
			}
		}
	}

	protected abstract PropertyChangeListener createActionPropertyChangeListener(Action a);

	protected boolean isListener(Class c, ActionListener a) {
		boolean isListener = false;
		Object[] listeners = list.getListeners(c);
		for (int i = listeners.length - 1; i >= 0; i--) {
			if (listeners[i] == a) {
				isListener = true;
			}
		}
		return isListener;
	}

	public void addActionListener(ActionListener l) {
		list.add(ActionListener.class, l);
	}
}
