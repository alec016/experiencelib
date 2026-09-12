package es.degrassi.experiencelib.api;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ExperienceLibAPI {
  public static final Logger logger = LogUtils.getLogger();
  @ApiStatus.Internal
  private static final ClassLoader SERVICE_CL = ExperienceLibAPI.class.getClassLoader();

  @ApiStatus.Internal
  public static <SERVICE> SERVICE getService(Class<SERVICE> serviceClass) {
    Iterator<SERVICE> service = ServiceLoader.load(serviceClass, SERVICE_CL).iterator();
    if (service.hasNext()) {
      return service.next();
    }

    IllegalStateException illegalStateException = new IllegalStateException("No valid ServiceImpl for " + serviceClass.getSimpleName() + " found");
    ExperienceLibAPI.logger.error("Failed to load service", illegalStateException);
    ExperienceLibAPI.logger.error("CL: {} CCL: {}", SERVICE_CL, Thread.currentThread().getContextClassLoader());
    throw illegalStateException;

  }
}
