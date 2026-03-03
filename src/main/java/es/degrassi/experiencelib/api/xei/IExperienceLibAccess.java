package es.degrassi.experiencelib.api.xei;

import es.degrassi.experiencelib.api.ExperienceLibAPI;
import es.degrassi.experiencelib.api.xei.emi.IExperienceLibEmiHelper;
import es.degrassi.experiencelib.api.xei.jei.IExperienceLibJEIHelper;

public interface IExperienceLibAccess {
  IExperienceLibAccess INSTANCE = ExperienceLibAPI.getService(IExperienceLibAccess.class);

  IExperienceLibEmiHelper emiHelper();

  IExperienceLibJEIHelper jeiHelper();
}
