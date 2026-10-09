package es.degrassi.experiencelib.util;

import com.google.common.primitives.Ints;
import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExperienceUtils {
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

  public static void addLevelToPlayer(IExperienceHandler handler, int levelDiff, Player player, TransactionContext context) {
    int requestedLevel = player.experienceLevel + levelDiff;
    requestedLevel = Math.max(requestedLevel, 0);
    long playerXP = getPlayerTotalXp(player);
    long requestedXP = getXpFromLevel(requestedLevel) - playerXP;
    long awardXP = levelDiff > 0 ? Math.min(handler.getAmountAsLong(0), requestedXP) : -Math.min(Math.abs(requestedXP),
        handler.getCapacityAsLong(0, ExperienceResource.EMPTY) - handler.getAmountAsLong(0));
    awardXP(handler, Ints.saturatedCast(awardXP), player, context);
  }

  public static void addAllLevelToPlayer(IExperienceHandler handler, boolean give, Player player, TransactionContext context) {
    int awardXP;
    if (give) {
      awardXP = handler.getAmountAsInt(0);
    } else {
      awardXP = -Ints.saturatedCast(Math.min(getPlayerTotalXp(player), handler.getCapacityAsLong(0, ExperienceResource.EMPTY) - handler.getAmountAsLong(0)));
    }
    awardXP(handler, awardXP, player, context);
  }

  public static void awardXP(IExperienceHandler handler, int xpOrigin, Player player, TransactionContext context) {
    int xp = xpOrigin;
    if (xpOrigin > 0) {
      for (int i = 0; i < handler.size(); i++) {
        if (xp <= 0) break;
        xp -= (int) handler.extract(i, xp, context);
      }
    } else {
      for (int i = 0; i < handler.size(); i++) {
        if (xp >= 0) break;
        xp += (int) handler.insert(i, Math.abs(xp), context);
      }
    }
    player.giveExperiencePoints(xpOrigin);
  }
}
