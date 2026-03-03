package es.degrassi.experiencelib.common.service;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.xei.IExperienceLibAccess;
import es.degrassi.experiencelib.api.xei.emi.ExperienceLibEmiHelper;
import es.degrassi.experiencelib.api.xei.emi.IExperienceLibEmiHelper;
import es.degrassi.experiencelib.api.xei.jei.ExperienceLibJEIHelper;
import es.degrassi.experiencelib.api.xei.jei.IExperienceLibJEIHelper;

public class ExperienceLibAccess implements IExperienceLibAccess {

  @Override
  public IExperienceLibJEIHelper jeiHelper() {
    ExperienceLib.hooks.jei.assertLoaded();
    return ExperienceLibJEIHelper.INSTANCE;
  }

  @Override
  public IExperienceLibEmiHelper emiHelper() {
    ExperienceLib.hooks.emi.assertLoaded();
    return ExperienceLibEmiHelper.INSTANCE;
  }
}
