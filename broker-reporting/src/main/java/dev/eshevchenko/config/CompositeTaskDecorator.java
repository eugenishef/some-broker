package dev.eshevchenko.config;

import java.util.List;
import org.springframework.core.task.TaskDecorator;

public class CompositeTaskDecorator implements TaskDecorator {

  private final List<TaskDecorator> decorators;

  public CompositeTaskDecorator(List<TaskDecorator> decorators) {
    this.decorators = decorators;
  }

  @Override
  public Runnable decorate(Runnable runnable) {
    Runnable decorated = runnable;
    for (TaskDecorator decorator : decorators) {
      decorated = decorator.decorate(decorated);
    }
    return decorated;
  }
}