package art.arcane.thaumcraft.util;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public final class FluidHelper {

    public static boolean isTankFull(ResourceHandler<FluidResource> handler) {
        return handler.getAmountAsInt(0) >= handler.getCapacityAsInt(0, FluidResource.EMPTY);
    }

    public static boolean isTankEmpty(ResourceHandler<FluidResource> handler) {
        return handler.getAmountAsInt(0) <= 0;
    }

    public static String serializeTankStatus(ResourceHandler<FluidResource> tank) {
        return String.format("%d/%d [%s]", tank.getAmountAsInt(0), tank.getCapacityAsInt(0, FluidResource.EMPTY), tank.getResource(0).getFluid().getFluidType().getDescription().getString());
    }

    public static String serializeFluidStack(FluidStack stack) {
        return String.format("%dx %s", stack.getAmount(), stack.getFluid().getFluidType().getDescription().getString());
    }
}
