package org.uma.jmetal.util.archive.impl;

import java.util.List;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.Archive;
import org.uma.jmetal.util.archive.BoundedArchive;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @param <S>
 */
public abstract class AbstractBoundedArchive<S extends Solution<?>> implements BoundedArchive<S> {
  protected NonDominatedSolutionListArchive<S> archive;
  protected int maxSize;

  public AbstractBoundedArchive(int maxSize) {
    this.maxSize = maxSize;
    this.archive = new NonDominatedSolutionListArchive<>();
  }

  @Override
  public boolean add(S solution) {
    boolean success = archive.add(solution);
    if (success) {
      prune();
    }

    return success;
  }

  @Override
  public S get(int index) {
    return solutions().get(index);
  }

  @Override
  public List<S> solutions() {
    return archive.solutions();
  }

  @Override
  public int size() {
    return archive.size();
  }

  @Override
  public int maximumSize() {
    return maxSize;
  }

  public abstract void prune();

  public Archive<S> join(Archive<S> archive) {
    archive.solutions().forEach(this::add);

    return archive;
  }
}
