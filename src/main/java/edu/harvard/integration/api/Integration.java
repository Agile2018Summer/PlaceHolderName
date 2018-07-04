package edu.harvard.integration.api;

import edu.harvard.integration.Integrator;

/**
 * A api interface for the integrations {@link Integrator} includes.
 */
public interface Integration {
    /**
     * Gracefully stop this {@link Integration}.
     */
    void stop();
}