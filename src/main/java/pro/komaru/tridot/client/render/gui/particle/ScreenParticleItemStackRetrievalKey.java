package pro.komaru.tridot.client.render.gui.particle;

public record ScreenParticleItemStackRetrievalKey(boolean isHotbarItem, boolean isRenderedAfterItem, int x, int y){

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ScreenParticleItemStackRetrievalKey key)){
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isRenderedAfterItem == isRenderedAfterItem && key.x == x && key.y == y;
    }
}