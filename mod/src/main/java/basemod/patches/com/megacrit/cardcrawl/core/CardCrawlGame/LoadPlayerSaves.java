package basemod.patches.com.megacrit.cardcrawl.core.CardCrawlGame;

import basemod.BaseMod;
import basemod.abstracts.CustomSavableRaw;
import basemod.patches.com.megacrit.cardcrawl.characters.AbstractPlayer.SeenEvents;
import basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveFile.ModSaves;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Map;

@SpirePatch(clz=CardCrawlGame.class, method="loadPlayerSave")
public class LoadPlayerSaves
{
    public static void Postfix(CardCrawlGame __instance, AbstractPlayer p)
    {
        // Cards
        ModSaves.ArrayListOfJsonElement modCardSaves = ModSaves.modCardSaves.get(CardCrawlGame.saveFile);
        int i = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof CustomSavableRaw) {
                ((CustomSavableRaw)card).onLoadRaw(modCardSaves == null || i >= modCardSaves.size() ? null : modCardSaves.get(i));
            }
            i++;
        }

        // Relics
        ModSaves.ArrayListOfJsonElement modRelicSaves = ModSaves.modRelicSaves.get(CardCrawlGame.saveFile);
        i = 0;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof CustomSavableRaw) {
                ((CustomSavableRaw)relic).onLoadRaw(modRelicSaves == null || i >= modRelicSaves.size() ? null : modRelicSaves.get(i));
            }
            i++;
        }

        // Potions
        ModSaves.ArrayListOfJsonElement modPotionSaves = ModSaves.modPotionSaves.get(CardCrawlGame.saveFile);
        i = 0;
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (potion instanceof CustomSavableRaw) {
                ((CustomSavableRaw)potion).onLoadRaw(modPotionSaves == null || i >= modPotionSaves.size() ? null : modPotionSaves.get(i));
            }
            i++;
        }

        // Seen Events
        ModSaves.ArrayListOfString seenEventSaves = ModSaves.eventSaves.get(CardCrawlGame.saveFile);
        if (seenEventSaves != null)
            SeenEvents.seenEvents.get(AbstractDungeon.player).addAll(seenEventSaves);

        // Custom save fields
        ModSaves.HashMapOfJsonElement modSaves = ModSaves.modSaves.get(CardCrawlGame.saveFile);
        for (Map.Entry<String, CustomSavableRaw> field : BaseMod.getSaveFields().entrySet()) {
            field.getValue().onLoadRaw(modSaves == null ? null : modSaves.get(field.getKey()));
        }
    }
}
