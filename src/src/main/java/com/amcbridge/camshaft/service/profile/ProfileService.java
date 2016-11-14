package com.amcbridge.camshaft.service.profile;

import com.amcbridge.camshaft.model.CamShaft;

/**
 * Service to calculate camshaft profiles.
 */
public interface ProfileService {

    /**
     * Calculates camshaft central(theoretical) profile.
     *
     * @param camShaft camshaft model
     */
    void calculateCentralProfile(CamShaft camShaft);

    /**
     * Calculates camshaft practical profile.
     *
     * @param camShaft camshaft model
     */
    void calculatePracticalProfile(CamShaft camShaft);

}
