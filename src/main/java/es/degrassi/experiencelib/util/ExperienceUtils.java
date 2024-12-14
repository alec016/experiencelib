package es.degrassi.experiencelib.util;

import com.google.common.primitives.Ints;
import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ExperienceUtils {
  public static final Material EXPERIENCE = new Material(InventoryMenu.BLOCK_ATLAS, ExperienceLib.rl("block/experience"));
  public static final ResourceLocation EXPERIENCE_GUI = ExperienceLib.rl("textures/gui/experience.png");
  private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,###");

  public static String format(int number) {
    return NUMBER_FORMAT.format(number);
  }

  public static String format(long number) {
    return NUMBER_FORMAT.format(number);
  }

  public static String format(double number) {
    return NUMBER_FORMAT.format(number);
  }

  public static long getXpNeededForNextLevel(long currentLevel) {
    if (currentLevel >= 30) {
      return 112 + (currentLevel - 30) * 9;
    }
    if (currentLevel >= 15) {
      return 37 + (currentLevel - 15) * 5;
    }
    return 7 + currentLevel * 2;
  }

  public static long getXpFromLevel(long level) {
    if (level >= 32)
      return (long) ((4.5 * Math.pow(level, 2)) - 162.5 * level + 2220);
    else if (level >= 17)
      return (long) ((2.5 * Math.pow(level, 2)) - 40.5 * level + 360);
    else
      return (long) (Math.pow(level, 2) + 6L * level);
  }

  public static long getLevelFromXp(long experience) {
    if (experience >= 1508) {
      return (int) ((325.0 / 18) + (Math.sqrt((2.0 / 9) * (experience - (54215.0 / 72)))));
    } else if (experience >= 353) {
      return (int) ((81.0 / 10) + (Math.sqrt((2.0 / 5) * (experience - (7839.0 / 40)))));
    } else {
      return (int) (Math.sqrt(experience + 9) - 3);
    }
  }

  public static long getPlayerTotalXp(Player player) {
    return getXpFromLevel(player.experienceLevel) + (long) Math.floor(player.experienceProgress * getXpNeededForNextLevel(player.experienceLevel));
  }

  public static void addLevelToPlayer(IExperienceHandler handler, int levelDiff, Player player) {
    int requestedLevel = player.experienceLevel + levelDiff;
    requestedLevel = Math.max(requestedLevel, 0);
    long playerXP = ExperienceUtils.getPlayerTotalXp(player);
    long requestedXP = ExperienceUtils.getXpFromLevel(requestedLevel) - playerXP;
    long awardXP = levelDiff > 0 ? Math.min(handler.getExperience(), requestedXP) : -Math.min(Math.abs(requestedXP),
        handler.getExperienceCapacity() - handler.getExperience());
    awardXP(handler, Ints.saturatedCast(awardXP), player);
  }

  public static void addAllLevelToPlayer(IExperienceHandler handler, boolean give, Player player) {
    int awardXP;
    if (give) {
      awardXP = Ints.saturatedCast(handler.getExperience());
    } else {
      awardXP = -Ints.saturatedCast(Math.min(ExperienceUtils.getPlayerTotalXp(player),
          handler.getExperienceCapacity() - handler.getExperience()));
    }
    awardXP(handler, awardXP, player);
  }

  public static void awardXP(IExperienceHandler handler, int xp, Player player) {
    if (xp > 0) {
      handler.extractExperience(xp, false);
    } else {
      handler.receiveExperience(Math.abs(xp), false);
    }
    player.giveExperiencePoints(xp);
  }
}
