package mod.maxbogomol.wizards_reborn.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.maxbogomol.wizards_reborn.WizardsReborn;
import mod.maxbogomol.wizards_reborn.api.crystalritual.CrystalRitual;
import mod.maxbogomol.wizards_reborn.utils.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CrystalRitualRecipe implements Recipe<Container> {
    public static ResourceLocation TYPE_ID = new ResourceLocation(WizardsReborn.MOD_ID, "crystal_ritual");
    private final ResourceLocation id;
    private final CrystalRitual ritual;
    private final NonNullList<Ingredient> inputs;

    public CrystalRitualRecipe(ResourceLocation id, CrystalRitual ritual, Ingredient... inputs) {
        this.id = id;
        this.ritual = ritual;
        this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return matches(inputs, inv);
    }

    public static boolean matches(List<Ingredient> inputs, Container inv) {
        List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack input = inv.getItem(i);
            if (input.isEmpty()) {
                break;
            }

            int stackIndex = -1;

            for (int j = 0; j < ingredientsMissing.size(); j++) {
                Ingredient ingr = ingredientsMissing.get(j);
                if (ingr.test(input)) {
                    stackIndex = j;
                    break;
                }
            }

            if (stackIndex != -1) {
                ingredientsMissing.remove(stackIndex);
            } else {
                return false;
            }
        }

        return ingredientsMissing.isEmpty();
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

    public CrystalRitual getRecipeRitual() {
        return ritual;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(WizardsReborn.RUNIC_PEDESTAL_ITEM.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WizardsReborn.CRYSTAL_RITUAL_SERIALIZER.get();
    }

    public static class CrystalRitualRecipeType implements RecipeType<CrystalRitualRecipe> {
        @Override
        public String toString() {
            return CrystalRitualRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer implements RecipeSerializer<CrystalRitualRecipe> {

        @Override
        public CrystalRitualRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            for (JsonElement e : ingrs) {
                inputs.add(Ingredient.fromJson(e));
            }

            CrystalRitual crystalRitual = RecipeUtils.deserializeCrystalRitual(GsonHelper.getAsJsonObject(json, "crystal_ritual"));

            return new CrystalRitualRecipe(recipeId, crystalRitual, inputs.toArray(new Ingredient[0]));
        }

        @Nullable
        @Override
        public CrystalRitualRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient[] inputs = new Ingredient[buffer.readInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.fromNetwork(buffer);
            }
            CrystalRitual crystalRitual = RecipeUtils.crystalRitualFromNetwork(buffer);
            return new CrystalRitualRecipe(recipeId, crystalRitual, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrystalRitualRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient input : recipe.getIngredients()) {
                input.toNetwork(buffer);
            }
            RecipeUtils.crystalRitualToNetwork(recipe.getRecipeRitual(), buffer);
        }
    }

    @Override
    public RecipeType<?> getType(){
        return BuiltInRegistries.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial(){
        return true;
    }
}
