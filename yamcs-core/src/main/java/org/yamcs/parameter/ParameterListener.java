package org.yamcs.parameter;

/**
 * This is the interface implemented by the ParameterRequestManager to receive parameters from 
 * the different parameter providers.  
 * 
 * @author nm
 *
 */
public interface ParameterListener {
    /**
     * Called each time some parameters have been updated.
     * 
     * @param params
     *            - new delivered parameter values
     */
    public abstract void update(ParameterValueList params);
}
