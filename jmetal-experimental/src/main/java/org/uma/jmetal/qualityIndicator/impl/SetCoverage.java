package org.uma.jmetal.qualityIndicator.impl;

import org.uma.jmetal.qualityIndicator.QualityIndicator;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.front.util.FrontUtils;

import java.io.FileNotFoundException;

/**
 * Set coverage metric
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SetCoverage extends QualityIndicator {

  /**
   * Constructor
   */
  public SetCoverage() {
  }

  /**
   * Constructor
   *
   * @param referenceFront
   * @throws FileNotFoundException
   */
  public SetCoverage(double[][] referenceFront) {
    super(referenceFront) ;
  }

  @Override
  public double compute(double[][] front) {
    Check.isNotNull(front);

    return compute(front, referenceFront);
  }

  @Override
  public boolean isTheLowerTheIndicatorValueTheBetter() {
    return false;
  }

  /**
   * Calculates the set coverage of a front over a reference front
   * @param front
   * @param referenceFront
   * @return The value of the set coverage
   */
  public double compute(double[][] front, double[][] referenceFront) {
    double result ;
    int sum = 0 ;

    if (referenceFront.length == 0) {
      if (front.length ==0) {
        result = 0.0 ;
      } else {
        result = 1.0 ;
      }
    } else {
      for (double[] vector : referenceFront) {
        if (FrontUtils.isVectorDominatedByFront(vector, front)) {
          sum++;
        }
      }
      result = (double)sum/referenceFront.length ;
    }
    return result ;
  }

  @Override public String getName() {
    return "Set coverage";
  }

  @Override public String getShortName() {
    return "SC";
  }
}
