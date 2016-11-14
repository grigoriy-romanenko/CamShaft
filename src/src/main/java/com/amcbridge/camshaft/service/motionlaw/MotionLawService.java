package com.amcbridge.camshaft.service.motionlaw;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.MotionLawFragment;

import java.util.stream.Stream;

/**
 * Service to modify camshaft follower motion law.
 */
public interface MotionLawService {

    /**
     * Creates sequence of follower motion law points using specified fragment and adds it to camshaft.
     *
     * @param camShaft camshaft model that stores follower motion law
     * @param fragment params to create sequence of follower motion law points
     */
    void setMotionLawFragment(CamShaft camShaft, MotionLawFragment fragment);

    /**
     * Parses follower motion law points from specified lines stream and sets it to camshaft.
     *
     * @param camShaft camshaft model that stores follower motion law
     * @param lines lines stream
     */
    void readMotionLaw(CamShaft camShaft, Stream<String> lines);

}
